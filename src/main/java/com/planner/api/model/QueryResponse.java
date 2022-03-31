package com.planner.api.model;

import lombok.NoArgsConstructor;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@NoArgsConstructor
@Schema(name = "Query Response", description = "Model query response.")
public class QueryResponse {

    @Schema(description = "Count of the queried result instances")
    public int instanceCount = 0;

    @Schema(description = "Column definition with index & column name")
    public Map<Integer, String> columnDef = new HashMap<>();

    @Schema(description = "Row with objects. column = row[columnDef.key]")
    public List<Object[]> rows = new ArrayList<>();

    @Schema(description = "Count of occurred errors while reading query result")
    public int errorCount = 0;

    @Schema(description = "Messages from occurred errors (ordered)")
    public List<String> errorMessages = new ArrayList<>();

}
