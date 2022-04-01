package com.planner.api.controller;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;

@QuarkusTest
class QueryControllerTest {

    @Test
    void dynamicQuery() {
    }

    @Test
    void staticQuery() {
    }

    @Test
    void getFiles() {
        given()
                .when().get("/query/static-files")
                .then()
                .statusCode(200);
        //.body(is());
    }
}
