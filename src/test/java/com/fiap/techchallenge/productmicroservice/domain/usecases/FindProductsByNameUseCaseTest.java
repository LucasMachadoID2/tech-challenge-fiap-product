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
class FindProductsByNameUseCaseTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private FindProductsByNameUseCase useCase;

    private Product product1;
    private Product product2;

    @BeforeEach
    void setUp() {
        product1 = new Product("Margherita Pizza", "Classic pizza", new BigDecimal("45.00"), "LANCHE");
        product1.setId("1");

        product2 = new Product("Pepperoni Pizza", "Spicy pizza", new BigDecimal("50.00"), "LANCHE");
        product2.setId("2");
    }

    @Test
    @DisplayName("Should find products by name successfully")
    void shouldFindProductsByName() {
        when(productRepository.findByNameContaining("Pizza"))
                .thenReturn(Arrays.asList(product1, product2));

        List<Product> result = useCase.execute("Pizza");

        assertThat(result).isNotNull();
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getName()).contains("Pizza");
        assertThat(result.get(1).getName()).contains("Pizza");
        verify(productRepository, times(1)).findByNameContaining("Pizza");
    }

    @Test
    @DisplayName("Should return empty list when no products found by name")
    void shouldReturnEmptyListWhenNoProductsFound() {
        when(productRepository.findByNameContaining("NonExistent"))
                .thenReturn(Collections.emptyList());

        List<Product> result = useCase.execute("NonExistent");

        assertThat(result).isNotNull();
        assertThat(result).isEmpty();
        verify(productRepository, times(1)).findByNameContaining("NonExistent");
    }

    @Test
    @DisplayName("Should find single product by name")
    void shouldFindSingleProductByName() {
        when(productRepository.findByNameContaining("Margherita"))
                .thenReturn(Collections.singletonList(product1));

        List<Product> result = useCase.execute("Margherita");

        assertThat(result).isNotNull();
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getName()).isEqualTo("Margherita Pizza");
        verify(productRepository, times(1)).findByNameContaining("Margherita");
    }

    @Test
    @DisplayName("Should throw exception when name is null")
    void shouldThrowExceptionWhenNameIsNull() {
        assertThatThrownBy(() -> useCase.execute(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Nome é obrigatório");

        verify(productRepository, never()).findByNameContaining(any());
    }

    @Test
    @DisplayName("Should throw exception when name is empty")
    void shouldThrowExceptionWhenNameIsEmpty() {
        assertThatThrownBy(() -> useCase.execute(""))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Nome é obrigatório");

        verify(productRepository, never()).findByNameContaining(any());
    }

    @Test
    @DisplayName("Should throw exception when name is blank")
    void shouldThrowExceptionWhenNameIsBlank() {
        assertThatThrownBy(() -> useCase.execute("   "))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Nome é obrigatório");

        verify(productRepository, never()).findByNameContaining(any());
    }

    @Test
    @DisplayName("Should handle partial name search")
    void shouldHandlePartialNameSearch() {
        when(productRepository.findByNameContaining("Burger"))
                .thenReturn(Collections.singletonList(product1));

        List<Product> result = useCase.execute("Burger");

        assertThat(result).isNotNull();
        assertThat(result).hasSize(1);
        verify(productRepository, times(1)).findByNameContaining("Burger");
    }

    @Test
    @DisplayName("Should search with special characters")
    void shouldSearchWithSpecialCharacters() {
        when(productRepository.findByNameContaining("X-Burger"))
                .thenReturn(Collections.emptyList());

        List<Product> result = useCase.execute("X-Burger");

        assertThat(result).isNotNull();
        assertThat(result).isEmpty();
        verify(productRepository, times(1)).findByNameContaining("X-Burger");
    }
}
