package com.fiap.techchallenge.productmicroservice.infrastructure.persistence;

import com.fiap.techchallenge.productmicroservice.domain.entities.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.math.BigDecimal;
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
        product = new Product("Test Product", "Description", new BigDecimal("25.90"), "LANCHE");
        product.setId("1");

        productDocument = new ProductDocument();
        productDocument.setId("1");
        productDocument.setName("Test Product");
        productDocument.setDescription("Description");
        productDocument.setPrice(new BigDecimal("25.90"));
        productDocument.setCategory("LANCHE");
        productDocument.setOnPromotion(false);
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
        when(productMongoRepository.findByCategory("LANCHE")).thenReturn(documents);
        when(modelMapper.map(productDocument, Product.class)).thenReturn(product);

        List<Product> products = productRepositoryAdapter.findByCategory("LANCHE");

        assertThat(products).hasSize(1);
        assertThat(products.get(0).getCategory()).isEqualTo("LANCHE");
        verify(productMongoRepository, times(1)).findByCategory("LANCHE");
    }

    @Test
    @DisplayName("Should find products on promotion successfully")
    void shouldFindProductsOnPromotionSuccessfully() {
        productDocument.setOnPromotion(true);
        productDocument.setPromotionPrice(new BigDecimal("19.90"));
        List<ProductDocument> documents = Arrays.asList(productDocument);
        when(productMongoRepository.findByOnPromotion(true)).thenReturn(documents);
        when(modelMapper.map(productDocument, Product.class)).thenReturn(product);

        List<Product> products = productRepositoryAdapter.findByOnPromotion(true);

        assertThat(products).hasSize(1);
        verify(productMongoRepository, times(1)).findByOnPromotion(true);
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
        BigDecimal minPrice = new BigDecimal("10.00");
        BigDecimal maxPrice = new BigDecimal("30.00");
        
        when(productMongoRepository.findByCategoryAndPriceBetween("LANCHE", minPrice, maxPrice))
                .thenReturn(documents);
        when(modelMapper.map(productDocument, Product.class)).thenReturn(product);

        List<Product> products = productRepositoryAdapter.findByCategoryAndPriceBetween("LANCHE", minPrice, maxPrice);

        assertThat(products).hasSize(1);
        verify(productMongoRepository, times(1))
                .findByCategoryAndPriceBetween("LANCHE", minPrice, maxPrice);
    }

    @Test
    @DisplayName("Should find products by category and price range manual successfully")
    void shouldFindProductsByCategoryAndPriceRangeManualSuccessfully() {
        List<ProductDocument> documents = Arrays.asList(productDocument);
        BigDecimal minPrice = new BigDecimal("10.00");
        BigDecimal maxPrice = new BigDecimal("30.00");
        
        when(productMongoRepository.findByCategoryAndPriceRangeManual("LANCHE", minPrice, maxPrice))
                .thenReturn(documents);
        when(modelMapper.map(productDocument, Product.class)).thenReturn(product);

        List<Product> products = productRepositoryAdapter
                .findByCategoryAndPriceRangeManual("LANCHE", minPrice, maxPrice);

        assertThat(products).hasSize(1);
        verify(productMongoRepository, times(1))
                .findByCategoryAndPriceRangeManual("LANCHE", minPrice, maxPrice);
    }
}
