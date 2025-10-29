package com.fiap.techchallenge.productmicroservice.application.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

class CreateProductRequestDTOTest {

    @Test
    @DisplayName("Should create DTO with default constructor")
    void shouldCreateDTOWithDefaultConstructor() {
        CreateProductRequestDTO dto = new CreateProductRequestDTO();

        assertThat(dto).isNotNull();
        assertThat(dto.isOnPromotion()).isFalse();
    }

    @Test
    @DisplayName("Should create DTO with parameterized constructor")
    void shouldCreateDTOWithParameterizedConstructor() {
        CreateProductRequestDTO dto = new CreateProductRequestDTO(
                "Test Product",
                "Test Description",
                new BigDecimal("25.90"),
                "LANCHE"
        );

        assertThat(dto.getName()).isEqualTo("Test Product");
        assertThat(dto.getDescription()).isEqualTo("Test Description");
        assertThat(dto.getPrice()).isEqualByComparingTo(new BigDecimal("25.90"));
        assertThat(dto.getCategory()).isEqualTo("LANCHE");
    }

    @Test
    @DisplayName("Should set and get all fields correctly")
    void shouldSetAndGetAllFieldsCorrectly() {
        CreateProductRequestDTO dto = new CreateProductRequestDTO();

        dto.setName("Product Name");
        dto.setDescription("Product Description");
        dto.setPrice(new BigDecimal("50.00"));
        dto.setCategory("BEBIDA");
        dto.setOnPromotion(true);
        dto.setPromotionPrice(new BigDecimal("40.00"));

        assertThat(dto.getName()).isEqualTo("Product Name");
        assertThat(dto.getDescription()).isEqualTo("Product Description");
        assertThat(dto.getPrice()).isEqualByComparingTo(new BigDecimal("50.00"));
        assertThat(dto.getCategory()).isEqualTo("BEBIDA");
        assertThat(dto.isOnPromotion()).isTrue();
        assertThat(dto.getPromotionPrice()).isEqualByComparingTo(new BigDecimal("40.00"));
    }

    @Test
    @DisplayName("Should handle null promotion price")
    void shouldHandleNullPromotionPrice() {
        CreateProductRequestDTO dto = new CreateProductRequestDTO();
        dto.setPromotionPrice(null);

        assertThat(dto.getPromotionPrice()).isNull();
    }

    @Test
    @DisplayName("Should handle null description")
    void shouldHandleNullDescription() {
        CreateProductRequestDTO dto = new CreateProductRequestDTO();
        dto.setDescription(null);

        assertThat(dto.getDescription()).isNull();
    }

    @Test
    @DisplayName("Should set onPromotion to false by default")
    void shouldSetOnPromotionToFalseByDefault() {
        CreateProductRequestDTO dto = new CreateProductRequestDTO();

        assertThat(dto.isOnPromotion()).isFalse();
    }

    @Test
    @DisplayName("Should allow setting onPromotion to true")
    void shouldAllowSettingOnPromotionToTrue() {
        CreateProductRequestDTO dto = new CreateProductRequestDTO();
        dto.setOnPromotion(true);

        assertThat(dto.isOnPromotion()).isTrue();
    }
}
