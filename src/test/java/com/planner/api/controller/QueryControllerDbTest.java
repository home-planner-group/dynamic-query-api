package com.planner.api.controller;

import com.google.common.net.HttpHeaders;
import com.planner.api.database.JdbcExecutor;
import com.planner.api.model.DynamicRequest;
import com.planner.api.model.QueryResponse;
import com.planner.api.model.StaticRequest;
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
@DisabledOnNativeImage
public class QueryControllerDbTest {


    private static final Logger LOGGER = Logger.getLogger(QueryControllerDbTest.class.getName());

    @Inject
    JdbcExecutor jdbcExecutor;

    @BeforeEach
    void initDatabase() throws SQLException, IOException {
        LOGGER.info("\n\n========== DB TEST PREPARATION ==========\n\n");
        QueryResponse queryResponse = jdbcExecutor.executeStaticFile("fp-create-tables-and-data.sql");
        assertNotNull(queryResponse);
        LOGGER.info("\n\n========== DB TEST CASE =================\n\n");
    }

    @Test
    void dynamicQuery() {
        String requestPath = "/query/dynamic";
        DynamicRequest body = new DynamicRequest();
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
    void dynamicQueryCustomDB() {
        String requestPath = "/query/dynamic";
        DynamicRequest body = new DynamicRequest();
        body.setStatement("SELECT * from products");
        body.setDbUrl("jdbc:mysql://localhost:3306/fresh_planner_db_dev");
        body.setUsername("root");
        body.setPassword("password");
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
    @DisabledOnNativeImage
    void dynamicQueryBadRequest() {
        String requestPath = "/query/dynamic";
        DynamicRequest body = new DynamicRequest();
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
        String requestPath = "/query/static";
        StaticRequest body = new StaticRequest();
        body.setFileName("fp-select-recipes.sql");
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
        assertTrue(response.contains("recipe_name"), "Expected contains 'recipe_name'.");
    }


    @Test
    void staticQueryCustomDB() {
        String requestPath = "/query/static";
        StaticRequest body = new StaticRequest();
        body.setFileName("fp-select-recipes.sql");
        body.setDbUrl("jdbc:mysql://localhost:3306/fresh_planner_db_dev");
        body.setUsername("root");
        body.setPassword("password");
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
        assertTrue(response.contains("recipe_name"), "Expected contains 'recipe_name'.");
    }
}
