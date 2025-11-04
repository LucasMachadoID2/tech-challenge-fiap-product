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
        product1 = new Product("Burger", "Delicious burger", "image.url", 2500L, 2000L, CategoryEnum.LANCHE, 10L);
        product1.setId("1");
        
        product2 = new Product("Hot Dog", "Classic hot dog", "image.url", 1500L, 1200L, CategoryEnum.LANCHE, 15L);
        product2.setId("2");
    }

    @Test
    @DisplayName("Should find products by category successfully")
    void shouldFindProductsByCategory() {
        when(productRepository.findByCategory(CategoryEnum.LANCHE))
                .thenReturn(Arrays.asList(product1, product2));
        
        List<Product> result = useCase.execute(CategoryEnum.LANCHE);
        
        assertThat(result).isNotNull();
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getCategory()).isEqualTo(CategoryEnum.LANCHE);
        assertThat(result.get(1).getCategory()).isEqualTo(CategoryEnum.LANCHE);
        verify(productRepository, times(1)).findByCategory(CategoryEnum.LANCHE);
    }

    @Test
    @DisplayName("Should return empty list when no products found in category")
    void shouldReturnEmptyListWhenNoProductsFound() {
        when(productRepository.findByCategory(CategoryEnum.SOBREMESA))
                .thenReturn(Collections.emptyList());
        
        List<Product> result = useCase.execute(CategoryEnum.SOBREMESA);
        
        assertThat(result).isNotNull();
        assertThat(result).isEmpty();
        verify(productRepository, times(1)).findByCategory(CategoryEnum.SOBREMESA);
    }

    @Test
    @DisplayName("Should find single product in category")
    void shouldFindSingleProductInCategory() {
        when(productRepository.findByCategory(CategoryEnum.BEBIDA))
                .thenReturn(Collections.singletonList(product1));
        
        List<Product> result = useCase.execute(CategoryEnum.BEBIDA);
        
        assertThat(result).isNotNull();
        assertThat(result).hasSize(1);
        verify(productRepository, times(1)).findByCategory(CategoryEnum.BEBIDA);
    }

    @Test
    @DisplayName("Should handle different category names")
    void shouldHandleDifferentCategoryNames() {
        CategoryEnum[] categories = {CategoryEnum.LANCHE, CategoryEnum.BEBIDA, CategoryEnum.ACOMPANHAMENTO, CategoryEnum.SOBREMESA};
        
        for (CategoryEnum category : categories) {
            when(productRepository.findByCategory(category))
                    .thenReturn(Collections.singletonList(product1));
            
            List<Product> result = useCase.execute(category);
            
            assertThat(result).isNotNull();
            assertThat(result).hasSize(1);
        }
        
        verify(productRepository, times(4)).findByCategory(any(CategoryEnum.class));
    }
}
