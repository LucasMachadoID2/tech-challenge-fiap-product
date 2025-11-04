package com.fiap.techchallenge.productmicroservice.infrastructure.persistence;

import com.fiap.techchallenge.productmicroservice.domain.entities.CategoryEnum;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class ProductDocumentTest {

    @Test
    @DisplayName("Should create ProductDocument with default constructor")
    void shouldCreateProductDocumentWithDefaultConstructor() {
        ProductDocument document = new ProductDocument();
        
        assertThat(document).isNotNull();
        assertThat(document.getId()).isNull();
    }

    @Test
    @DisplayName("Should create ProductDocument with all parameters")
    void shouldCreateProductDocumentWithAllParameters() {
        LocalDateTime now = LocalDateTime.now();
        ProductDocument document = new ProductDocument(
                "Test Product",
                "Test Description",
                "http://image.url",
                2590L,
                2000L,
                CategoryEnum.LANCHE,
                10L,
                now,
                now
        );

        assertThat(document.getName()).isEqualTo("Test Product");
        assertThat(document.getDescription()).isEqualTo("Test Description");
        assertThat(document.getImage()).isEqualTo("http://image.url");
        assertThat(document.getPrice()).isEqualTo(2590L);
        assertThat(document.getPriceForClient()).isEqualTo(2000L);
        assertThat(document.getCategory()).isEqualTo(CategoryEnum.LANCHE);
        assertThat(document.getQuantity()).isEqualTo(10L);
        assertThat(document.getCreatedAt()).isEqualTo(now);
        assertThat(document.getUpdatedAt()).isEqualTo(now);
    }

    @Test
    @DisplayName("Should set and get all fields correctly")
    void shouldSetAndGetAllFieldsCorrectly() {
        ProductDocument document = new ProductDocument();
        LocalDateTime now = LocalDateTime.now();

        document.setId("123");
        document.setName("Product Name");
        document.setDescription("Product Description");
        document.setImage("http://image.url");
        document.setPrice(5000L);
        document.setPriceForClient(4500L);
        document.setCategory(CategoryEnum.BEBIDA);
        document.setQuantity(20L);
        document.setCreatedAt(now);
        document.setUpdatedAt(now);

        assertThat(document.getId()).isEqualTo("123");
        assertThat(document.getName()).isEqualTo("Product Name");
        assertThat(document.getDescription()).isEqualTo("Product Description");
        assertThat(document.getImage()).isEqualTo("http://image.url");
        assertThat(document.getPrice()).isEqualTo(5000L);
        assertThat(document.getPriceForClient()).isEqualTo(4500L);
        assertThat(document.getCategory()).isEqualTo(CategoryEnum.BEBIDA);
        assertThat(document.getQuantity()).isEqualTo(20L);
        assertThat(document.getCreatedAt()).isEqualTo(now);
        assertThat(document.getUpdatedAt()).isEqualTo(now);
    }

    @Test
    @DisplayName("Should handle null optional fields")
    void shouldHandleNullOptionalFields() {
        ProductDocument document = new ProductDocument();
        document.setImage(null);
        document.setPriceForClient(null);
        document.setQuantity(null);

        assertThat(document.getImage()).isNull();
        assertThat(document.getPriceForClient()).isNull();
        assertThat(document.getQuantity()).isNull();
    }

    @Test
    @DisplayName("Should handle CategoryEnum")
    void shouldHandleCategoryEnum() {
        ProductDocument document = new ProductDocument();
        document.setCategory(CategoryEnum.SOBREMESA);

        assertThat(document.getCategory()).isEqualTo(CategoryEnum.SOBREMESA);
    }
}
