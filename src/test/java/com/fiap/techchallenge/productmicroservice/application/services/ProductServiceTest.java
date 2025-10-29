package com.fiap.techchallenge.productmicroservice.application.services;

import com.fiap.techchallenge.productmicroservice.application.dto.CreateProductRequestDTO;
import com.fiap.techchallenge.productmicroservice.application.dto.ProductResponseDTO;
import com.fiap.techchallenge.productmicroservice.domain.entities.Product;
import com.fiap.techchallenge.productmicroservice.domain.usecases.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private CreateProductUseCase createProductUseCase;
    
    @Mock
    private FindProductByIdUseCase findProductByIdUseCase;
    
    @Mock
    private FindAllProductsUseCase findAllProductsUseCase;
    
    @Mock
    private FindProductsByCategoryUseCase findProductsByCategoryUseCase;
    
    @Mock
    private FindProductsByNameUseCase findProductsByNameUseCase;
    
    @Mock
    private FindProductsOnPromotionUseCase findProductsOnPromotionUseCase;
    
    @Mock
    private FindProductsByCategoryAndPriceRangeUseCase findProductsByCategoryAndPriceRangeUseCase;
    
    @Mock
    private DeleteProductByIdUseCase deleteProductByIdUseCase;
    
    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private ProductService productService;

    private Product testProduct;
    private ProductResponseDTO testProductResponse;
    private CreateProductRequestDTO createRequest;

    @BeforeEach
    void setUp() {
        testProduct = new Product();
        testProduct.setId("123");
        testProduct.setName("Test Product");
        testProduct.setDescription("Test Description");
        testProduct.setPrice(new BigDecimal("25.90"));
        testProduct.setCategory("LANCHE");

        testProductResponse = new ProductResponseDTO();
        testProductResponse.setId("123");
        testProductResponse.setName("Test Product");
        testProductResponse.setDescription("Test Description");
        testProductResponse.setPrice(new BigDecimal("25.90"));
        testProductResponse.setCategory("LANCHE");
        testProductResponse.setEffectivePrice(new BigDecimal("25.90"));

        createRequest = new CreateProductRequestDTO();
        createRequest.setName("Test Product");
        createRequest.setDescription("Test Description");
        createRequest.setPrice(new BigDecimal("25.90"));
        createRequest.setCategory("LANCHE");
    }

    @Test
    void shouldCreateProduct() {
        when(modelMapper.map(any(CreateProductRequestDTO.class), eq(Product.class))).thenReturn(testProduct);
        when(createProductUseCase.execute(any(Product.class))).thenReturn(testProduct);
        when(modelMapper.map(any(Product.class), eq(ProductResponseDTO.class))).thenReturn(testProductResponse);

        ProductResponseDTO result = productService.createProduct(createRequest);

        assertNotNull(result);
        assertEquals("123", result.getId());
        assertEquals("Test Product", result.getName());
        verify(createProductUseCase, times(1)).execute(any(Product.class));
    }

    @Test
    void shouldFindProductById() {
        when(findProductByIdUseCase.execute("123")).thenReturn(Optional.of(testProduct));
        when(modelMapper.map(any(Product.class), eq(ProductResponseDTO.class))).thenReturn(testProductResponse);

        Optional<ProductResponseDTO> result = productService.findById("123");

        assertTrue(result.isPresent());
        assertEquals("Test Product", result.get().getName());
        verify(findProductByIdUseCase, times(1)).execute("123");
    }

    @Test
    void shouldReturnEmptyWhenProductNotFound() {
        when(findProductByIdUseCase.execute("999")).thenReturn(Optional.empty());

        Optional<ProductResponseDTO> result = productService.findById("999");

        assertFalse(result.isPresent());
    }

    @Test
    void shouldFindAllProducts() {
        List<Product> products = Arrays.asList(testProduct);
        when(findAllProductsUseCase.execute()).thenReturn(products);
        when(modelMapper.map(any(Product.class), eq(ProductResponseDTO.class))).thenReturn(testProductResponse);

        List<ProductResponseDTO> result = productService.findAll();

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(findAllProductsUseCase, times(1)).execute();
    }

    @Test
    void shouldFindProductsByCategory() {
        List<Product> products = Arrays.asList(testProduct);
        when(findProductsByCategoryUseCase.execute("LANCHE")).thenReturn(products);
        when(modelMapper.map(any(Product.class), eq(ProductResponseDTO.class))).thenReturn(testProductResponse);

        List<ProductResponseDTO> result = productService.findByCategory("LANCHE");

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("LANCHE", result.get(0).getCategory());
    }

    @Test
    void shouldFindProductsByName() {
        List<Product> products = Arrays.asList(testProduct);
        when(findProductsByNameUseCase.execute("Test")).thenReturn(products);
        when(modelMapper.map(any(Product.class), eq(ProductResponseDTO.class))).thenReturn(testProductResponse);

        List<ProductResponseDTO> result = productService.findByName("Test");

        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void shouldFindProductsOnPromotion() {
        testProduct.setOnPromotion(true);
        testProduct.setPromotionPrice(new BigDecimal("19.90"));
        testProductResponse.setOnPromotion(true);
        testProductResponse.setPromotionPrice(new BigDecimal("19.90"));
        testProductResponse.setEffectivePrice(new BigDecimal("19.90"));
        
        List<Product> products = Arrays.asList(testProduct);
        when(findProductsOnPromotionUseCase.execute()).thenReturn(products);
        when(modelMapper.map(any(Product.class), eq(ProductResponseDTO.class))).thenReturn(testProductResponse);

        List<ProductResponseDTO> result = productService.findProductsOnPromotion();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertTrue(result.get(0).isOnPromotion());
    }

    @Test
    void shouldFindProductsByCategoryAndPriceRange() {
        List<Product> products = Arrays.asList(testProduct);
        when(findProductsByCategoryAndPriceRangeUseCase.execute(
            anyString(), any(BigDecimal.class), any(BigDecimal.class)
        )).thenReturn(products);
        when(modelMapper.map(any(Product.class), eq(ProductResponseDTO.class))).thenReturn(testProductResponse);

        List<ProductResponseDTO> result = productService.findByCategoryAndPriceRange(
            "LANCHE", new BigDecimal("10"), new BigDecimal("30")
        );

        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void shouldDeleteProduct() {
        doNothing().when(deleteProductByIdUseCase).execute("123");

        assertDoesNotThrow(() -> productService.deleteById("123"));
        verify(deleteProductByIdUseCase, times(1)).execute("123");
    }

    @Test
    void shouldCalculateEffectivePriceForProductOnPromotion() {
        testProduct.setOnPromotion(true);
        testProduct.setPromotionPrice(new BigDecimal("19.90"));
        testProductResponse.setOnPromotion(true);
        testProductResponse.setPromotionPrice(new BigDecimal("19.90"));
        testProductResponse.setEffectivePrice(new BigDecimal("19.90"));
        
        when(modelMapper.map(any(CreateProductRequestDTO.class), eq(Product.class))).thenReturn(testProduct);
        when(createProductUseCase.execute(any(Product.class))).thenReturn(testProduct);
        when(modelMapper.map(any(Product.class), eq(ProductResponseDTO.class))).thenReturn(testProductResponse);

        createRequest.setOnPromotion(true);
        createRequest.setPromotionPrice(new BigDecimal("19.90"));
        
        ProductResponseDTO result = productService.createProduct(createRequest);

        assertEquals(new BigDecimal("19.90"), result.getEffectivePrice());
    }

    @Test
    void shouldCalculateEffectivePriceForRegularProduct() {
        when(modelMapper.map(any(CreateProductRequestDTO.class), eq(Product.class))).thenReturn(testProduct);
        when(createProductUseCase.execute(any(Product.class))).thenReturn(testProduct);
        when(modelMapper.map(any(Product.class), eq(ProductResponseDTO.class))).thenReturn(testProductResponse);

        ProductResponseDTO result = productService.createProduct(createRequest);

        assertEquals(testProduct.getPrice(), result.getEffectivePrice());
    }
}
