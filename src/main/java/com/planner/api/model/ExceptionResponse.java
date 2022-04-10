package com.planner.api.model;

import lombok.Getter;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.text.SimpleDateFormat;
import java.util.Date;

@Getter
@Schema(name = "Error", description = "Model for application exception.")
public class ExceptionResponse {

    @Schema(description = "exception name", example = "SqlException")
    private final String exception;

    @Schema(description = "exception message", example = "Something went wrong.")
    private final String message;

    @Schema(description = "timestamp of the exception", example = "2020-03-24 15:45:55.155")
    private final String timestamp;

    public ExceptionResponse(Exception exception) {
        this.exception = exception.getClass().getSimpleName();
        this.message = exception.getMessage();
        this.timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new Date());
    }
}
