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
        product.setPrice(2590L);
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

    @Test
    @DisplayName("Should throw exception when id is blank with spaces")
    void shouldThrowExceptionWhenIdIsBlankWithSpaces() {
        assertThrows(IllegalArgumentException.class, () -> useCase.execute("   "));
        verify(productRepository, never()).findById(anyString());
        verify(productRepository, never()).deleteById(anyString());
    }

    @Test
    @DisplayName("Should successfully delete product with special characters in id")
    void shouldSuccessfullyDeleteProductWithSpecialCharactersInId() {
        String specialId = "abc-123_XYZ@456";
        Product specialProduct = new Product();
        specialProduct.setId(specialId);
        
        when(productRepository.findById(specialId)).thenReturn(Optional.of(specialProduct));
        doNothing().when(productRepository).deleteById(specialId);
        
        useCase.execute(specialId);
        
        verify(productRepository, times(1)).findById(specialId);
        verify(productRepository, times(1)).deleteById(specialId);
    }

    @Test
    @DisplayName("Should propagate repository exception on delete")
    void shouldPropagateRepositoryExceptionOnDelete() {
        when(productRepository.findById("123")).thenReturn(Optional.of(product));
        doThrow(new RuntimeException("Database error")).when(productRepository).deleteById("123");
        
        assertThrows(RuntimeException.class, () -> useCase.execute("123"));
        
        verify(productRepository, times(1)).findById("123");
        verify(productRepository, times(1)).deleteById("123");
    }
}
