package com.github.samuelz47.tests;

import com.github.samuelz47.core.BaseTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.*;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

public class BarrigaTest extends BaseTest {
    private String token;


    @BeforeEach
    public void login() {
        Map<String, String> login = new HashMap<String, String>();
        login.put("email", "alice.rodrigues.pereira65@outlook.com");
        login.put("senha", "admin");

        //Recebe o Token JWT
        token =
                given().
                        body(login).
                when().
                        post("/signin").
                then().
                        statusCode(200).
                        extract().path("token")
                ;
    }

    @Test
    public void naoDeveAcessarAPISemToken() {
        given().
                log().all().
        when().
                get("https://barrigarest.wcaquino.me/contas").
        then().
                log().all().
                statusCode(401)
        ;
    }

    @Test
    public void deveFazerUmPostComAutenticacaoJWT() {
        given().
                header("Authorization", "JWT " + token).
                body("{\"nome\": \"RestAssured2\"}").
        when().
                post("/contas").
        then().
                statusCode(201).
                body("nome", is("RestAssured2"))
        ;
    }

    @Test
    public void deveAlterarONome() {
        Integer id =
                given().
                        header("Authorization", "JWT " + token).
                when().
                        get("/contas").
                then().
                        statusCode(200).
                        extract().path("[1].id")
                ;

        given().
                header("Authorization", "JWT " + token).
                pathParam("id", id).
                body("{\"nome\": \"RestAssuredAtualizado\"}").
        when().
                put("/contas/{id}").
        then().
                statusCode(200).
                body("nome", is("RestAssuredAtualizado"))
        ;
    }

    @Test
    public void naoDeveAdicionarUmaContaComOMesmoNome() {
        given().
                header("Authorization", "JWT " + token).
                body("{\"nome\": \"RestAssured2\"}").
        when().
                post("/contas").
        then().
                statusCode(400).
                body("error", is("Já existe uma conta com esse nome!"))
        ;
    }
}
