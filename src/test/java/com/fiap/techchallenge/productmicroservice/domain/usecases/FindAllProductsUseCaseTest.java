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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FindAllProductsUseCaseTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private FindAllProductsUseCase findAllProductsUseCase;

    private Product product1;
    private Product product2;

    @BeforeEach
    void setUp() {
        product1 = new Product();
        product1.setId("1");
        product1.setName("Product 1");
        product1.setPrice(1000L);
        product1.setCategory(CategoryEnum.LANCHE);

        product2 = new Product();
        product2.setId("2");
        product2.setName("Product 2");
        product2.setPrice(2000L);
        product2.setCategory(CategoryEnum.BEBIDA);
    }

    @Test
    void shouldFindAllProducts() {
        List<Product> expectedProducts = Arrays.asList(product1, product2);
        when(productRepository.findAll()).thenReturn(expectedProducts);

        List<Product> result = findAllProductsUseCase.execute();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Product 1", result.get(0).getName());
        assertEquals("Product 2", result.get(1).getName());
        verify(productRepository, times(1)).findAll();
    }

    @Test
    void shouldReturnEmptyListWhenNoProducts() {
        when(productRepository.findAll()).thenReturn(Collections.emptyList());
        List<Product> result = findAllProductsUseCase.execute();
        assertTrue(result.isEmpty());
        verify(productRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Should handle large list of products")
    void shouldHandleLargeListOfProducts() {
        List<Product> largeList = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            Product p = new Product();
            p.setId("id-" + i);
            p.setName("Product " + i);
            largeList.add(p);
        }
        when(productRepository.findAll()).thenReturn(largeList);
        
        List<Product> result = findAllProductsUseCase.execute();
        
        assertThat(result).hasSize(100);
        verify(productRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Should propagate repository exception")
    void shouldPropagateRepositoryException() {
        when(productRepository.findAll()).thenThrow(new RuntimeException("Database error"));
        
        assertThatThrownBy(() -> findAllProductsUseCase.execute())
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Database error");
        
        verify(productRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Should return products with all different categories")
    void shouldReturnProductsWithAllDifferentCategories() {
        List<Product> products = Arrays.asList(
            createProduct("1", "Product 1", CategoryEnum.LANCHE),
            createProduct("2", "Product 2", CategoryEnum.BEBIDA),
            createProduct("3", "Product 3", CategoryEnum.ACOMPANHAMENTO),
            createProduct("4", "Product 4", CategoryEnum.SOBREMESA)
        );
        when(productRepository.findAll()).thenReturn(products);
        
        List<Product> result = findAllProductsUseCase.execute();
        
        assertThat(result).hasSize(4);
        assertThat(result).extracting(Product::getCategory)
                .containsExactlyInAnyOrder(
                    CategoryEnum.LANCHE, 
                    CategoryEnum.BEBIDA, 
                    CategoryEnum.ACOMPANHAMENTO, 
                    CategoryEnum.SOBREMESA
                );
        verify(productRepository, times(1)).findAll();
    }

    private Product createProduct(String id, String name, CategoryEnum category) {
        Product product = new Product();
        product.setId(id);
        product.setName(name);
        product.setCategory(category);
        product.setPrice(1000L);
        return product;
    }
}
