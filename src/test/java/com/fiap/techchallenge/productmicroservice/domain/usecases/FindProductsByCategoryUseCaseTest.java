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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FindProductsByCategoryUseCaseTest {
    
    @Mock
    private ProductRepository productRepository;
    
    @InjectMocks
    private FindProductsByCategoryUseCase useCase;

    private Product product1;
    private Product product2;

    @BeforeEach
    void setUp() {
        product1 = new Product("Burger", "Delicious burger", new BigDecimal("25.00"), "LANCHE");
        product1.setId("1");
        
        product2 = new Product("Hot Dog", "Classic hot dog", new BigDecimal("15.00"), "LANCHE");
        product2.setId("2");
    }

    @Test
    @DisplayName("Should find products by category successfully")
    void shouldFindProductsByCategory() {
        when(productRepository.findByCategory("LANCHE"))
                .thenReturn(Arrays.asList(product1, product2));
        
        List<Product> result = useCase.execute("LANCHE");
        
        assertThat(result).isNotNull();
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getCategory()).isEqualTo("LANCHE");
        assertThat(result.get(1).getCategory()).isEqualTo("LANCHE");
        verify(productRepository, times(1)).findByCategory("LANCHE");
    }

    @Test
    @DisplayName("Should return empty list when no products found in category")
    void shouldReturnEmptyListWhenNoProductsFound() {
        when(productRepository.findByCategory("SOBREMESA"))
                .thenReturn(Collections.emptyList());
        
        List<Product> result = useCase.execute("SOBREMESA");
        
        assertThat(result).isNotNull();
        assertThat(result).isEmpty();
        verify(productRepository, times(1)).findByCategory("SOBREMESA");
    }

    @Test
    @DisplayName("Should find single product in category")
    void shouldFindSingleProductInCategory() {
        when(productRepository.findByCategory("BEBIDA"))
                .thenReturn(Collections.singletonList(product1));
        
        List<Product> result = useCase.execute("BEBIDA");
        
        assertThat(result).isNotNull();
        assertThat(result).hasSize(1);
        verify(productRepository, times(1)).findByCategory("BEBIDA");
    }

    @Test
    @DisplayName("Should handle different category names")
    void shouldHandleDifferentCategoryNames() {
        String[] categories = {"LANCHE", "BEBIDA", "ACOMPANHAMENTO", "SOBREMESA"};
        
        for (String category : categories) {
            when(productRepository.findByCategory(category))
                    .thenReturn(Collections.singletonList(product1));
            
            List<Product> result = useCase.execute(category);
            
            assertThat(result).isNotNull();
            assertThat(result).hasSize(1);
        }
        
        verify(productRepository, times(4)).findByCategory(anyString());
    }
}
