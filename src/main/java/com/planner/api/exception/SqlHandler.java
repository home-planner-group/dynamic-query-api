package com.planner.api.exception;

import com.planner.api.model.ExceptionResponse;

import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import java.sql.SQLException;
import java.util.logging.Logger;

public class SqlHandler implements ExceptionMapper<SQLException> {

    private final Logger LOGGER = Logger.getLogger(SqlHandler.class.getName());

    @Override
    @Produces(MediaType.APPLICATION_JSON)
    public Response toResponse(SQLException exception) {
        LOGGER.warning(exception.getMessage());
        return Response.status(Response.Status.BAD_REQUEST).entity(new ExceptionResponse(exception)).build();
    }
}
