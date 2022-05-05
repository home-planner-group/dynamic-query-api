package com.planner.api.model;

import lombok.Getter;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

@Getter
@Schema(name = "Connection Info", description = "Model for default database connection.")
public class ConnectionInfo {

    @Schema(description = "database driver", example = "jdbc:mysql:")
    private final String driver;

    @Schema(description = "database host", example = "localhost")
    private final String host;

    @Schema(description = "database port", example = "3306")
    private final String port;

    @Schema(description = "database name", example = "fresh_planner_db")
    private final String database;

    /**
     * @param dbUrl pattern: jdbc:mysql://host:port/database-name
     */
    public ConnectionInfo(String dbUrl) {
        String[] split = dbUrl.split("//");
        // [0]=jdbc:mysql:      [1]=host:port/database-name
        this.driver = split[0];
        split = split[1].split("/");
        // [0]=host:port      [1]=database-name
        this.database = split[1];
        split = split[0].split(":");
        // [0]=host      [1]=port
        this.host = split[0];
        this.port = split[1];
    }
}
