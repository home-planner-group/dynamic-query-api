package com.planner.api.utility;

import javax.enterprise.context.ApplicationScoped;
import java.util.logging.Level;
import java.util.logging.Logger;

@ApplicationScoped
public class ApiLogger {

    private static final Logger LOGGER = Logger.getLogger(ApiLogger.class.getName());

    public void warning(String message) {
        LOGGER.log(Level.WARNING, message + getExecutingMethod());
    }

    public void info(String message) {
        LOGGER.log(Level.INFO, message + getExecutingMethod());
    }

    private String getExecutingMethod() {
        StackTraceElement stacktraceElement = Thread.currentThread().getStackTrace()[3];
        return " (" + stacktraceElement.getFileName() + "::" + stacktraceElement.getMethodName() + ")";
    }
}
