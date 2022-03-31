package com.planner.api;

import org.eclipse.microprofile.openapi.annotations.OpenAPIDefinition;
import org.eclipse.microprofile.openapi.annotations.info.Contact;
import org.eclipse.microprofile.openapi.annotations.info.Info;

import javax.ws.rs.core.Application;

@OpenAPIDefinition(
        info = @Info(
                title = "Dynamic Query API",
                version = "0.0.1",
                contact = @Contact(name = "Felix Steinke", url = "https://github.com/felixsteinke")
        )
)
public class ApiApplication extends Application {
}
