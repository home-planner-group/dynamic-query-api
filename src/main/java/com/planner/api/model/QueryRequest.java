package com.planner.api.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

@Getter
@Setter
@NoArgsConstructor
@Schema(name = "Query Request", description = "Model query request.")
public class QueryRequest {

    @Schema(description = "SQL Statement", example = "SELECT * FROM table")
    private String statement;
}
