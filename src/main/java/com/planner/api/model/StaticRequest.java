package com.planner.api.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

@Setter
@Getter
@NoArgsConstructor
@Schema(name = "Static Query Request", description = "Model for static query request.")
public class StaticRequest {

    @Schema(description = "Database URL -> null = default db", example = "jdbc:mysql://localhost:3306/fresh_planner_db_dev")
    private String dbUrl;

    @Schema(description = "Database Username -> null = default db", example = "root")
    private String username;

    @Schema(description = "Database User Password -> null = default db", example = "password")
    private String password;

    @Schema(description = "Static File Name", example = "select-entity.sql", required = true)
    private String fileName;

    @Schema(hidden = true)
    public boolean useDefaultDatabase() {
        return dbUrl == null || dbUrl.isEmpty() || username == null || password == null;
    }

    @Schema(hidden = true)
    public boolean isFileNameInvalid() {
        return fileName == null || fileName.isEmpty();
    }
}
