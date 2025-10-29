package com.fiap.techchallenge.productmicroservice.domain.usecases;

import com.fiap.techchallenge.productmicroservice.domain.entities.Product;
import com.fiap.techchallenge.productmicroservice.domain.repositories.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.math.BigDecimal;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DeleteProductByIdUseCaseTest {
    @Mock
    private ProductRepository productRepository;
    @InjectMocks
    private DeleteProductByIdUseCase useCase;
    
    private Product product;

    @BeforeEach
    void setUp() {
        product = new Product();
        product.setId("123");
        product.setName("Test Product");
        product.setPrice(new BigDecimal("25.90"));
    }

    @Test
    void shouldDeleteProductById() {
        when(productRepository.findById("123")).thenReturn(Optional.of(product));
        doNothing().when(productRepository).deleteById("123");
        
        useCase.execute("123");
        
        verify(productRepository, times(1)).findById("123");
        verify(productRepository, times(1)).deleteById("123");
    }
    
    @Test
    void shouldThrowExceptionWhenProductNotFound() {
        when(productRepository.findById("999")).thenReturn(Optional.empty());
        
        assertThrows(IllegalArgumentException.class, () -> useCase.execute("999"));
        verify(productRepository, never()).deleteById(anyString());
    }
    
    @Test
    void shouldThrowExceptionWhenIdIsNull() {
        assertThrows(IllegalArgumentException.class, () -> useCase.execute(null));
        verify(productRepository, never()).deleteById(anyString());
    }
    
    @Test
    void shouldThrowExceptionWhenIdIsEmpty() {
        assertThrows(IllegalArgumentException.class, () -> useCase.execute(""));
        verify(productRepository, never()).deleteById(anyString());
    }
}
