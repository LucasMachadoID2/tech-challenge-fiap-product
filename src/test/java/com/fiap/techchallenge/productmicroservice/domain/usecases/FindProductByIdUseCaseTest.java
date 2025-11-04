package com.fiap.techchallenge.productmicroservice.domain.usecases;

import com.fiap.techchallenge.productmicroservice.domain.entities.CategoryEnum;
import com.fiap.techchallenge.productmicroservice.domain.entities.Product;
import com.fiap.techchallenge.productmicroservice.domain.repositories.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FindProductByIdUseCaseTest {
    @Mock
    private ProductRepository productRepository;
    @InjectMocks
    private FindProductByIdUseCase findProductByIdUseCase;
    private Product product;

    @BeforeEach
    void setUp() {
        product = new Product();
        product.setId("123");
        product.setName("Test Product");
        product.setPrice(2590L);
        product.setCategory(CategoryEnum.LANCHE);
    }

    @Test
    void shouldFindProductById() {
        when(productRepository.findById("123")).thenReturn(Optional.of(product));
        Optional<Product> result = findProductByIdUseCase.execute("123");
        assertTrue(result.isPresent());
        assertEquals("123", result.get().getId());
        verify(productRepository, times(1)).findById("123");
    }

    @Test
    void shouldReturnEmptyWhenProductNotFound() {
        when(productRepository.findById("999")).thenReturn(Optional.empty());
        Optional<Product> result = findProductByIdUseCase.execute("999");
        assertFalse(result.isPresent());
    }
}
