package com.planner.api.controller;

import com.google.common.net.HttpHeaders;
import com.planner.api.model.QueryRequest;
import io.quarkus.test.junit.NativeImageTest;
import org.junit.jupiter.api.Test;

import javax.ws.rs.core.MediaType;
import java.util.logging.Logger;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Guide: https://quarkus.io/guides/building-native-image#testing-the-native-executable
 */
@NativeImageTest
public class NativeQueryControllerIT {

    private static final Logger LOGGER = Logger.getLogger(NativeQueryControllerIT.class.getName());

    @Test
    void dynamicQueryNotAcceptable() {
        String requestPath = "/query/dynamic";
        QueryRequest body = new QueryRequest();
        body.setStatement("INSERT INTO products");
        given()
                .when()
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                .body(body)
                .post(requestPath)
                .then()
                .statusCode(406);
    }

    @Test
    void staticQueryNotFound() {
        String requestPath = "/query/static";
        given()
                .when()
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                .post(requestPath)
                .then()
                .statusCode(404);
    }

    @Test
    void getFiles() {
        String requestPath = "/query/static-files";
        String response = given()
                .when()
                .get(requestPath)
                .then()
                .statusCode(200)
                .extract()
                .body()
                .asString();

        LOGGER.info("[" + requestPath + "] Response: '" + response + "'");
        assertTrue(response.contains("fp-select"), "Expected contains 'fp-select'.");
    }
}
