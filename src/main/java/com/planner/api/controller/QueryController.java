package com.planner.api.controller;

import com.planner.api.database.QueryDB;
import com.planner.api.model.QueryRequest;
import com.planner.api.utility.FileReader;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

@Path("/query")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Query Controller", description = "Controller to manage the resources for query requests.")
public class QueryController {

    @Inject
    QueryDB queryDB;

    @Inject
    FileReader fileReader;

    @POST
    @Path("/dynamic")
    @Operation(summary = "Dynamic Query", description = "Query with dynamic and static statement.")
    public Response dynamicQuery(@RequestBody(description = "request model", required = true)
                                         QueryRequest model) throws SQLException {
        if (model.isStatementInvalid())
            throw new NotAcceptableException("Invalid content of the statement: " + model.getStatement());

        return Response
                .status(Response.Status.OK)
                .entity(model.useDefaultDatabase() ?
                        queryDB.executeStatementOnDefaultDB(model.getStatement()) :
                        queryDB.executeStatementOnCustomDB(model.getDbUrl(), model.getUsername(), model.getPassword(), model.getStatement()))
                .build();
    }

    @POST
    @Path("/static")
    @Operation(summary = "Static Query", description = "Query with prepared and static statement.")
    public Response staticQuery(@Parameter(description = "sql statement file name", required = true, example = "insert.sql")
                                @QueryParam("file") String file) throws SQLException, IOException {
        if (file == null || file.isEmpty())
            throw new IOException("No file name specified!");

        return Response
                .status(Response.Status.OK)
                .entity(queryDB.executeStaticStatement(file))
                .build();
    }

    @GET
    @Path("/static-files")
    @Operation(summary = "Available Files", description = "Get all available files.")
    public Response getFiles() throws IOException {
        List<String> files = fileReader.getStatementFiles();
        if (files.isEmpty())
            files = placeholderNativeFileList(); // TODO fix the selection for native files
        return Response
                .status(Response.Status.OK)
                .entity(files)
                .build();
    }

    private List<String> placeholderNativeFileList() {
        return Arrays.asList("db2-create-ancestor-table.sql",
                "db2-create-stud-tables.sql",
                "db2-select-ancestor.sql",
                "fp-create-tables-and-data.sql",
                "fp-insert-cart.sql",
                "fp-insert-user.sql",
                "fp-select-cart.sql",
                "fp-select-carts-from-user.sql",
                "fp-select-low-storage-products.sql",
                "fp-select-recipes.sql");
    }
}
