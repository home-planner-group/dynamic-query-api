package com.planner.api.database;

import com.planner.api.model.QueryResponse;
import com.planner.api.utility.ApiLogger;
import com.planner.api.utility.FileReader;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import java.io.IOException;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;

@RequestScoped
public class QueryDB {

    @Inject
    ApiLogger apiLogger;

    @Inject
    ConnectionBuilder connectionBuilder;

    @Inject
    FileReader fileReader;


    public QueryResponse executeStaticStatement(String fileName) throws IOException, SQLException {
        return executeStatement(fileReader.readStaticSqlStatement(fileName));
    }

    public QueryResponse executeStatement(String sqlStatement) throws SQLException {
        apiLogger.info("Create connection to default database.");
        apiLogger.info("Execute dynamic SQL statement: " + sqlStatement);

        QueryResponse queryResult;
        // establish database connection & execute sqlStatement
        try (Connection connection = connectionBuilder.createConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sqlStatement)) {

            queryResult = extractResponse(resultSet);
        }
        return queryResult;
    }

    public QueryResponse executeStatement(String dbUrl, String username, String password, String sqlStatement) throws SQLException {
        apiLogger.info("Create connection to '" + dbUrl + "' with user '" + username + "'");
        apiLogger.info("Execute dynamic SQL statement: " + sqlStatement);

        QueryResponse queryResult;
        // establish database connection & execute sqlStatement
        try (Connection connection = connectionBuilder.createConnection(dbUrl, username, password);
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sqlStatement)) {

            queryResult = extractResponse(resultSet);
        }
        return queryResult;
    }

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
}
