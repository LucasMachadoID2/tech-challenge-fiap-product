package com.fiap.techchallenge.productmicroservice.application.dto;

import com.fiap.techchallenge.productmicroservice.domain.entities.CategoryEnum;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class CreateProductRequestDTOTest {

    @Test
    @DisplayName("Should create DTO with default constructor")
    void shouldCreateDTOWithDefaultConstructor() {
        CreateProductRequestDTO dto = new CreateProductRequestDTO();

        assertThat(dto).isNotNull();
        assertThat(dto.getName()).isNull();
        assertThat(dto.getCategory()).isNull();
    }

    @Test
    @DisplayName("Should create DTO with parameterized constructor")
    void shouldCreateDTOWithParameterizedConstructor() {
        CreateProductRequestDTO dto = new CreateProductRequestDTO(
                "Test Product",
                "Test Description",
                "http://image.url",
                2590L,
                2000L,
                CategoryEnum.LANCHE,
                10L
        );

        assertThat(dto.getName()).isEqualTo("Test Product");
        assertThat(dto.getDescription()).isEqualTo("Test Description");
        assertThat(dto.getImage()).isEqualTo("http://image.url");
        assertThat(dto.getPrice()).isEqualTo(2590L);
        assertThat(dto.getPriceForClient()).isEqualTo(2000L);
        assertThat(dto.getCategory()).isEqualTo(CategoryEnum.LANCHE);
        assertThat(dto.getQuantity()).isEqualTo(10L);
    }

    @Test
    @DisplayName("Should set and get all fields correctly")
    void shouldSetAndGetAllFieldsCorrectly() {
        CreateProductRequestDTO dto = new CreateProductRequestDTO();

        dto.setName("Product Name");
        dto.setDescription("Product Description");
        dto.setImage("http://image.url");
        dto.setPrice(5000L);
        dto.setPriceForClient(4500L);
        dto.setCategory(CategoryEnum.BEBIDA);
        dto.setQuantity(20L);

        assertThat(dto.getName()).isEqualTo("Product Name");
        assertThat(dto.getDescription()).isEqualTo("Product Description");
        assertThat(dto.getImage()).isEqualTo("http://image.url");
        assertThat(dto.getPrice()).isEqualTo(5000L);
        assertThat(dto.getPriceForClient()).isEqualTo(4500L);
        assertThat(dto.getCategory()).isEqualTo(CategoryEnum.BEBIDA);
        assertThat(dto.getQuantity()).isEqualTo(20L);
    }

    @Test
    @DisplayName("Should handle null optional fields")
    void shouldHandleNullOptionalFields() {
        CreateProductRequestDTO dto = new CreateProductRequestDTO();
        dto.setDescription(null);
        dto.setImage(null);
        dto.setPriceForClient(null);
        dto.setQuantity(null);

        assertThat(dto.getDescription()).isNull();
        assertThat(dto.getImage()).isNull();
        assertThat(dto.getPriceForClient()).isNull();
        assertThat(dto.getQuantity()).isNull();
    }

    @Test
    @DisplayName("Should set category enum correctly")
    void shouldSetCategoryEnumCorrectly() {
        CreateProductRequestDTO dto = new CreateProductRequestDTO();
        
        dto.setCategory(CategoryEnum.LANCHE);
        assertThat(dto.getCategory()).isEqualTo(CategoryEnum.LANCHE);
        
        dto.setCategory(CategoryEnum.SOBREMESA);
        assertThat(dto.getCategory()).isEqualTo(CategoryEnum.SOBREMESA);
    }

    @Test
    @DisplayName("Should handle zero quantity")
    void shouldHandleZeroQuantity() {
        CreateProductRequestDTO dto = new CreateProductRequestDTO();
        dto.setQuantity(0L);

        assertThat(dto.getQuantity()).isEqualTo(0L);
    }
}
