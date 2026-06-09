package com.github.samuelz47.rest;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class UserJsonTest {

    public static RequestSpecification requestSpecification;
    public static ResponseSpecification responseSpecification;

    @BeforeAll
    public static void setup() {
        RestAssured.baseURI = "https://restapi.wcaquino.me";

        RequestSpecBuilder reqBuilder = new RequestSpecBuilder();
        reqBuilder.log(LogDetail.ALL);
        requestSpecification = reqBuilder.build();

        ResponseSpecBuilder resBuilder = new ResponseSpecBuilder();
        resBuilder.expectStatusCode(200);
        responseSpecification = resBuilder.build();
        // Variaveis globais servem para todos os testes
        RestAssured.requestSpecification = requestSpecification;
        RestAssured.responseSpecification = responseSpecification;
    }

    @Test
    public void deveVerificarJsonNoPrimeiroNivel() {

        given().
        when().
                get("/users/1").
        then().
                //statusCode(200).
                body("id", is(1)).
                body("name", containsString("Silva")).
                body("age", greaterThan(20));

    }

    @Test
    public void deveVerificarJsonNoSegundoNivel()
    {
        given().
        when().
                get("/users/2").
        then().
                //statusCode(200).
                body("id", is(2)).
                body("name", containsString("Maria")).
                body("endereco.rua", is("Rua dos bobos"));
    }

    @Test
    public void deveVerificarJsonEmLista()
    {
        given().
        when().
                get("/users/3").
        then().
                //statusCode(200).
                body("id", is(3)).
                body("name", containsString("Ana")).
                body("filhos", hasSize(2)).
                body("filhos[0].name", is("Zezinho")).
                body("filhos[1].name", is("Luizinho"));
    }

    @Test
    public void deveRetornarErro()
    {
        RestAssured.responseSpecification = null;    //desabilita a resposta global para esse teste, pois o teste espera um 404 e a resposta global é 200

        given().
        when().
                get("/users/4").
        then().
                statusCode(404).
                body("error", is("Usuário inexistente"));

    }

    @Test
    public void deveFazerVerificacoesAvancadas()
    {
        given().
        when().
                get("/users").
        then().
                //statusCode(200).
                body("age.findAll{it <= 25}.size()", is(2)).    //busca todas as idades menores ou iguais a 25 e verifica o tamanho da lista retornada, ou seja, quantas pessoas tem idade menor ou igual a 25 anos
                body("findAll{it.age <= 25 && it.age > 20}.name", hasItem("Maria Joaquina")).    //busca todas as pessoas com idade menor ou igual a 25 e maior que 20 e verifica se o nome da pessoa é Maria Joaquina
                body("findAll{it.filhos.any{f -> f.name.endsWith('inho')}}.filhos.name.flatten()", hasItems("Zezinho", "Luizinho"));    //busca todas as pessoas que tem filhos com nome terminando com "inho" e verifica se o nome do filho é Luizinho ou Zezinho);
    }
}
