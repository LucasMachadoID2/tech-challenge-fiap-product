package com.fiap.techchallenge.productmicroservice.infrastructure.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fiap.techchallenge.productmicroservice.application.dto.CreateProductRequestDTO;
import com.fiap.techchallenge.productmicroservice.application.dto.ProductResponseDTO;
import com.fiap.techchallenge.productmicroservice.application.services.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductController.class)
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ProductService productService;

    private ProductResponseDTO productResponse;
    private CreateProductRequestDTO createRequest;

    @BeforeEach
    void setUp() {
        productResponse = new ProductResponseDTO();
        productResponse.setId("1");
        productResponse.setName("Test Product");
        productResponse.setDescription("Test Description");
        productResponse.setPrice(new BigDecimal("25.90"));
        productResponse.setCategory("LANCHE");
        productResponse.setOnPromotion(false);

        createRequest = new CreateProductRequestDTO();
        createRequest.setName("Test Product");
        createRequest.setDescription("Test Description");
        createRequest.setPrice(new BigDecimal("25.90"));
        createRequest.setCategory("LANCHE");
    }

    @Test
    @DisplayName("Should create product successfully")
    void shouldCreateProductSuccessfully() throws Exception {
        when(productService.createProduct(any(CreateProductRequestDTO.class)))
                .thenReturn(productResponse);

        mockMvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.name").value("Test Product"))
                .andExpect(jsonPath("$.price").value(25.90));

        verify(productService, times(1)).createProduct(any(CreateProductRequestDTO.class));
    }

    @Test
    @DisplayName("Should find product by id successfully")
    void shouldFindProductByIdSuccessfully() throws Exception {
        when(productService.findById("1")).thenReturn(Optional.of(productResponse));

        mockMvc.perform(get("/api/products/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.name").value("Test Product"));

        verify(productService, times(1)).findById("1");
    }

    @Test
    @DisplayName("Should return 404 when product not found")
    void shouldReturn404WhenProductNotFound() throws Exception {
        when(productService.findById("999")).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/products/999"))
                .andExpect(status().isNotFound());

        verify(productService, times(1)).findById("999");
    }

    @Test
    @DisplayName("Should find all products successfully")
    void shouldFindAllProductsSuccessfully() throws Exception {
        List<ProductResponseDTO> products = Arrays.asList(productResponse);
        when(productService.findAll()).thenReturn(products);

        mockMvc.perform(get("/api/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value("1"))
                .andExpect(jsonPath("$[0].name").value("Test Product"));

        verify(productService, times(1)).findAll();
    }

    @Test
    @DisplayName("Should delete product successfully")
    void shouldDeleteProductSuccessfully() throws Exception {
        doNothing().when(productService).deleteById("1");

        mockMvc.perform(delete("/api/products/1"))
                .andExpect(status().isNoContent());

        verify(productService, times(1)).deleteById("1");
    }

    @Test
    @DisplayName("Should return 404 when deleting non-existent product")
    void shouldReturn404WhenDeletingNonExistentProduct() throws Exception {
        doThrow(new IllegalArgumentException("Product not found"))
                .when(productService).deleteById("999");

        mockMvc.perform(delete("/api/products/999"))
                .andExpect(status().isNotFound());

        verify(productService, times(1)).deleteById("999");
    }

    @Test
    @DisplayName("Should find products by category successfully")
    void shouldFindProductsByCategorySuccessfully() throws Exception {
        List<ProductResponseDTO> products = Arrays.asList(productResponse);
        when(productService.findByCategory("LANCHE")).thenReturn(products);

        mockMvc.perform(get("/api/products/category/LANCHE"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].category").value("LANCHE"));

        verify(productService, times(1)).findByCategory("LANCHE");
    }

    @Test
    @DisplayName("Should find products on promotion successfully")
    void shouldFindProductsOnPromotionSuccessfully() throws Exception {
        productResponse.setOnPromotion(true);
        productResponse.setPromotionPrice(new BigDecimal("19.90"));
        List<ProductResponseDTO> products = Arrays.asList(productResponse);
        when(productService.findProductsOnPromotion()).thenReturn(products);

        mockMvc.perform(get("/api/products/promotions"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].onPromotion").value(true));

        verify(productService, times(1)).findProductsOnPromotion();
    }

    @Test
    @DisplayName("Should find products by name successfully")
    void shouldFindProductsByNameSuccessfully() throws Exception {
        List<ProductResponseDTO> products = Arrays.asList(productResponse);
        when(productService.findByName("Test")).thenReturn(products);

        mockMvc.perform(get("/api/products/search")
                        .param("name", "Test"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Test Product"));

        verify(productService, times(1)).findByName("Test");
    }

    @Test
    @DisplayName("Should find products by category and price range successfully")
    void shouldFindProductsByCategoryAndPriceRangeSuccessfully() throws Exception {
        List<ProductResponseDTO> products = Arrays.asList(productResponse);
        when(productService.findByCategoryAndPriceRange(
                eq("LANCHE"), 
                eq(new BigDecimal("10.00")), 
                eq(new BigDecimal("30.00"))))
                .thenReturn(products);

        mockMvc.perform(get("/api/products/category/LANCHE/price-range")
                        .param("minPrice", "10.00")
                        .param("maxPrice", "30.00"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].category").value("LANCHE"));

        verify(productService, times(1)).findByCategoryAndPriceRange(
                eq("LANCHE"), 
                eq(new BigDecimal("10.00")), 
                eq(new BigDecimal("30.00")));
    }

    @Test
    @DisplayName("Should find products by category and price range manual successfully")
    void shouldFindProductsByCategoryAndPriceRangeManualSuccessfully() throws Exception {
        List<ProductResponseDTO> products = Arrays.asList(productResponse);
        when(productService.findByCategoryAndPriceRangeManual(
                eq("LANCHE"), 
                eq(new BigDecimal("10.00")), 
                eq(new BigDecimal("30.00"))))
                .thenReturn(products);

        mockMvc.perform(get("/api/products/category/LANCHE/price-range-manual")
                        .param("minPrice", "10.00")
                        .param("maxPrice", "30.00"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].category").value("LANCHE"));

        verify(productService, times(1)).findByCategoryAndPriceRangeManual(
                eq("LANCHE"), 
                eq(new BigDecimal("10.00")), 
                eq(new BigDecimal("30.00")));
    }

    @Test
    @DisplayName("Should return empty list when no products found")
    void shouldReturnEmptyListWhenNoProductsFound() throws Exception {
        when(productService.findAll()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());

        verify(productService, times(1)).findAll();
    }
}
