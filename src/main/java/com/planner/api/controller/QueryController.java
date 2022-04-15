package com.planner.api.controller;

import com.planner.api.database.ConnectionBuilder;
import com.planner.api.database.JdbcExecutor;
import com.planner.api.files.FileReader;
import com.planner.api.model.DynamicRequest;
import com.planner.api.model.StaticRequest;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.sql.SQLException;

@Path("/query")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Query Controller", description = "Controller to manage the resources for query requests.")
public class QueryController {

    @Inject
    JdbcExecutor jdbcExecutor;

    @Inject
    ConnectionBuilder connectionBuilder;

    @Inject
    FileReader fileReader;

    @POST
    @Path("/dynamic")
    @Operation(summary = "Dynamic Query", description = "Query with dynamic and static statement.")
    public Response dynamicQuery(@RequestBody(description = "request model", required = true)
                                         DynamicRequest model) throws SQLException {
        if (model.isStatementInvalid())
            throw new NotAcceptableException("Invalid content of the statement: " + model.getStatement());

        return Response
                .status(Response.Status.OK)
                .entity(model.useDefaultDatabase() ?
                        jdbcExecutor.executeDynamicQuery(model.getStatement()) :
                        jdbcExecutor.executeDynamicQuery(model.getDbUrl(), model.getUsername(), model.getPassword(), model.getStatement()))
                .build();
    }

    @POST
    @Path("/static")
    @Operation(summary = "Static Query", description = "Query with prepared and static statement.")
    public Response staticQuery(@RequestBody(description = "request model", required = true)
                                        StaticRequest model) throws SQLException, IOException {
        if (model.isFileNameInvalid())
            throw new IOException("No file name specified!");

        return Response
                .status(Response.Status.OK)
                .entity(model.useDefaultDatabase() ?
                        jdbcExecutor.executeStaticFile(model.getFileName()) :
                        jdbcExecutor.executeStaticFile(model.getDbUrl(), model.getUsername(), model.getPassword(), model.getFileName()))
                .build();
    }

    @GET
    @Path("/sql-files")
    @Operation(summary = "Available Files", description = "Get all available sql files.")
    public Response getFiles() throws IOException {
        return Response
                .status(Response.Status.OK)
                .entity(fileReader.getStatementFiles())
                .build();
    }

    @GET
    @Path("/default-connection")
    @Operation(summary = "Default Connection", description = "Get default connection info.")
    public Response getDefaultConnectionInfo() {
        return Response
                .status(Response.Status.OK)
                .entity(connectionBuilder.getDefaultConnectionInfo())
                .build();
    }
}
