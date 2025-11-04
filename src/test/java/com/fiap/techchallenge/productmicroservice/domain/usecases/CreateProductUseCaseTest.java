package com.fiap.techchallenge.productmicroservice.domain.usecases;

import com.fiap.techchallenge.productmicroservice.domain.entities.CategoryEnum;
import com.fiap.techchallenge.productmicroservice.domain.entities.Product;
import com.fiap.techchallenge.productmicroservice.domain.repositories.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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
        product = new Product("Test Product", "Description", "http://image.url", 2590L, 2000L, CategoryEnum.LANCHE, 10L);
        product.setId("1");
    }

    @Test
    @DisplayName("Should create product successfully")
    void shouldCreateProduct() {
        when(productRepository.save(any(Product.class))).thenReturn(product);

        Product result = createProductUseCase.execute(product);

        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo("Test Product");
        assertThat(result.getPrice()).isEqualTo(2590L);
        assertThat(result.getPriceForClient()).isEqualTo(2000L);
        assertThat(result.getQuantity()).isEqualTo(10L);
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

    @Test
    @DisplayName("Should create product with all categories")
    void shouldCreateProductWithAllCategories() {
        CategoryEnum[] categories = {CategoryEnum.LANCHE, CategoryEnum.BEBIDA, CategoryEnum.ACOMPANHAMENTO, CategoryEnum.SOBREMESA};
        
        for (CategoryEnum category : categories) {
            Product productWithCategory = new Product("Product", "Desc", "image.url", 1000L, 900L, category, 5L);
            when(productRepository.save(any(Product.class))).thenReturn(productWithCategory);
            
            Product result = createProductUseCase.execute(productWithCategory);
            
            assertThat(result).isNotNull();
            assertThat(result.getCategory()).isEqualTo(category);
        }
        
        verify(productRepository, times(4)).save(any(Product.class));
    }

    @Test
    @DisplayName("Should create product with minimum valid data")
    void shouldCreateProductWithMinimumValidData() {
        Product minProduct = new Product("Min", "Desc", "img.url", 1L, 1L, CategoryEnum.LANCHE, 1L);
        when(productRepository.save(any(Product.class))).thenReturn(minProduct);

        Product result = createProductUseCase.execute(minProduct);

        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo("Min");
        assertThat(result.getPrice()).isEqualTo(1L);
        verify(productRepository, times(1)).save(minProduct);
    }

    @Test
    @DisplayName("Should create product with large values")
    void shouldCreateProductWithLargeValues() {
        Product largeProduct = new Product("Product", "Desc", "image.url", 999999L, 888888L, CategoryEnum.LANCHE, 1000L);
        when(productRepository.save(any(Product.class))).thenReturn(largeProduct);

        Product result = createProductUseCase.execute(largeProduct);

        assertThat(result).isNotNull();
        assertThat(result.getPrice()).isEqualTo(999999L);
        assertThat(result.getQuantity()).isEqualTo(1000L);
        verify(productRepository, times(1)).save(largeProduct);
    }
}
