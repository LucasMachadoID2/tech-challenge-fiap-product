package com.fiap.techchallenge.productmicroservice.domain.usecases;

import com.fiap.techchallenge.productmicroservice.domain.entities.Product;
import com.fiap.techchallenge.productmicroservice.domain.repositories.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateProductUseCaseTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private CreateProductUseCase createProductUseCase;

    private Product product;

    @BeforeEach
    void setUp() {
        product = new Product();
        product.setName("Test Product");
        product.setDescription("Description");
        product.setPrice(new BigDecimal("25.90"));
        product.setCategory("LANCHE");
    }

    @Test
    void shouldCreateProduct() {
        when(productRepository.save(any(Product.class))).thenReturn(product);

        Product result = createProductUseCase.execute(product);

        assertNotNull(result);
        assertEquals("Test Product", result.getName());
        verify(productRepository, times(1)).save(product);
    }
}
