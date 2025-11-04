package com.fiap.techchallenge.productmicroservice.application.dto;

import com.fiap.techchallenge.productmicroservice.domain.entities.CategoryEnum;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

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
    @DisplayName("Should create DTO with all parameters")
    void shouldCreateDTOWithAllParameters() {
        LocalDateTime now = LocalDateTime.now();
        ProductResponseDTO dto = new ProductResponseDTO(
                "1",
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

        assertThat(dto.getId()).isEqualTo("1");
        assertThat(dto.getName()).isEqualTo("Test Product");
        assertThat(dto.getDescription()).isEqualTo("Test Description");
        assertThat(dto.getImage()).isEqualTo("http://image.url");
        assertThat(dto.getPrice()).isEqualTo(2590L);
        assertThat(dto.getPriceForClient()).isEqualTo(2000L);
        assertThat(dto.getCategory()).isEqualTo(CategoryEnum.LANCHE);
        assertThat(dto.getQuantity()).isEqualTo(10L);
        assertThat(dto.getCreatedAt()).isEqualTo(now);
        assertThat(dto.getUpdatedAt()).isEqualTo(now);
    }

    @Test
    @DisplayName("Should set and get all fields correctly")
    void shouldSetAndGetAllFieldsCorrectly() {
        ProductResponseDTO dto = new ProductResponseDTO();
        LocalDateTime now = LocalDateTime.now();

        dto.setId("123");
        dto.setName("Product Name");
        dto.setDescription("Product Description");
        dto.setImage("http://image.url");
        dto.setPrice(5000L);
        dto.setPriceForClient(4500L);
        dto.setCategory(CategoryEnum.BEBIDA);
        dto.setQuantity(20L);
        dto.setCreatedAt(now);
        dto.setUpdatedAt(now);

        assertThat(dto.getId()).isEqualTo("123");
        assertThat(dto.getName()).isEqualTo("Product Name");
        assertThat(dto.getDescription()).isEqualTo("Product Description");
        assertThat(dto.getImage()).isEqualTo("http://image.url");
        assertThat(dto.getPrice()).isEqualTo(5000L);
        assertThat(dto.getPriceForClient()).isEqualTo(4500L);
        assertThat(dto.getCategory()).isEqualTo(CategoryEnum.BEBIDA);
        assertThat(dto.getQuantity()).isEqualTo(20L);
        assertThat(dto.getCreatedAt()).isEqualTo(now);
        assertThat(dto.getUpdatedAt()).isEqualTo(now);
    }

    @Test
    @DisplayName("Should handle null optional fields")
    void shouldHandleNullOptionalFields() {
        ProductResponseDTO dto = new ProductResponseDTO();
        dto.setDescription(null);
        dto.setImage(null);
        dto.setPriceForClient(null);

        assertThat(dto.getDescription()).isNull();
        assertThat(dto.getImage()).isNull();
        assertThat(dto.getPriceForClient()).isNull();
    }

    @Test
    @DisplayName("Should handle category enum")
    void shouldHandleCategoryEnum() {
        ProductResponseDTO dto = new ProductResponseDTO();
        dto.setCategory(CategoryEnum.SOBREMESA);

        assertThat(dto.getCategory()).isEqualTo(CategoryEnum.SOBREMESA);
    }

    @Test
    @DisplayName("Should handle zero quantity")
    void shouldHandleZeroQuantity() {
        ProductResponseDTO dto = new ProductResponseDTO();
        dto.setQuantity(0L);

        assertThat(dto.getQuantity()).isEqualTo(0L);
    }
}
