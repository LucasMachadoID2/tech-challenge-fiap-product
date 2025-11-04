package com.fiap.techchallenge.productmicroservice.infrastructure.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fiap.techchallenge.productmicroservice.application.dto.CreateProductRequestDTO;
import com.fiap.techchallenge.productmicroservice.application.dto.ProductResponseDTO;
import com.fiap.techchallenge.productmicroservice.application.services.ProductService;
import com.fiap.techchallenge.productmicroservice.domain.entities.CategoryEnum;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

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
        productResponse.setImage("http://image.url");
        productResponse.setPrice(2590L);
        productResponse.setPriceForClient(2000L);
        productResponse.setCategory(CategoryEnum.LANCHE);
        productResponse.setQuantity(10L);

        createRequest = new CreateProductRequestDTO();
        createRequest.setName("Test Product");
        createRequest.setDescription("Test Description");
        createRequest.setImage("http://image.url");
        createRequest.setPrice(2590L);
        createRequest.setPriceForClient(2000L);
        createRequest.setCategory(CategoryEnum.LANCHE);
        createRequest.setQuantity(10L);
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
                .andExpect(jsonPath("$.price").value(2590));

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
        when(productService.findByCategory(CategoryEnum.LANCHE)).thenReturn(products);

        mockMvc.perform(get("/api/products/category/LANCHE"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].category").value("LANCHE"));

        verify(productService, times(1)).findByCategory(CategoryEnum.LANCHE);
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
                eq(CategoryEnum.LANCHE), 
                eq(1000L), 
                eq(3000L)))
                .thenReturn(products);

        mockMvc.perform(get("/api/products/category/LANCHE/price-range")
                        .param("minPrice", "1000")
                        .param("maxPrice", "3000"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].category").value("LANCHE"));

        verify(productService, times(1)).findByCategoryAndPriceRange(
                eq(CategoryEnum.LANCHE), 
                eq(1000L), 
                eq(3000L));
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

    @Test
    @DisplayName("Should return 400 when creating product with null name")
    void shouldReturn400WhenCreatingProductWithNullName() throws Exception {
        CreateProductRequestDTO invalidRequest = new CreateProductRequestDTO();
        invalidRequest.setName(null);
        invalidRequest.setDescription("Description");
        invalidRequest.setPrice(2590L);
        invalidRequest.setCategory(CategoryEnum.LANCHE);

        mockMvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should return 400 when creating product with negative price")
    void shouldReturn400WhenCreatingProductWithNegativePrice() throws Exception {
        CreateProductRequestDTO invalidRequest = new CreateProductRequestDTO();
        invalidRequest.setName("Product");
        invalidRequest.setDescription("Description");
        invalidRequest.setPrice(-100L);
        invalidRequest.setCategory(CategoryEnum.LANCHE);

        mockMvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should return empty list when searching by non-existent name")
    void shouldReturnEmptyListWhenSearchingByNonExistentName() throws Exception {
        when(productService.findByName("NonExistent")).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/products/search")
                        .param("name", "NonExistent"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());

        verify(productService, times(1)).findByName("NonExistent");
    }

    @Test
    @DisplayName("Should return empty list when no products in category")
    void shouldReturnEmptyListWhenNoProductsInCategory() throws Exception {
        when(productService.findByCategory(CategoryEnum.SOBREMESA)).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/products/category/SOBREMESA"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());

        verify(productService, times(1)).findByCategory(CategoryEnum.SOBREMESA);
    }

    @Test
    @DisplayName("Should return empty list when no products in price range")
    void shouldReturnEmptyListWhenNoProductsInPriceRange() throws Exception {
        when(productService.findByCategoryAndPriceRange(
                CategoryEnum.LANCHE, 10000L, 20000L))
                .thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/products/category/LANCHE/price-range")
                        .param("minPrice", "10000")
                        .param("maxPrice", "20000"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());

        verify(productService, times(1)).findByCategoryAndPriceRange(
                CategoryEnum.LANCHE, 10000L, 20000L);
    }

    @Test
    @DisplayName("Should handle all category types")
    void shouldHandleAllCategoryTypes() throws Exception {
        CategoryEnum[] categories = {CategoryEnum.LANCHE, CategoryEnum.BEBIDA, 
                                     CategoryEnum.ACOMPANHAMENTO, CategoryEnum.SOBREMESA};
        
        for (CategoryEnum category : categories) {
            when(productService.findByCategory(category))
                    .thenReturn(Collections.singletonList(productResponse));

            mockMvc.perform(get("/api/products/category/" + category.name()))
                    .andExpect(status().isOk());
        }

        verify(productService, times(4)).findByCategory(any(CategoryEnum.class));
    }

    @Test
    @DisplayName("Should handle product creation with all fields")
    void shouldHandleProductCreationWithAllFields() throws Exception {
        CreateProductRequestDTO fullRequest = new CreateProductRequestDTO();
        fullRequest.setName("Complete Product");
        fullRequest.setDescription("Complete Description");
        fullRequest.setImage("http://complete.image.url");
        fullRequest.setPrice(5000L);
        fullRequest.setPriceForClient(4500L);
        fullRequest.setCategory(CategoryEnum.LANCHE);
        fullRequest.setQuantity(100L);

        ProductResponseDTO fullResponse = new ProductResponseDTO();
        fullResponse.setId("full-1");
        fullResponse.setName("Complete Product");
        fullResponse.setDescription("Complete Description");
        fullResponse.setImage("http://complete.image.url");
        fullResponse.setPrice(5000L);
        fullResponse.setPriceForClient(4500L);
        fullResponse.setCategory(CategoryEnum.LANCHE);
        fullResponse.setQuantity(100L);

        when(productService.createProduct(any(CreateProductRequestDTO.class)))
                .thenReturn(fullResponse);

        mockMvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(fullRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value("full-1"))
                .andExpect(jsonPath("$.name").value("Complete Product"))
                .andExpect(jsonPath("$.price").value(5000))
                .andExpect(jsonPath("$.priceForClient").value(4500))
                .andExpect(jsonPath("$.quantity").value(100));

        verify(productService, times(1)).createProduct(any(CreateProductRequestDTO.class));
    }
}
