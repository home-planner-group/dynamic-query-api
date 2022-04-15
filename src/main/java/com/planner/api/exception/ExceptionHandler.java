package com.planner.api.exception;

import com.planner.api.model.ExceptionResponse;

import javax.ws.rs.NotAcceptableException;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Logger;

import static javax.ws.rs.core.Response.Status.*;

@Provider
public class ExceptionHandler implements ExceptionMapper<Exception> {

    private final Logger LOGGER = Logger.getLogger(ExceptionHandler.class.getName());

    @Override
    @Produces(MediaType.APPLICATION_JSON)
    public Response toResponse(Exception exception) {
        LOGGER.warning(exception.getMessage());
        if (exception instanceof IOException) {
            return buildResponse(NOT_FOUND, exception);
        } else if (exception instanceof NotAcceptableException) {
            return buildResponse(NOT_ACCEPTABLE, exception);
        } else if (exception instanceof SQLException) {
            return buildResponse(BAD_REQUEST, exception);
        } else {
            // exception.printStackTrace();
            return buildResponse(INTERNAL_SERVER_ERROR, exception);
        }
    }

    /**
     * @param status    http status
     * @param exception exception for body
     * @return http response
     */
    private Response buildResponse(Response.Status status, Exception exception) {
        return Response.status(status).entity(new ExceptionResponse(exception)).build();
    }
}
