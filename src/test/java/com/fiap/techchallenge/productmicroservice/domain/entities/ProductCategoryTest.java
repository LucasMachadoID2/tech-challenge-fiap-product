package com.fiap.techchallenge.productmicroservice.domain.entities;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ProductCategoryTest {

    @Test
    @DisplayName("Should validate valid categories")
    void shouldValidateValidCategories() {
        assertThat(ProductCategory.isValidCategory("LANCHE")).isTrue();
        assertThat(ProductCategory.isValidCategory("ACOMPANHAMENTO")).isTrue();
        assertThat(ProductCategory.isValidCategory("BEBIDA")).isTrue();
        assertThat(ProductCategory.isValidCategory("SOBREMESA")).isTrue();
    }

    @Test
    @DisplayName("Should invalidate invalid category")
    void shouldInvalidateInvalidCategory() {
        assertThat(ProductCategory.isValidCategory("INVALID")).isFalse();
        assertThat(ProductCategory.isValidCategory("")).isFalse();
        assertThat(ProductCategory.isValidCategory(null)).isFalse();
    }

    @Test
    @DisplayName("Should get all categories")
    void shouldGetAllCategories() {
        String[] categories = ProductCategory.getAllCategories();
        
        assertThat(categories).hasSize(4);
        assertThat(categories).contains("LANCHE", "ACOMPANHAMENTO", "BEBIDA", "SOBREMESA");
    }

    @Test
    @DisplayName("Should have correct category constants")
    void shouldHaveCorrectCategoryConstants() {
        assertThat(ProductCategory.LANCHE).isEqualTo("LANCHE");
        assertThat(ProductCategory.ACOMPANHAMENTO).isEqualTo("ACOMPANHAMENTO");
        assertThat(ProductCategory.BEBIDA).isEqualTo("BEBIDA");
        assertThat(ProductCategory.SOBREMESA).isEqualTo("SOBREMESA");
    }
}
