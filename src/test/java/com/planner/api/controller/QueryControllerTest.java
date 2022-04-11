package com.planner.api.controller;

import com.google.common.net.HttpHeaders;
import com.planner.api.model.QueryRequest;
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
    void dynamicQuery() {
        String requestPath = "/query/dynamic";
        QueryRequest body = new QueryRequest();
        body.setStatement("SELECT * from products");
        String response = given()
                .when()
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                .body(body)
                .post(requestPath)
                .then()
                .statusCode(200)
                .extract()
                .body()
                .asString();

        LOGGER.info("[" + requestPath + "] Response: '" + response + "'");
        assertTrue(response.contains("name"), "Expected contains 'name'.");
    }

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
    void dynamicQueryBadRequest() {
        String requestPath = "/query/dynamic";
        QueryRequest body = new QueryRequest();
        body.setStatement("SELECT no_column FROM products");
        given()
                .when()
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                .body(body)
                .post(requestPath)
                .then()
                .statusCode(400);
    }

    @Test
    void staticQuery() {
        String requestPath = "/query/static?file=fp-select-recipes.sql";
        String response = given()
                .when()
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                .post(requestPath)
                .then()
                .statusCode(200)
                .extract()
                .body()
                .asString();

        LOGGER.info("[" + requestPath + "] Response: '" + response + "'");
        assertTrue(response.contains("Pasta"), "Expected contains 'Pasta'.");
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
