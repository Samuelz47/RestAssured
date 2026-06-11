package com.github.samuelz47.rest;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;

public class VerbosTest {
    @Test
    public void devoSalvarUmUsuario() {
        // POST

        given().
                log().all().
                contentType("application/json").        //Objeto deve ser tratado como JSON
                body("{\"name\": \"Gilvan\", \"age\": 47}").
        when().
                post("https://restapi.wcaquino.me/users").
        then().
                log().all().
                statusCode(201).
                body("id", is(notNullValue())).
                body("name", is("Gilvan")).
                body("age", is(47))
        ;
    }

    @Test
    public void naoDeveSalvarUsuarioSemNome() {
        given().
                log().all().
                contentType("application/json").        //Objeto deve ser tratado como JSON
                body("{ \"age\": 47}").
        when().
                post("https://restapi.wcaquino.me/users").
        then().
                log().all().
                statusCode(400).
                body("id", is(nullValue())).
                body("error", is("Name é um atributo obrigatório"))
        ;
    }

    @Test
    public void deveAtualizarUmUsuario() {
        given().
                log().all().
                contentType("application/json").
                body("{\"name\": \"Clodoaldo\", \"age\": 47}").
        when().
                put("https://restapi.wcaquino.me/users/1").
        then().
                log().all().
                statusCode(200).
                body("name", is("Clodoaldo")).
                body("age", is(47))
        ;
    }

    @Test
    public void devoParametrizarUrl() {
        given().
                log().all().
                contentType("application/json").
                body("{\"name\": \"Clodoaldo\", \"age\": 47}").
                pathParam("entidade", "users").
                pathParam("userId", "1").
        when().
                put("https://restapi.wcaquino.me/{entidade}/{userId}").
        then().
                log().all().
                statusCode(200).
                body("name", is("Clodoaldo")).
                body("age", is(47))
        ;
    }

    @Test
    public void deveDeletarUmUsuario() {
        given().
                log().all().
        when().
                delete("https://restapi.wcaquino.me/users/1").
        then().
                log().all().
                statusCode(204)
        ;
    }

    @Test
    public void naoDeveDeletarUmUsuarioInexistente() {
        given().
                log().all().
        when().
                delete("https://restapi.wcaquino.me/users/184861").
        then().
                log().all().
                statusCode(400).
                body("error", is("Registro inexistente"))
        ;
    }

    @Test
    public void devoSalvarUmUsuarioUsandoMap() {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("name", "Usuario via map");
        params.put("age", 25);

        given().
                log().all().
                contentType("application/json").        //Objeto deve ser tratado como JSON
                body(params).
        when().
                post("https://restapi.wcaquino.me/users").
        then().
                log().all().
                statusCode(201).
                body("id", is(notNullValue())).
                body("name", is("Usuario via map")).
                body("age", is(25))
        ;
    }

    @Test
    public void devoSalvarUmUsuarioUsandoObjeto() {
        User user = new User(25, "Usuario via objeto");

        User usuarioInserido = given().
                log().all().
                contentType("application/json").        //Objeto deve ser tratado como JSON
                body(user).
                when().
                post("https://restapi.wcaquino.me/users").
                then().
                log().all().
                statusCode(201).
                extract().body().as(User.class)     //Usando extract para receber os parametros no novo usuario criado
        ;

        System.out.println(usuarioInserido);
        assertThat(usuarioInserido.getId(), notNullValue());
        assertEquals("Usuario via objeto", usuarioInserido.getName());
        assertThat(usuarioInserido.getAge(), is(25));
    }
}
