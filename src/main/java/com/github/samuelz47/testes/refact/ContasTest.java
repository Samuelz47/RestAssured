package com.github.samuelz47.testes.refact;

import com.github.samuelz47.core.BaseTest;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;


import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;

public class ContasTest extends BaseTest {
    @BeforeAll
    public static void setup() {
        Map<String, String> login = new HashMap<String, String>();
        login.put("email", "alice.rodrigues.pereira65@outlook.com");
        login.put("senha", "admin");

        //Recebe o Token JWT
        String token =
                given().
                        body(login).
                when().
                        post("/signin").
                then().
                        statusCode(200).
                        extract().path("token")
        ;

        RestAssured.requestSpecification.header("Authorization", "JWT " + token);
        RestAssured.get("/reset").then().statusCode(200);
    }

    @Test
    public void deveFazerUmPostComAutenticacaoJWT() {
        given().
                body("{\"nome\": \"Conta Inserida\"}").
        when().
                post("/contas").
        then().
                statusCode(201)
        ;
    }

    @Test
    public void deveAlterarONome() {
        Integer CONTA_ID = getIdContaPeloNome("Conta para alterar");

        given().
                pathParam("id", CONTA_ID).
                body("{\"nome\": \"RestAssuredAtualizado\"}").
        when().
                put("/contas/{id}").
        then().
                statusCode(200).
                body("nome", is("RestAssuredAtualizado"))
        ;
    }

    public Integer getIdContaPeloNome(String nome) {
        return RestAssured.get("/contas?nome="+nome).then().extract().path("id[0]");
    }
}
