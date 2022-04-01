package com.planner.api.exception;

import com.planner.api.model.ExceptionResponse;

import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import java.io.IOException;
import java.util.logging.Logger;

public class IOHandler implements ExceptionMapper<IOException> {

    private final Logger LOGGER = Logger.getLogger(IOHandler.class.getName());

    @Override
    @Produces(MediaType.APPLICATION_JSON)
    public Response toResponse(IOException exception) {
        LOGGER.warning(exception.getMessage());
        return Response.status(Response.Status.NOT_FOUND).entity(new ExceptionResponse(exception)).build();
    }
}
