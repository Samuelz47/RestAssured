package com.github.samuelz47.rest;

import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import java.util.*;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class AuthTest {

    @Test
    public void deveAcessarLogineSenha() {
        given().
                log().all().
        when().
                get("https://admin:senha@restapi.wcaquino.me/basicauth"). //https://{login}:{senha}@url.com
        then().
                log().all().
                body("status", is("logado")).
                statusCode(200)
        ;
    }

    @Test
    public void deveAcessarLogineSenhaDeOutraForma() {
        given().
                log().all().
                auth().basic("admin", "senha").  //login e senha
        when().
                get("https://restapi.wcaquino.me/basicauth").
        then().
                log().all().
                body("status", is("logado")).
                statusCode(200)
        ;
    }

    @Test
    public void deveAcessarTokenJWT() {
        Map<String, String> login = new HashMap<String, String>();
        login.put("email", "alice.rodrigues.pereira65@outlook.com");
        login.put("senha", "admin");

        //Recebe o Token JWT
        String token =
            given().
                    log().all().
                    body(login).
                    contentType(ContentType.JSON).
            when().
                    post("https://barrigarest.wcaquino.me/signin").
            then().
                    log().all().
                    statusCode(200).
                    extract().path("token")
        ;

        //Obter dados com o Token
        given().
                log().all().
                header("Authorization", "JWT " + token).
        when().
                get("https://barrigarest.wcaquino.me/contas").
        then().
                log().all().
                statusCode(200).
                body("nome", hasItem("alice"))
        ;
    }
}
