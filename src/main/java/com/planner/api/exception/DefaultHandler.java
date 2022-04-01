package com.planner.api.exception;

import com.planner.api.model.ExceptionResponse;

import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.util.logging.Logger;

@Provider
public class DefaultHandler implements ExceptionMapper<Exception> {

    private final Logger LOGGER = Logger.getLogger(DefaultHandler.class.getName());

    @Override
    @Produces(MediaType.APPLICATION_JSON)
    public Response toResponse(Exception exception) {
        LOGGER.warning(exception.getMessage());
        exception.printStackTrace();
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(new ExceptionResponse(exception)).build();
    }
}
