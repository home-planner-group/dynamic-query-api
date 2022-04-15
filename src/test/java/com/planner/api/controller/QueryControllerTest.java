package com.planner.api.controller;

import com.google.common.net.HttpHeaders;
import com.planner.api.model.DynamicRequest;
import com.planner.api.model.StaticRequest;
import io.quarkus.test.junit.DisabledOnNativeImage;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import javax.ws.rs.core.MediaType;
import java.util.logging.Logger;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertTrue;

@QuarkusTest
class QueryControllerTest {

    private static final Logger LOGGER = Logger.getLogger(QueryControllerTest.class.getName());

    @Test
    @DisabledOnNativeImage
    void dynamicQueryNotAcceptable() {
        String requestPath = "/query/dynamic";
        DynamicRequest body = new DynamicRequest();
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
    @DisabledOnNativeImage
    void staticQueryNotFound() {
        String requestPath = "/query/static";
        StaticRequest body = new StaticRequest();
        given()
                .when()
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                .body(body)
                .post(requestPath)
                .then()
                .statusCode(404);
    }

    @Test
    void getFiles() {
        String requestPath = "/query/sql-files";
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

    @Test
    void getDefaultConnectionInfo() {
        String requestPath = "/query/default-connection";
        String response = given()
                .when()
                .get(requestPath)
                .then()
                .statusCode(200)
                .extract()
                .body()
                .asString();

        LOGGER.info("[" + requestPath + "] Response: '" + response + "'");
        assertTrue(response.contains("jdbc"), "Expected contains 'jdbc'.");
    }
}
