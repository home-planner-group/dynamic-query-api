package com.planner.api.exception;

import com.planner.api.model.ExceptionResponse;

import javax.ws.rs.NotAcceptableException;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import java.util.logging.Logger;

public class NotAcceptableHandler implements ExceptionMapper<NotAcceptableException> {

    private final Logger LOGGER = Logger.getLogger(NotAcceptableHandler.class.getName());

    @Override
    @Produces(MediaType.APPLICATION_JSON)
    public Response toResponse(NotAcceptableException exception) {
        LOGGER.warning(exception.getMessage());
        return Response.status(Response.Status.NOT_ACCEPTABLE).entity(new ExceptionResponse(exception)).build();
    }
}
