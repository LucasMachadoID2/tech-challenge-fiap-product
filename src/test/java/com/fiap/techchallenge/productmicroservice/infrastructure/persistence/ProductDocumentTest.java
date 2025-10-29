package com.fiap.techchallenge.productmicroservice.infrastructure.persistence;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
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
                new BigDecimal("25.90"),
                "LANCHE",
                true,
                new BigDecimal("19.90"),
                now,
                now
        );

        assertThat(document.getName()).isEqualTo("Test Product");
        assertThat(document.getDescription()).isEqualTo("Test Description");
        assertThat(document.getPrice()).isEqualByComparingTo(new BigDecimal("25.90"));
        assertThat(document.getCategory()).isEqualTo("LANCHE");
        assertThat(document.isOnPromotion()).isTrue();
        assertThat(document.getPromotionPrice()).isEqualByComparingTo(new BigDecimal("19.90"));
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
        document.setPrice(new BigDecimal("50.00"));
        document.setCategory("BEBIDA");
        document.setOnPromotion(true);
        document.setPromotionPrice(new BigDecimal("40.00"));
        document.setCreatedAt(now);
        document.setUpdatedAt(now);

        assertThat(document.getId()).isEqualTo("123");
        assertThat(document.getName()).isEqualTo("Product Name");
        assertThat(document.getDescription()).isEqualTo("Product Description");
        assertThat(document.getPrice()).isEqualByComparingTo(new BigDecimal("50.00"));
        assertThat(document.getCategory()).isEqualTo("BEBIDA");
        assertThat(document.isOnPromotion()).isTrue();
        assertThat(document.getPromotionPrice()).isEqualByComparingTo(new BigDecimal("40.00"));
        assertThat(document.getCreatedAt()).isEqualTo(now);
        assertThat(document.getUpdatedAt()).isEqualTo(now);
    }

    @Test
    @DisplayName("Should handle null promotion price")
    void shouldHandleNullPromotionPrice() {
        ProductDocument document = new ProductDocument();
        document.setPromotionPrice(null);

        assertThat(document.getPromotionPrice()).isNull();
    }

    @Test
    @DisplayName("Should set onPromotion to false")
    void shouldSetOnPromotionToFalse() {
        ProductDocument document = new ProductDocument();
        document.setOnPromotion(false);

        assertThat(document.isOnPromotion()).isFalse();
    }
}
