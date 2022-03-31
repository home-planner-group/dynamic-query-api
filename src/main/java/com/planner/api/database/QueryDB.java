package com.planner.api.database;

import com.planner.api.model.QueryResponse;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@ApplicationScoped //@RequestScoped?
public class QueryDB {

    private static final String MYSQL_DRIVER_NAME = "com.mysql.jdbc.Driver";

    @ConfigProperty(name = "database.url")
    String dbUrl;

    @ConfigProperty(name = "database.user")
    String username;

    @ConfigProperty(name = "database.password")
    String password;


    /**
     * <p>Loads JDBC Driver for connections. Is loaded on construct to make sure it is there.</p>
     * <p>Additional {@link DriverManager} configuration.</p>
     *
     * @throws ClassNotFoundException if driver is not found.
     */
    @PostConstruct
    private void postConstruct() throws ClassNotFoundException {
        Class.forName(MYSQL_DRIVER_NAME);
        DriverManager.setLoginTimeout(5);
    }


    public QueryResponse executeStaticStatement(String resourcePath) throws IOException, SQLException {
        return executeStatement(readQueryStatement(resourcePath));
    }

    public QueryResponse executeStatement(String sqlStatement) throws SQLException {
        // init response with empty body
        QueryResponse queryResult = new QueryResponse();
        // establish database connection & execute sqlStatement
        try (Connection connection = createConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sqlStatement)) {
            // analyse meta data
            queryResult.columnDef = extractColumnDef(resultSet);
            // go through result set
            while (resultSet.next()) {
                queryResult.instanceCount++;
                try {
                    // extract data
                    queryResult.rows.add(extractRow(queryResult.columnDef, resultSet));
                } catch (Exception exception) {
                    // log error
                    queryResult.errorCount++;
                    queryResult.errorMessages.add(exception.getClass().getSimpleName() + ": " + exception.getMessage());
                }
            }
        }
        return queryResult;
    }

    private Connection createConnection() throws SQLException {
        return DriverManager.getConnection(dbUrl, username, password);
    }

    private Map<Integer, String> extractColumnDef(ResultSet resultSet) throws SQLException {
        Map<Integer, String> columnDef = new HashMap<>();
        ResultSetMetaData metaData = resultSet.getMetaData();

        for (int colIndex = 0; colIndex < metaData.getColumnCount(); colIndex++) {
            // meta data column count starts at 1
            columnDef.put(colIndex, metaData.getColumnName(colIndex + 1));
        }
        return columnDef;
    }

    private Object[] extractRow(Map<Integer, String> columnDef, ResultSet resultSet) throws SQLException {
        Object[] row = new Object[columnDef.keySet().size()];

        for (Integer columnIndex : columnDef.keySet()) {
            row[columnIndex] = resultSet.getObject(columnDef.get(columnIndex));
        }
        return row;
    }

    /**
     * Reads a file from resources with path from resource root:
     * <p>sql-statements/file.sql</p>
     *
     * @return content as String
     * @throws IOException file not found
     */
    protected String readQueryStatement(String resourcePath) throws IOException {
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(
                        Objects.requireNonNull(
                                QueryDB.class.getClassLoader().getResourceAsStream(resourcePath))))) {
            return reader.lines().collect(Collectors.joining("\n"));
        }
    }
}
