package com.github.samuelz47.rest;

import io.restassured.RestAssured;
import io.restassured.http.Method;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static io.restassured.RestAssured.*;

public class OlaMundoTest {

    @Test
    public void testOlaMundoManual() {
        Response response = RestAssured.request(Method.GET, "https://restapi.wcaquino.me/ola");
        assertTrue(response.getBody().asString().equals("Ola Mundo!"));
        assertEquals(200,response.getStatusCode());
    }

    @Test
    public void OutrosCenarios() {
        get("https://restapi.wcaquino.me/ola").then().statusCode(200);
    }

    @Test
    public void FormatoBDD() {
        given()
        .when()
                .get("https://restapi.wcaquino.me/ola")
        .then()
                .statusCode(200)
                .assertThat();
    }

    @Test
    public void Hamcrest() {
        given().
        when().
                get("https://restapi.wcaquino.me/ola").
        then().
                statusCode(200).
                body(is("Ola Mundo!"));
    }
}
