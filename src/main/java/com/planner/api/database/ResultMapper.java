package com.planner.api.database;

import com.planner.api.model.QueryResponse;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ResultMapper {

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
    public static QueryResponse resultToResponse(ResultSet resultSet) throws SQLException {
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
    private static List<String> extractColumnDef(ResultSet resultSet) throws SQLException {
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
    private static Object[] extractRow(List<String> columnDef, ResultSet resultSet) throws SQLException {
        Object[] row = new Object[columnDef.size()];

        for (int i = 0; i < columnDef.size(); i++) {
            // meta data column count starts at 1
            row[i] = resultSet.getObject(i + 1);
        }
        return row;
    }
}
