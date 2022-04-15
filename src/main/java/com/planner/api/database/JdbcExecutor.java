package com.planner.api.database;

import com.planner.api.files.FileReader;
import com.planner.api.model.QueryResponse;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Logger;

@RequestScoped
public class JdbcExecutor {

    private final Logger LOGGER = Logger.getLogger(JdbcExecutor.class.getName());

    @Inject
    ConnectionBuilder connectionBuilder;

    @Inject
    FileReader fileReader;

    /**
     * <ol>
     *     <li>Connect to default database.</li>
     *     <li>Execute dynamic select statement.</li>
     * </ol>
     *
     * @param sqlSelectStatement SELECT * FROM table
     * @return extracted response
     * @throws SQLException if a database access error occurs or this method is called on a closed result set
     */
    public QueryResponse executeDynamicQuery(String sqlSelectStatement) throws SQLException {
        try (Connection connection = connectionBuilder.createConnection();
             Statement statement = connection.createStatement()) {
            return executeSelectStatement(statement, sqlSelectStatement);
        }
    }

    /**
     * <ol>
     *     <li>Connect to custom database.</li>
     *     <li>Execute dynamic select statement.</li>
     * </ol>
     *
     * @param dbUrl              pattern: jdbc:mysql://host:port/database-name
     * @param username           example: root
     * @param password           example: password
     * @param sqlSelectStatement example: SELECT * FROM table
     * @return extracted response
     * @throws SQLException if a database access error occurs or this method is called on a closed result set
     */
    public QueryResponse executeDynamicQuery(String dbUrl, String username, String password, String sqlSelectStatement) throws SQLException {
        try (Connection connection = connectionBuilder.createConnection(dbUrl, username, password);
             Statement statement = connection.createStatement()) {
            return executeSelectStatement(statement, sqlSelectStatement);
        }
    }

    /**
     * <ol>
     *     <li>Reads statement from file resource.</li>
     *     <li>Connect to default database.</li>
     *     <li>Execute static sql statement.</li>
     * </ol>
     *
     * @param fileName in the directory: resources/sql-statements/fileName
     * @return extracted response
     * @throws SQLException if a database access error occurs or this method is called on a closed result set
     * @throws IOException  if file not found
     */
    public QueryResponse executeStaticFile(String fileName) throws SQLException, IOException {
        String fileSqlStatement = fileReader.readStaticSqlStatement(fileName);
        try (Connection connection = connectionBuilder.createConnection();
             Statement statement = connection.createStatement()) {
            return executeFileStatement(statement, fileSqlStatement);
        }
    }

    /**
     * <ol>
     *     <li>Reads statement from file resource.</li>
     *     <li>Connect to default database.</li>
     *     <li>Execute static sql statement.</li>
     * </ol>
     *
     * @param dbUrl    pattern: jdbc:mysql://host:port/database-name
     * @param username example: root
     * @param password example: password
     * @param fileName in the directory: resources/sql-statements/fileName
     * @return extracted response
     * @throws SQLException if a database access error occurs or this method is called on a closed result set
     * @throws IOException  if file not found
     */
    public QueryResponse executeStaticFile(String dbUrl, String username, String password, String fileName) throws SQLException, IOException {
        String fileSqlStatement = fileReader.readStaticSqlStatement(fileName);
        try (Connection connection = connectionBuilder.createConnection(dbUrl, username, password);
             Statement statement = connection.createStatement()) {
            return executeFileStatement(statement, fileSqlStatement);
        }
    }

    /**
     * <ul>
     *     <li>If statements start with SELECT: Executes single SELECT statement.</li>
     *     <li>Else: Executes all found statements and collects instance count in the response.</li>
     * </ul>
     *
     * @param connectionStatement statement from jdbc connection
     * @param fileSqlStatement    statement from file resource
     * @return extracted response
     * @throws SQLException if a database access error occurs or this method is called on a closed result set
     */
    private QueryResponse executeFileStatement(Statement connectionStatement, String fileSqlStatement) throws SQLException {
        QueryResponse response;
        if (fileSqlStatement.toLowerCase().startsWith("select")) {
            response = executeSelectStatement(connectionStatement, fileSqlStatement);
        } else {
            response = new QueryResponse();
            for (String sqlStatement : fileSqlStatement.split(";")) {
                response.instanceCount += executeModificationStatement(connectionStatement, sqlStatement);
            }
        }
        return response;
    }

    /**
     * Executes single SELECT statement.
     *
     * @param connectionStatement statement from jdbc connection
     * @param sqlQuery            SELECT sql statement
     * @return extracted response
     * @throws SQLException if a database access error occurs or this method is called on a closed result set
     */
    private QueryResponse executeSelectStatement(Statement connectionStatement, String sqlQuery) throws SQLException {
        LOGGER.info("Execute SQL Query Statement:\n" + sqlQuery);
        try (ResultSet resultSet = connectionStatement.executeQuery(sqlQuery)) {
            return ResultMapper.resultToResponse(resultSet);
        }
    }

    /**
     * Executes single modification statement.
     *
     * @param connectionStatement statement from jdbc connection
     * @param sqlStatement        CREATE / INSERT / UPDATE / DELETE sql statement
     * @return instance count of result
     * @throws SQLException if a database access error occurs or this method is called on a closed result set
     */
    private int executeModificationStatement(Statement connectionStatement, String sqlStatement) throws SQLException {
        LOGGER.info("Execute SQL Statement:\n" + sqlStatement);
        int foundInstances = 0;
        boolean resultPresent = connectionStatement.execute(sqlStatement);
        if (resultPresent) {
            try (ResultSet resultSet = connectionStatement.getResultSet()) {
                while (resultSet.next()) {
                    foundInstances++;
                }
            }
        }
        return foundInstances;
    }
}
