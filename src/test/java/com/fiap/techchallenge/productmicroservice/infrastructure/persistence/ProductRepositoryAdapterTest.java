package com.fiap.techchallenge.productmicroservice.infrastructure.persistence;

import com.fiap.techchallenge.productmicroservice.domain.entities.CategoryEnum;
import com.fiap.techchallenge.productmicroservice.domain.entities.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductRepositoryAdapterTest {

    @Mock
    private ProductMongoRepository productMongoRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private ProductRepositoryAdapter productRepositoryAdapter;

    private Product product;
    private ProductDocument productDocument;

    @BeforeEach
    void setUp() {
        product = new Product("Test Product", "Description", "image.url", 2590L, 2000L, CategoryEnum.LANCHE, 10L);
        product.setId("1");

        productDocument = new ProductDocument();
        productDocument.setId("1");
        productDocument.setName("Test Product");
        productDocument.setDescription("Description");
        productDocument.setImage("image.url");
        productDocument.setPrice(2590L);
        productDocument.setPriceForClient(2000L);
        productDocument.setCategory(CategoryEnum.LANCHE);
        productDocument.setQuantity(10L);
        productDocument.setCreatedAt(LocalDateTime.now());
        productDocument.setUpdatedAt(LocalDateTime.now());
    }

    @Test
    @DisplayName("Should save product successfully")
    void shouldSaveProductSuccessfully() {
        when(modelMapper.map(product, ProductDocument.class)).thenReturn(productDocument);
        when(productMongoRepository.save(productDocument)).thenReturn(productDocument);
        when(modelMapper.map(productDocument, Product.class)).thenReturn(product);

        Product savedProduct = productRepositoryAdapter.save(product);

        assertThat(savedProduct).isNotNull();
        assertThat(savedProduct.getId()).isEqualTo("1");
        verify(productMongoRepository, times(1)).save(productDocument);
    }

    @Test
    @DisplayName("Should find product by id successfully")
    void shouldFindProductByIdSuccessfully() {
        when(productMongoRepository.findById("1")).thenReturn(Optional.of(productDocument));
        when(modelMapper.map(productDocument, Product.class)).thenReturn(product);

        Optional<Product> foundProduct = productRepositoryAdapter.findById("1");

        assertThat(foundProduct).isPresent();
        assertThat(foundProduct.get().getId()).isEqualTo("1");
        verify(productMongoRepository, times(1)).findById("1");
    }

    @Test
    @DisplayName("Should return empty when product not found by id")
    void shouldReturnEmptyWhenProductNotFoundById() {
        when(productMongoRepository.findById("999")).thenReturn(Optional.empty());

        Optional<Product> foundProduct = productRepositoryAdapter.findById("999");

        assertThat(foundProduct).isEmpty();
        verify(productMongoRepository, times(1)).findById("999");
    }

    @Test
    @DisplayName("Should find all products successfully")
    void shouldFindAllProductsSuccessfully() {
        List<ProductDocument> documents = Arrays.asList(productDocument);
        when(productMongoRepository.findAll()).thenReturn(documents);
        when(modelMapper.map(productDocument, Product.class)).thenReturn(product);

        List<Product> products = productRepositoryAdapter.findAll();

        assertThat(products).hasSize(1);
        assertThat(products.get(0).getId()).isEqualTo("1");
        verify(productMongoRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Should delete product by id successfully")
    void shouldDeleteProductByIdSuccessfully() {
        doNothing().when(productMongoRepository).deleteById("1");

        productRepositoryAdapter.deleteById("1");

        verify(productMongoRepository, times(1)).deleteById("1");
    }

    @Test
    @DisplayName("Should find products by category successfully")
    void shouldFindProductsByCategorySuccessfully() {
        List<ProductDocument> documents = Arrays.asList(productDocument);
        when(productMongoRepository.findByCategory(CategoryEnum.LANCHE)).thenReturn(documents);
        when(modelMapper.map(productDocument, Product.class)).thenReturn(product);

        List<Product> products = productRepositoryAdapter.findByCategory(CategoryEnum.LANCHE);

        assertThat(products).hasSize(1);
        assertThat(products.get(0).getCategory()).isEqualTo(CategoryEnum.LANCHE);
        verify(productMongoRepository, times(1)).findByCategory(CategoryEnum.LANCHE);
    }

    @Test
    @DisplayName("Should find products by name containing successfully")
    void shouldFindProductsByNameContainingSuccessfully() {
        List<ProductDocument> documents = Arrays.asList(productDocument);
        when(productMongoRepository.findByNameContainingIgnoreCase("Test")).thenReturn(documents);
        when(modelMapper.map(productDocument, Product.class)).thenReturn(product);

        List<Product> products = productRepositoryAdapter.findByNameContaining("Test");

        assertThat(products).hasSize(1);
        assertThat(products.get(0).getName()).contains("Test");
        verify(productMongoRepository, times(1)).findByNameContainingIgnoreCase("Test");
    }

    @Test
    @DisplayName("Should find products by category and price between successfully")
    void shouldFindProductsByCategoryAndPriceBetweenSuccessfully() {
        List<ProductDocument> documents = Arrays.asList(productDocument);
        Long minPrice = 1000L;
        Long maxPrice = 3000L;
        
        when(productMongoRepository.findByCategoryAndPriceBetween(CategoryEnum.LANCHE, minPrice, maxPrice))
                .thenReturn(documents);
        when(modelMapper.map(productDocument, Product.class)).thenReturn(product);

        List<Product> products = productRepositoryAdapter.findByCategoryAndPriceBetween(CategoryEnum.LANCHE, minPrice, maxPrice);

        assertThat(products).hasSize(1);
        verify(productMongoRepository, times(1))
                .findByCategoryAndPriceBetween(CategoryEnum.LANCHE, minPrice, maxPrice);
    }
}
