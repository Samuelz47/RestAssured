package com.github.samuelz47.rest;

import org.junit.jupiter.api.Test;


import java.io.*;

import static io.restassured.RestAssured.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class FileTest {
    @Test
    public void deveObrigarEnvioArquivo() {
        given().
                log().all().
        when().
                post("https://restapi.wcaquino.me/upload").
        then().
                log().all().
                statusCode(404).
                body("error", is("Arquivo não enviado"))
        ;
    }

    @Test
    public void deveFazerUploadDoArquivo() {
        given().
                log().all().
                multiPart("arquivo", new File("src/main/resources/users.pdf")).
        when().
                post("https://restapi.wcaquino.me/upload").
        then().
                log().all().
                statusCode(200).
                body("name", is ("users.pdf"))
        ;
    }

    @Test
    public void deveFazerDownloadDoArquivo() throws IOException {

        byte[] image = given().
                            log().all().
            when().
                    get("https://restapi.wcaquino.me/download").
            then().
                    statusCode(200).
                    extract().asByteArray()
            ;

        File imagem = new File("src/main/resources/file.jpg");
        OutputStream out = new FileOutputStream(imagem);
        out.write(image);
        out.close();

        System.out.println(imagem.length());
        assertThat(imagem.length(), lessThan(100000L));

    }
}
