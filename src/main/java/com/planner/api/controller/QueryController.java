package com.planner.api.controller;

import com.planner.api.database.QueryDB;
import com.planner.api.model.QueryRequest;
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

@Path("/query")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Query Controller", description = "Controller to manage the resources for query requests.")
public class QueryController {

    @Inject
    QueryDB queryDB;

    @POST
    @Path("/statement")
    @Operation(summary = "Dynamic Query", description = "Query with dynamic and static statement.")
    public Response query(@RequestBody(description = "request model", required = true)
                                  QueryRequest model) throws SQLException {

        return Response.status(Response.Status.OK).entity(
                queryDB.executeStatement(model.getStatement())).build();
    }

    @POST
    @Path("/static-statement")
    @Operation(summary = "Static Query", description = "Query with prepared and static statement.")
    public Response staticQuery(@Parameter(description = "sql statement file name", required = true)
                                @QueryParam("statement") String statement) throws SQLException, IOException {

        return Response.status(Response.Status.OK).entity(
                queryDB.executeStaticStatement(statement)).build();
    }
}
