package com.fiap.techchallenge.productmicroservice.domain.usecases;

import com.fiap.techchallenge.productmicroservice.domain.entities.Product;
import com.fiap.techchallenge.productmicroservice.domain.repositories.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
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
        product = new Product("Test Product", "Description", new BigDecimal("25.90"), "LANCHE");
        product.setId("1");
    }

    @Test
    @DisplayName("Should create product successfully")
    void shouldCreateProduct() {
        when(productRepository.save(any(Product.class))).thenReturn(product);

        Product result = createProductUseCase.execute(product);

        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo("Test Product");
        assertThat(result.getPrice()).isEqualByComparingTo(new BigDecimal("25.90"));
        verify(productRepository, times(1)).save(product);
    }

    @Test
    @DisplayName("Should create product with promotion")
    void shouldCreateProductWithPromotion() {
        product.applyPromotion(new BigDecimal("19.90"));
        when(productRepository.save(any(Product.class))).thenReturn(product);

        Product result = createProductUseCase.execute(product);

        assertThat(result).isNotNull();
        assertThat(result.isOnPromotion()).isTrue();
        assertThat(result.getPromotionPrice()).isEqualByComparingTo(new BigDecimal("19.90"));
        verify(productRepository, times(1)).save(product);
    }

    @Test
    @DisplayName("Should propagate repository exception")
    void shouldPropagateRepositoryException() {
        when(productRepository.save(any(Product.class)))
                .thenThrow(new RuntimeException("Database error"));

        assertThatThrownBy(() -> createProductUseCase.execute(product))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Database error");
        
        verify(productRepository, times(1)).save(product);
    }
}
