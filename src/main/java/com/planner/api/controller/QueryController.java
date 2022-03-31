package com.planner.api.controller;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/query")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Query Controller", description = "Controller to manage the resources for query requests.")
public class QueryController {

    @POST
    @Path("/demo")
    @Operation(summary = "demo summary", description = "demo description")
    @APIResponse(responseCode = "201", description = "demo description")
    public Response demo(
            @RequestBody(description = "demo description", required = true)
            @Valid String model
    ) {
        return Response.status(Response.Status.CREATED).entity(model).build();
    }
}
