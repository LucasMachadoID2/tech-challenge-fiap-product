package com.fiap.techchallenge.productmicroservice.infrastructure.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fiap.techchallenge.productmicroservice.application.dto.CreateProductRequestDTO;
import com.fiap.techchallenge.productmicroservice.domain.entities.CategoryEnum;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import com.fiap.techchallenge.productmicroservice.application.services.ProductService;


import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;

@WebMvcTest(controllers = {ProductController.class, GlobalExceptionHandler.class})
class GlobalExceptionHandlerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ProductService productService;

    @Test
    @DisplayName("Should handle IllegalArgumentException")
    void shouldHandleIllegalArgumentException() throws Exception {
        when(productService.createProduct(any(CreateProductRequestDTO.class)))
                .thenThrow(new IllegalArgumentException("Invalid data"));

        CreateProductRequestDTO request = new CreateProductRequestDTO();
        request.setName("Test");
        request.setDescription("Test");
        request.setPrice(1000L);
        request.setCategory(CategoryEnum.LANCHE);
        
        mockMvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("VALIDATION_ERROR"))
                .andExpect(jsonPath("$.message").value("Invalid data"))
                .andExpect(jsonPath("$.timestamp", notNullValue()));
    }

    @Test
    @DisplayName("Should handle MethodArgumentNotValidException")
    void shouldHandleMethodArgumentNotValidException() throws Exception {
        CreateProductRequestDTO request = new CreateProductRequestDTO();
        request.setName(""); // Invalid: empty name
        request.setDescription("Test");
        request.setCategory(CategoryEnum.LANCHE);
        
        mockMvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("VALIDATION_ERROR"))
                .andExpect(jsonPath("$.message").value("Dados inválidos fornecidos"))
                .andExpect(jsonPath("$.timestamp", notNullValue()))
                .andExpect(jsonPath("$.details", notNullValue()));
    }

    @Test
    @DisplayName("Should handle RuntimeException")
    void shouldHandleRuntimeException() throws Exception {
        when(productService.createProduct(any(CreateProductRequestDTO.class)))
                .thenThrow(new RuntimeException("Unexpected error"));

        CreateProductRequestDTO request = new CreateProductRequestDTO();
        request.setName("Test");
        request.setDescription("Test");
        request.setPrice(1000L);
        request.setCategory(CategoryEnum.LANCHE);
        
        mockMvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.code").value("INTERNAL_ERROR"))
                .andExpect(jsonPath("$.message").value("Erro interno do servidor"))
                .andExpect(jsonPath("$.timestamp", notNullValue()));
    }
}
