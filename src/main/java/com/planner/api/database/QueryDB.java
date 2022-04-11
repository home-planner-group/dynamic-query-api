package com.planner.api.database;

import com.planner.api.model.QueryResponse;
import com.planner.api.utility.FileReader;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@RequestScoped
public class QueryDB {

    private final Logger LOGGER = Logger.getLogger(QueryDB.class.getName());

    @Inject
    ConnectionBuilder connectionBuilder;

    @Inject
    FileReader fileReader;

    /**
     * @param fileName in the directory: resources/sql-statements/fileName
     * @return extracted response
     * @throws IOException  if file not found
     * @throws SQLException if a database access error occurs or this method is called on a closed result set
     */
    public QueryResponse executeStaticStatement(String fileName) throws IOException, SQLException {
        return executeStatementOnDefaultDB(fileReader.readStaticSqlStatement(fileName));
    }

    /**
     * Execute statement on default database.
     * Executes all kind of statements. SELECT will extract a full response.
     * Any other statement will return the modified rows.
     *
     * @param sqlStatement example: SELECT * FROM table
     * @return extracted response
     * @throws SQLException if a database access error occurs or this method is called on a closed result set
     */
    public QueryResponse executeStatementOnDefaultDB(String sqlStatement) throws SQLException {
        LOGGER.info("Create connection to default database.");
        LOGGER.info("Execute SQL statement: " + sqlStatement);

        QueryResponse queryResult;
        // establish database connection & execute sqlStatement
        try (Connection connection = connectionBuilder.createConnection();
             Statement statement = connection.createStatement()) {
            if (sqlStatement.toLowerCase().startsWith("select")) {
                try (ResultSet resultSet = statement.executeQuery(sqlStatement)) {
                    queryResult = extractResponse(resultSet);
                }
            } else {
                queryResult = new QueryResponse();
                queryResult.instanceCount = statement.executeUpdate(sqlStatement);
            }
        }
        return queryResult;
    }

    /**
     * Execute statement on custom database. Only executes statements with query response, that means only SELECT statements are possible.
     *
     * @param dbUrl        pattern: jdbc:mysql://host:port/database-name
     * @param username     example: root
     * @param password     example: password
     * @param sqlStatement example: SELECT * FROM table
     * @return extracted response
     * @throws SQLException if a database access error occurs or this method is called on a closed result set
     */
    public QueryResponse executeStatementOnCustomDB(String dbUrl, String username, String password, String sqlStatement) throws SQLException {
        LOGGER.info("Create connection to '" + dbUrl + "' with user '" + username + "'");
        LOGGER.info("Execute dynamic SQL statement: " + sqlStatement);

        QueryResponse queryResult;
        // establish database connection & execute sqlStatement
        try (Connection connection = connectionBuilder.createConnection(dbUrl, username, password);
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sqlStatement)) {

            queryResult = extractResponse(resultSet);
        }
        return queryResult;
    }

    /**
     * <ol>
     *     <li>Extract column definition.</li>
     *     <li>Extract rows as defined from the column definition.</li>
     *     <li>Collects possible errors from the rows.</li>
     * </ol>
     *
     * @param resultSet for data extraction
     * @return extracted response
     * @throws SQLException if a database access error occurs or this method is called on a closed result set
     */
    private QueryResponse extractResponse(ResultSet resultSet) throws SQLException {
        QueryResponse queryResult = new QueryResponse();

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
        return queryResult;
    }

    /**
     * Extract column definition of the metadata.
     *
     * @param resultSet for column definition
     * @return map of column definition <array index, column name>
     * @throws SQLException if a database access error occurs
     */
    private List<String> extractColumnDef(ResultSet resultSet) throws SQLException {
        List<String> columnDef = new ArrayList<>();
        ResultSetMetaData metaData = resultSet.getMetaData();

        for (int colIndex = 0; colIndex < metaData.getColumnCount(); colIndex++) {
            // meta data column count starts at 1
            columnDef.add(metaData.getColumnLabel(colIndex + 1));
        }
        return columnDef;
    }

    /**
     * Extract row of result set, with the definition (order) of the column definition.
     *
     * @param columnDef for row definition
     * @param resultSet to extract row from
     * @return row as array
     * @throws SQLException if the columnLabel is not valid; if a database access error occurs or this method is called on a closed result set
     */
    private Object[] extractRow(List<String> columnDef, ResultSet resultSet) throws SQLException {
        Object[] row = new Object[columnDef.size()];

        for (int i = 0; i < columnDef.size(); i++) {
            // meta data column count starts at 1
            row[i] = resultSet.getObject(i + 1);
        }
        return row;
    }
}
