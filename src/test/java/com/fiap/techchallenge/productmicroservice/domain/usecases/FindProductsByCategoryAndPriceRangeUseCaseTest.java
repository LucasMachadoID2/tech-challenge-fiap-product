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

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FindProductsByCategoryAndPriceRangeUseCaseTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private FindProductsByCategoryAndPriceRangeUseCase useCase;

    private Product product1;
    private Product product2;
    private Product product3;

    @BeforeEach
    void setUp() {
        product1 = new Product("Budget Burger", "Affordable burger", "image.url", 1500L, 1200L, CategoryEnum.LANCHE, 10L);
        product1.setId("1");

        product2 = new Product("Classic Burger", "Standard burger", "image.url", 2500L, 2000L, CategoryEnum.LANCHE, 15L);
        product2.setId("2");

        product3 = new Product("Premium Burger", "Luxury burger", "image.url", 4500L, 4000L, CategoryEnum.LANCHE, 5L);
        product3.setId("3");
    }

    @Test
    @DisplayName("Should find products by category and price range successfully")
    void shouldFindProductsByCategoryAndPriceRange() {
        when(productRepository.findByCategoryAndPriceBetween(
                CategoryEnum.LANCHE, 1000L, 3000L))
                .thenReturn(Arrays.asList(product1, product2));

        List<Product> result = useCase.execute(CategoryEnum.LANCHE, 1000L, 3000L);

        assertThat(result).isNotNull();
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getPrice()).isBetween(1000L, 3000L);
        assertThat(result.get(1).getPrice()).isBetween(1000L, 3000L);
        verify(productRepository, times(1))
                .findByCategoryAndPriceBetween(CategoryEnum.LANCHE, 1000L, 3000L);
    }

    @Test
    @DisplayName("Should return empty list when no products in price range")
    void shouldReturnEmptyListWhenNoProductsInPriceRange() {
        when(productRepository.findByCategoryAndPriceBetween(
                CategoryEnum.LANCHE, 10000L, 20000L))
                .thenReturn(Collections.emptyList());

        List<Product> result = useCase.execute(CategoryEnum.LANCHE, 10000L, 20000L);

        assertThat(result).isNotNull();
        assertThat(result).isEmpty();
        verify(productRepository, times(1))
                .findByCategoryAndPriceBetween(CategoryEnum.LANCHE, 10000L, 20000L);
    }

    @Test
    @DisplayName("Should throw exception when category is null")
    void shouldThrowExceptionWhenCategoryIsNull() {
        assertThatThrownBy(() -> useCase.execute(null, 1000L, 3000L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Categoria é obrigatória");

        verify(productRepository, never()).findByCategoryAndPriceBetween(any(), any(), any());
    }

    @Test
    @DisplayName("Should throw exception when minPrice is null")
    void shouldThrowExceptionWhenMinPriceIsNull() {
        assertThatThrownBy(() -> useCase.execute(CategoryEnum.LANCHE, null, 3000L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Preço mínimo deve ser maior ou igual a zero");

        verify(productRepository, never()).findByCategoryAndPriceBetween(any(), any(), any());
    }

    @Test
    @DisplayName("Should throw exception when minPrice is negative")
    void shouldThrowExceptionWhenMinPriceIsNegative() {
        assertThatThrownBy(() -> useCase.execute(CategoryEnum.LANCHE, -1000L, 3000L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Preço mínimo deve ser maior ou igual a zero");

        verify(productRepository, never()).findByCategoryAndPriceBetween(any(), any(), any());
    }

    @Test
    @DisplayName("Should throw exception when maxPrice is null")
    void shouldThrowExceptionWhenMaxPriceIsNull() {
        assertThatThrownBy(() -> useCase.execute(CategoryEnum.LANCHE, 1000L, null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Preço máximo deve ser maior que zero");

        verify(productRepository, never()).findByCategoryAndPriceBetween(any(), any(), any());
    }

    @Test
    @DisplayName("Should throw exception when maxPrice is zero")
    void shouldThrowExceptionWhenMaxPriceIsZero() {
        assertThatThrownBy(() -> useCase.execute(CategoryEnum.LANCHE, 1000L, 0L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Preço máximo deve ser maior que zero");

        verify(productRepository, never()).findByCategoryAndPriceBetween(any(), any(), any());
    }

    @Test
    @DisplayName("Should throw exception when minPrice is greater than maxPrice")
    void shouldThrowExceptionWhenMinPriceIsGreaterThanMaxPrice() {
        assertThatThrownBy(() -> useCase.execute(CategoryEnum.LANCHE, 5000L, 3000L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Preço mínimo não pode ser maior que o preço máximo");

        verify(productRepository, never()).findByCategoryAndPriceBetween(any(), any(), any());
    }

    @Test
    @DisplayName("Should accept minPrice equal to zero")
    void shouldAcceptMinPriceEqualToZero() {
        when(productRepository.findByCategoryAndPriceBetween(
                CategoryEnum.LANCHE, 0L, 3000L))
                .thenReturn(Arrays.asList(product1, product2));

        List<Product> result = useCase.execute(CategoryEnum.LANCHE, 0L, 3000L);

        assertThat(result).isNotNull();
        assertThat(result).hasSize(2);
        verify(productRepository, times(1))
                .findByCategoryAndPriceBetween(CategoryEnum.LANCHE, 0L, 3000L);
    }

    @Test
    @DisplayName("Should accept minPrice equal to maxPrice")
    void shouldAcceptMinPriceEqualToMaxPrice() {
        when(productRepository.findByCategoryAndPriceBetween(
                CategoryEnum.LANCHE, 2500L, 2500L))
                .thenReturn(Collections.singletonList(product2));

        List<Product> result = useCase.execute(CategoryEnum.LANCHE, 2500L, 2500L);

        assertThat(result).isNotNull();
        assertThat(result).hasSize(1);
        verify(productRepository, times(1))
                .findByCategoryAndPriceBetween(CategoryEnum.LANCHE, 2500L, 2500L);
    }
}
