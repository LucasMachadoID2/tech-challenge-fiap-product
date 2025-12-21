package com.fiap.techchallenge.productmicroservice.infrastructure.config;

import com.fiap.techchallenge.productmicroservice.domain.entities.Product;
import com.fiap.techchallenge.productmicroservice.domain.repositories.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.CommandLineRunner;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

class DataLoaderTest {

    @Mock
    private ProductRepository productRepository;

    private DataLoader dataLoader;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        dataLoader = new DataLoader();
    }

    @Test
    void shouldPopulateDatabaseWhenEmpty() throws Exception {
        // Arrange
        when(productRepository.findAll()).thenReturn(Collections.emptyList());

        // Act
        CommandLineRunner runner = dataLoader.initDatabase(productRepository);
        runner.run();

        // Assert
        verify(productRepository, times(1)).findAll();
        verify(productRepository, times(16)).save(any(Product.class));
    }

    @Test
    void shouldNotPopulateDatabaseWhenAlreadyHasProducts() throws Exception {
        // Arrange
        List<Product> existingProducts = List.of(
            new Product("Test Product", "Description", "image.jpg", 1000L, 1200L, 
                       com.fiap.techchallenge.productmicroservice.domain.entities.CategoryEnum.LANCHE, 10L)
        );
        when(productRepository.findAll()).thenReturn(existingProducts);

        // Act
        CommandLineRunner runner = dataLoader.initDatabase(productRepository);
        runner.run();

        // Assert
        verify(productRepository, times(1)).findAll();
        verify(productRepository, never()).save(any(Product.class));
    }

    @Test
    void shouldCreateCommandLineRunnerBean() {
        // Act
        CommandLineRunner runner = dataLoader.initDatabase(productRepository);

        // Assert
        assertNotNull(runner);
    }

    @Test
    void shouldPopulateWithCorrectNumberOfProducts() throws Exception {
        // Arrange
        when(productRepository.findAll()).thenReturn(Collections.emptyList());

        // Act
        CommandLineRunner runner = dataLoader.initDatabase(productRepository);
        runner.run();

        // Assert
        verify(productRepository, times(16)).save(any(Product.class));
    }
}
