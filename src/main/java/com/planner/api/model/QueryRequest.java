package com.planner.api.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

@Setter
@Getter
@NoArgsConstructor
@Schema(name = "Query Request", description = "Model query request.")
public class QueryRequest {

    @Schema(description = "Database URL", example = "jdbc:mysql://localhost:3306/fresh_planner_db_dev")
    private String dbUrl;

    @Schema(description = "Database Username", example = "root")
    private String username;

    @Schema(description = "Database User Password", example = "password")
    private String password;

    @Schema(description = "SQL Statement", example = "SELECT * FROM table", required = true)
    private String statement;

    @Schema(hidden = true)
    public boolean useDefaultDatabase() {
        return dbUrl == null || dbUrl.isEmpty() || username == null || password == null;
    }

    @Schema(hidden = true)
    public boolean isStatementInvalid() {
        String normedStatement = statement.toLowerCase();
        for (String content : StatementBlacklist.BLACKLIST) {
            if (normedStatement.contains(content)) return true;
        }
        return false;
    }
}
