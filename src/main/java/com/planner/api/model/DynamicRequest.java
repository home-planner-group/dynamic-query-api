package com.planner.api.model;

import com.planner.api.database.DynamicBlacklist;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

@Setter
@Getter
@NoArgsConstructor
@Schema(name = "Dynamic Query Request", description = "Model for dynamic query request.")
public class DynamicRequest {

    @Schema(description = "Database URL -> null = default db", example = "jdbc:mysql://localhost:3306/fresh_planner_db_dev")
    private String dbUrl;

    @Schema(description = "Database Username -> null = default db", example = "root")
    private String username;

    @Schema(description = "Database User Password -> null = default db", example = "password")
    private String password;

    @Schema(description = "SQL Statement", example = "SELECT * FROM table", required = true)
    private String statement;

    @Schema(hidden = true)
    public boolean useDefaultDatabase() {
        return dbUrl == null || dbUrl.isEmpty() || username == null || password == null;
    }

    @Schema(hidden = true)
    public boolean isStatementInvalid() {
        if (statement == null || statement.isEmpty()) return true;

        String normedStatement = statement.toLowerCase();
        for (String content : DynamicBlacklist.BLACKLIST) {
            if (normedStatement.contains(content)) return true;
        }
        return false;
    }
}
