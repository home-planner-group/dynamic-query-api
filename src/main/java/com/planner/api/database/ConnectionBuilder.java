package com.planner.api.database;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import javax.annotation.PostConstruct;
import javax.inject.Singleton;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@Singleton
public class ConnectionBuilder {

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
     * @throws ClassNotFoundException if the driver class cannot be located
     */
    @PostConstruct
    private void postConstruct() throws ClassNotFoundException {
        Class.forName(MYSQL_DRIVER_NAME);
        DriverManager.setLoginTimeout(5);
    }


    /**
     * Creates connection to the default database from the application.properties.
     *
     * @return JDBC connection
     * @throws SQLException if a database access error occurs or the url is null
     */
    public Connection createConnection() throws SQLException {
        return DriverManager.getConnection(dbUrl, username, password);
    }

    /**
     * @param dbUrl    pattern: jdbc:mysql://host:port/database-name
     * @param username example: root
     * @param password example: password
     * @return JDBC connection
     * @throws SQLException if a database access error occurs or the url is null
     */
    public Connection createConnection(String dbUrl, String username, String password) throws SQLException {
        return DriverManager.getConnection(dbUrl, username, password);
    }
}
