package com.fiap.techchallenge.productmicroservice.application.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class ProductResponseDTOTest {

    @Test
    @DisplayName("Should create DTO with default constructor")
    void shouldCreateDTOWithDefaultConstructor() {
        ProductResponseDTO dto = new ProductResponseDTO();

        assertThat(dto).isNotNull();
    }

    @Test
    @DisplayName("Should create DTO with all parameters and calculate effective price when not on promotion")
    void shouldCreateDTOWithAllParametersWhenNotOnPromotion() {
        LocalDateTime now = LocalDateTime.now();
        ProductResponseDTO dto = new ProductResponseDTO(
                "1",
                "Test Product",
                "Test Description",
                new BigDecimal("25.90"),
                "LANCHE",
                false,
                null,
                now,
                now
        );

        assertThat(dto.getId()).isEqualTo("1");
        assertThat(dto.getName()).isEqualTo("Test Product");
        assertThat(dto.getDescription()).isEqualTo("Test Description");
        assertThat(dto.getPrice()).isEqualByComparingTo(new BigDecimal("25.90"));
        assertThat(dto.getCategory()).isEqualTo("LANCHE");
        assertThat(dto.isOnPromotion()).isFalse();
        assertThat(dto.getPromotionPrice()).isNull();
        assertThat(dto.getEffectivePrice()).isEqualByComparingTo(new BigDecimal("25.90"));
        assertThat(dto.getCreatedAt()).isEqualTo(now);
        assertThat(dto.getUpdatedAt()).isEqualTo(now);
    }

    @Test
    @DisplayName("Should create DTO with promotion and calculate effective price as promotion price")
    void shouldCreateDTOWithPromotionAndCalculateEffectivePriceAsPromotionPrice() {
        LocalDateTime now = LocalDateTime.now();
        ProductResponseDTO dto = new ProductResponseDTO(
                "1",
                "Test Product",
                "Test Description",
                new BigDecimal("50.00"),
                "LANCHE",
                true,
                new BigDecimal("35.00"),
                now,
                now
        );

        assertThat(dto.isOnPromotion()).isTrue();
        assertThat(dto.getPromotionPrice()).isEqualByComparingTo(new BigDecimal("35.00"));
        assertThat(dto.getEffectivePrice()).isEqualByComparingTo(new BigDecimal("35.00"));
    }

    @Test
    @DisplayName("Should set and get all fields correctly")
    void shouldSetAndGetAllFieldsCorrectly() {
        ProductResponseDTO dto = new ProductResponseDTO();
        LocalDateTime now = LocalDateTime.now();

        dto.setId("123");
        dto.setName("Product Name");
        dto.setDescription("Product Description");
        dto.setPrice(new BigDecimal("50.00"));
        dto.setCategory("BEBIDA");
        dto.setOnPromotion(true);
        dto.setPromotionPrice(new BigDecimal("40.00"));
        dto.setEffectivePrice(new BigDecimal("40.00"));
        dto.setCreatedAt(now);
        dto.setUpdatedAt(now);

        assertThat(dto.getId()).isEqualTo("123");
        assertThat(dto.getName()).isEqualTo("Product Name");
        assertThat(dto.getDescription()).isEqualTo("Product Description");
        assertThat(dto.getPrice()).isEqualByComparingTo(new BigDecimal("50.00"));
        assertThat(dto.getCategory()).isEqualTo("BEBIDA");
        assertThat(dto.isOnPromotion()).isTrue();
        assertThat(dto.getPromotionPrice()).isEqualByComparingTo(new BigDecimal("40.00"));
        assertThat(dto.getEffectivePrice()).isEqualByComparingTo(new BigDecimal("40.00"));
        assertThat(dto.getCreatedAt()).isEqualTo(now);
        assertThat(dto.getUpdatedAt()).isEqualTo(now);
    }

    @Test
    @DisplayName("Should handle null promotion price")
    void shouldHandleNullPromotionPrice() {
        ProductResponseDTO dto = new ProductResponseDTO();
        dto.setPromotionPrice(null);

        assertThat(dto.getPromotionPrice()).isNull();
    }

    @Test
    @DisplayName("Should set onPromotion to false")
    void shouldSetOnPromotionToFalse() {
        ProductResponseDTO dto = new ProductResponseDTO();
        dto.setOnPromotion(false);

        assertThat(dto.isOnPromotion()).isFalse();
    }

    @Test
    @DisplayName("Should handle null effective price when promotion price is null")
    void shouldHandleNullEffectivePriceWhenPromotionPriceIsNull() {
        LocalDateTime now = LocalDateTime.now();
        ProductResponseDTO dto = new ProductResponseDTO(
                "1",
                "Test Product",
                "Test Description",
                new BigDecimal("50.00"),
                "LANCHE",
                true,
                null,
                now,
                now
        );

        assertThat(dto.getEffectivePrice()).isEqualByComparingTo(new BigDecimal("50.00"));
    }
}
