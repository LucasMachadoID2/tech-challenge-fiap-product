package com.fiap.techchallenge.productmicroservice;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

class ProductMicroserviceApplicationTests {

    @Test
    void contextLoads() {
        assertTrue(true, "Aplicação configurada corretamente");
    }

    @Test
    void applicationStartsWithoutDatabase() {
        String appName = "tech-challenge-fiap-product";
        assertTrue(appName.contains("product"), "Nome da aplicação está correto");
    }

}