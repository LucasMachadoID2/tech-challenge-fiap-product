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
        product1 = new Product("Budget Burger", "Affordable burger", new BigDecimal("15.00"), "LANCHE");
        product1.setId("1");

        product2 = new Product("Classic Burger", "Standard burger", new BigDecimal("25.00"), "LANCHE");
        product2.setId("2");

        product3 = new Product("Premium Burger", "Luxury burger", new BigDecimal("45.00"), "LANCHE");
        product3.setId("3");
    }

    @Test
    @DisplayName("Should find products by category and price range successfully")
    void shouldFindProductsByCategoryAndPriceRange() {
        when(productRepository.findByCategoryAndPriceBetween(
                "LANCHE", new BigDecimal("10.00"), new BigDecimal("30.00")))
                .thenReturn(Arrays.asList(product1, product2));

        List<Product> result = useCase.execute("LANCHE", new BigDecimal("10.00"), new BigDecimal("30.00"));

        assertThat(result).isNotNull();
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getPrice()).isBetween(new BigDecimal("10.00"), new BigDecimal("30.00"));
        assertThat(result.get(1).getPrice()).isBetween(new BigDecimal("10.00"), new BigDecimal("30.00"));
        verify(productRepository, times(1))
                .findByCategoryAndPriceBetween("LANCHE", new BigDecimal("10.00"), new BigDecimal("30.00"));
    }

    @Test
    @DisplayName("Should return empty list when no products in price range")
    void shouldReturnEmptyListWhenNoProductsInPriceRange() {
        when(productRepository.findByCategoryAndPriceBetween(
                "LANCHE", new BigDecimal("100.00"), new BigDecimal("200.00")))
                .thenReturn(Collections.emptyList());

        List<Product> result = useCase.execute("LANCHE", new BigDecimal("100.00"), new BigDecimal("200.00"));

        assertThat(result).isNotNull();
        assertThat(result).isEmpty();
        verify(productRepository, times(1))
                .findByCategoryAndPriceBetween("LANCHE", new BigDecimal("100.00"), new BigDecimal("200.00"));
    }

    @Test
    @DisplayName("Should throw exception when category is null")
    void shouldThrowExceptionWhenCategoryIsNull() {
        assertThatThrownBy(() -> useCase.execute(null, new BigDecimal("10.00"), new BigDecimal("30.00")))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Categoria é obrigatória");

        verify(productRepository, never()).findByCategoryAndPriceBetween(any(), any(), any());
    }

    @Test
    @DisplayName("Should throw exception when category is empty")
    void shouldThrowExceptionWhenCategoryIsEmpty() {
        assertThatThrownBy(() -> useCase.execute("", new BigDecimal("10.00"), new BigDecimal("30.00")))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Categoria é obrigatória");

        verify(productRepository, never()).findByCategoryAndPriceBetween(any(), any(), any());
    }

    @Test
    @DisplayName("Should throw exception when minPrice is null")
    void shouldThrowExceptionWhenMinPriceIsNull() {
        assertThatThrownBy(() -> useCase.execute("LANCHE", null, new BigDecimal("30.00")))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Preço mínimo deve ser maior ou igual a zero");

        verify(productRepository, never()).findByCategoryAndPriceBetween(any(), any(), any());
    }

    @Test
    @DisplayName("Should throw exception when minPrice is negative")
    void shouldThrowExceptionWhenMinPriceIsNegative() {
        assertThatThrownBy(() -> useCase.execute("LANCHE", new BigDecimal("-10.00"), new BigDecimal("30.00")))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Preço mínimo deve ser maior ou igual a zero");

        verify(productRepository, never()).findByCategoryAndPriceBetween(any(), any(), any());
    }

    @Test
    @DisplayName("Should throw exception when maxPrice is null")
    void shouldThrowExceptionWhenMaxPriceIsNull() {
        assertThatThrownBy(() -> useCase.execute("LANCHE", new BigDecimal("10.00"), null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Preço máximo deve ser maior que zero");

        verify(productRepository, never()).findByCategoryAndPriceBetween(any(), any(), any());
    }

    @Test
    @DisplayName("Should throw exception when maxPrice is zero")
    void shouldThrowExceptionWhenMaxPriceIsZero() {
        assertThatThrownBy(() -> useCase.execute("LANCHE", new BigDecimal("10.00"), BigDecimal.ZERO))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Preço máximo deve ser maior que zero");

        verify(productRepository, never()).findByCategoryAndPriceBetween(any(), any(), any());
    }

    @Test
    @DisplayName("Should throw exception when minPrice is greater than maxPrice")
    void shouldThrowExceptionWhenMinPriceIsGreaterThanMaxPrice() {
        assertThatThrownBy(() -> useCase.execute("LANCHE", new BigDecimal("50.00"), new BigDecimal("30.00")))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Preço mínimo não pode ser maior que o preço máximo");

        verify(productRepository, never()).findByCategoryAndPriceBetween(any(), any(), any());
    }

    @Test
    @DisplayName("Should accept minPrice equal to zero")
    void shouldAcceptMinPriceEqualToZero() {
        when(productRepository.findByCategoryAndPriceBetween(
                "LANCHE", BigDecimal.ZERO, new BigDecimal("30.00")))
                .thenReturn(Arrays.asList(product1, product2));

        List<Product> result = useCase.execute("LANCHE", BigDecimal.ZERO, new BigDecimal("30.00"));

        assertThat(result).isNotNull();
        assertThat(result).hasSize(2);
        verify(productRepository, times(1))
                .findByCategoryAndPriceBetween("LANCHE", BigDecimal.ZERO, new BigDecimal("30.00"));
    }

    @Test
    @DisplayName("Should accept minPrice equal to maxPrice")
    void shouldAcceptMinPriceEqualToMaxPrice() {
        when(productRepository.findByCategoryAndPriceBetween(
                "LANCHE", new BigDecimal("25.00"), new BigDecimal("25.00")))
                .thenReturn(Collections.singletonList(product2));

        List<Product> result = useCase.execute("LANCHE", new BigDecimal("25.00"), new BigDecimal("25.00"));

        assertThat(result).isNotNull();
        assertThat(result).hasSize(1);
        verify(productRepository, times(1))
                .findByCategoryAndPriceBetween("LANCHE", new BigDecimal("25.00"), new BigDecimal("25.00"));
    }
}
