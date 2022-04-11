package com.planner.api.controller;

import com.google.common.net.HttpHeaders;
import com.planner.api.database.QueryDB;
import com.planner.api.model.QueryRequest;
import com.planner.api.model.QueryResponse;
import io.quarkus.test.junit.DisabledOnNativeImage;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Logger;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@QuarkusTest
class QueryControllerTest {

    private static final Logger LOGGER = Logger.getLogger(QueryControllerTest.class.getName());

    @Inject
    QueryDB queryDB;

    @BeforeEach
    @DisabledOnNativeImage
    void initDatabase() throws SQLException, IOException {
        LOGGER.info("\n\n========== TEST PREPARATION ==========\n");
        QueryResponse queryResponse = queryDB.executeStaticStatement("fp-create-tables-and-data.sql");
        assertNotNull(queryResponse);
        LOGGER.info("\n\n========== TEST CASE =================\n");
    }

    @Test
    @DisabledOnNativeImage
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
    @DisabledOnNativeImage
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
    @DisabledOnNativeImage
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
        assertTrue(response.contains("recipe_name"), "Expected contains 'recipe_name'.");
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
