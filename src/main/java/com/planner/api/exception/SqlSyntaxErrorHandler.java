package com.planner.api.exception;

import com.planner.api.model.ExceptionResponse;

import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import java.sql.SQLSyntaxErrorException;
import java.util.logging.Logger;

public class SqlSyntaxErrorHandler implements ExceptionMapper<SQLSyntaxErrorException> {

    private final Logger LOGGER = Logger.getLogger(SqlSyntaxErrorHandler.class.getName());

    @Override
    @Produces(MediaType.APPLICATION_JSON)
    public Response toResponse(SQLSyntaxErrorException exception) {
        LOGGER.warning(exception.getMessage());
        return Response.status(Response.Status.BAD_REQUEST).entity(new ExceptionResponse(exception)).build();
    }
}
