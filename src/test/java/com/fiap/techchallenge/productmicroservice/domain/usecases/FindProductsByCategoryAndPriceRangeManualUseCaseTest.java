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
class FindProductsByCategoryAndPriceRangeManualUseCaseTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private FindProductsByCategoryAndPriceRangeManualUseCase useCase;

    private Product product1;
    private Product product2;
    private Product product3;

    @BeforeEach
    void setUp() {
        product1 = new Product("Economy Snack", "Budget option", new BigDecimal("12.00"), "LANCHE");
        product1.setId("1");

        product2 = new Product("Standard Meal", "Regular meal", new BigDecimal("22.00"), "LANCHE");
        product2.setId("2");

        product3 = new Product("Deluxe Combo", "Premium combo", new BigDecimal("55.00"), "LANCHE");
        product3.setId("3");
    }

    @Test
    @DisplayName("Should find products by category and price range using manual query")
    void shouldFindProductsByCategoryAndPriceRangeManual() {
        when(productRepository.findByCategoryAndPriceRangeManual(
                "LANCHE", new BigDecimal("10.00"), new BigDecimal("30.00")))
                .thenReturn(Arrays.asList(product1, product2));

        List<Product> result = useCase.execute("LANCHE", new BigDecimal("10.00"), new BigDecimal("30.00"));

        assertThat(result).isNotNull();
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getPrice()).isBetween(new BigDecimal("10.00"), new BigDecimal("30.00"));
        assertThat(result.get(1).getPrice()).isBetween(new BigDecimal("10.00"), new BigDecimal("30.00"));
        verify(productRepository, times(1))
                .findByCategoryAndPriceRangeManual("LANCHE", new BigDecimal("10.00"), new BigDecimal("30.00"));
    }

    @Test
    @DisplayName("Should return empty list when no products match criteria")
    void shouldReturnEmptyListWhenNoProductsMatch() {
        when(productRepository.findByCategoryAndPriceRangeManual(
                "BEBIDA", new BigDecimal("100.00"), new BigDecimal("150.00")))
                .thenReturn(Collections.emptyList());

        List<Product> result = useCase.execute("BEBIDA", new BigDecimal("100.00"), new BigDecimal("150.00"));

        assertThat(result).isNotNull();
        assertThat(result).isEmpty();
        verify(productRepository, times(1))
                .findByCategoryAndPriceRangeManual("BEBIDA", new BigDecimal("100.00"), new BigDecimal("150.00"));
    }

    @Test
    @DisplayName("Should throw exception when category is null")
    void shouldThrowExceptionWhenCategoryIsNull() {
        assertThatThrownBy(() -> useCase.execute(null, new BigDecimal("10.00"), new BigDecimal("30.00")))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Categoria é obrigatória");

        verify(productRepository, never()).findByCategoryAndPriceRangeManual(any(), any(), any());
    }

    @Test
    @DisplayName("Should throw exception when category is empty")
    void shouldThrowExceptionWhenCategoryIsEmpty() {
        assertThatThrownBy(() -> useCase.execute("", new BigDecimal("10.00"), new BigDecimal("30.00")))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Categoria é obrigatória");

        verify(productRepository, never()).findByCategoryAndPriceRangeManual(any(), any(), any());
    }

    @Test
    @DisplayName("Should throw exception when category is blank")
    void shouldThrowExceptionWhenCategoryIsBlank() {
        assertThatThrownBy(() -> useCase.execute("   ", new BigDecimal("10.00"), new BigDecimal("30.00")))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Categoria é obrigatória");

        verify(productRepository, never()).findByCategoryAndPriceRangeManual(any(), any(), any());
    }

    @Test
    @DisplayName("Should throw exception when minPrice is null")
    void shouldThrowExceptionWhenMinPriceIsNull() {
        assertThatThrownBy(() -> useCase.execute("LANCHE", null, new BigDecimal("30.00")))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Preço mínimo deve ser maior ou igual a zero");

        verify(productRepository, never()).findByCategoryAndPriceRangeManual(any(), any(), any());
    }

    @Test
    @DisplayName("Should throw exception when minPrice is negative")
    void shouldThrowExceptionWhenMinPriceIsNegative() {
        assertThatThrownBy(() -> useCase.execute("LANCHE", new BigDecimal("-5.00"), new BigDecimal("30.00")))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Preço mínimo deve ser maior ou igual a zero");

        verify(productRepository, never()).findByCategoryAndPriceRangeManual(any(), any(), any());
    }

    @Test
    @DisplayName("Should throw exception when maxPrice is null")
    void shouldThrowExceptionWhenMaxPriceIsNull() {
        assertThatThrownBy(() -> useCase.execute("LANCHE", new BigDecimal("10.00"), null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Preço máximo deve ser maior que zero");

        verify(productRepository, never()).findByCategoryAndPriceRangeManual(any(), any(), any());
    }

    @Test
    @DisplayName("Should throw exception when maxPrice is zero")
    void shouldThrowExceptionWhenMaxPriceIsZero() {
        assertThatThrownBy(() -> useCase.execute("LANCHE", new BigDecimal("10.00"), BigDecimal.ZERO))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Preço máximo deve ser maior que zero");

        verify(productRepository, never()).findByCategoryAndPriceRangeManual(any(), any(), any());
    }

    @Test
    @DisplayName("Should throw exception when maxPrice is negative")
    void shouldThrowExceptionWhenMaxPriceIsNegative() {
        assertThatThrownBy(() -> useCase.execute("LANCHE", new BigDecimal("10.00"), new BigDecimal("-20.00")))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Preço máximo deve ser maior que zero");

        verify(productRepository, never()).findByCategoryAndPriceRangeManual(any(), any(), any());
    }

    @Test
    @DisplayName("Should throw exception when minPrice is greater than maxPrice")
    void shouldThrowExceptionWhenMinPriceIsGreaterThanMaxPrice() {
        assertThatThrownBy(() -> useCase.execute("LANCHE", new BigDecimal("50.00"), new BigDecimal("30.00")))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Preço mínimo não pode ser maior que o preço máximo");

        verify(productRepository, never()).findByCategoryAndPriceRangeManual(any(), any(), any());
    }

    @Test
    @DisplayName("Should accept minPrice equal to zero")
    void shouldAcceptMinPriceEqualToZero() {
        when(productRepository.findByCategoryAndPriceRangeManual(
                "LANCHE", BigDecimal.ZERO, new BigDecimal("25.00")))
                .thenReturn(Arrays.asList(product1, product2));

        List<Product> result = useCase.execute("LANCHE", BigDecimal.ZERO, new BigDecimal("25.00"));

        assertThat(result).isNotNull();
        assertThat(result).hasSize(2);
        verify(productRepository, times(1))
                .findByCategoryAndPriceRangeManual("LANCHE", BigDecimal.ZERO, new BigDecimal("25.00"));
    }

    @Test
    @DisplayName("Should accept minPrice equal to maxPrice")
    void shouldAcceptMinPriceEqualToMaxPrice() {
        when(productRepository.findByCategoryAndPriceRangeManual(
                "LANCHE", new BigDecimal("22.00"), new BigDecimal("22.00")))
                .thenReturn(Collections.singletonList(product2));

        List<Product> result = useCase.execute("LANCHE", new BigDecimal("22.00"), new BigDecimal("22.00"));

        assertThat(result).isNotNull();
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getPrice()).isEqualByComparingTo(new BigDecimal("22.00"));
        verify(productRepository, times(1))
                .findByCategoryAndPriceRangeManual("LANCHE", new BigDecimal("22.00"), new BigDecimal("22.00"));
    }

    @Test
    @DisplayName("Should handle wide price range")
    void shouldHandleWidePriceRange() {
        when(productRepository.findByCategoryAndPriceRangeManual(
                "LANCHE", new BigDecimal("0.01"), new BigDecimal("1000.00")))
                .thenReturn(Arrays.asList(product1, product2, product3));

        List<Product> result = useCase.execute("LANCHE", new BigDecimal("0.01"), new BigDecimal("1000.00"));

        assertThat(result).isNotNull();
        assertThat(result).hasSize(3);
        verify(productRepository, times(1))
                .findByCategoryAndPriceRangeManual("LANCHE", new BigDecimal("0.01"), new BigDecimal("1000.00"));
    }
}
