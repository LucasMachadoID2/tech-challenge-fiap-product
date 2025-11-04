package com.fiap.techchallenge.productmicroservice.domain.entities;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ProductCategoryTest {

    @Test
    @DisplayName("Should have all category enum values")
    void shouldHaveAllCategoryEnumValues() {
        CategoryEnum[] categories = CategoryEnum.values();
        
        assertThat(categories).hasSize(4);
        assertThat(categories).contains(
            CategoryEnum.LANCHE,
            CategoryEnum.ACOMPANHAMENTO,
            CategoryEnum.BEBIDA,
            CategoryEnum.SOBREMESA
        );
    }

    @Test
    @DisplayName("Should convert enum to string correctly")
    void shouldConvertEnumToString() {
        assertThat(CategoryEnum.LANCHE.toString()).isEqualTo("LANCHE");
        assertThat(CategoryEnum.ACOMPANHAMENTO.toString()).isEqualTo("ACOMPANHAMENTO");
        assertThat(CategoryEnum.BEBIDA.toString()).isEqualTo("BEBIDA");
        assertThat(CategoryEnum.SOBREMESA.toString()).isEqualTo("SOBREMESA");
    }

    @Test
    @DisplayName("Should get enum by name")
    void shouldGetEnumByName() {
        assertThat(CategoryEnum.valueOf("LANCHE")).isEqualTo(CategoryEnum.LANCHE);
        assertThat(CategoryEnum.valueOf("ACOMPANHAMENTO")).isEqualTo(CategoryEnum.ACOMPANHAMENTO);
        assertThat(CategoryEnum.valueOf("BEBIDA")).isEqualTo(CategoryEnum.BEBIDA);
        assertThat(CategoryEnum.valueOf("SOBREMESA")).isEqualTo(CategoryEnum.SOBREMESA);
    }

    @Test
    @DisplayName("Should have correct enum constants")
    void shouldHaveCorrectEnumConstants() {
        assertThat(CategoryEnum.LANCHE).isNotNull();
        assertThat(CategoryEnum.ACOMPANHAMENTO).isNotNull();
        assertThat(CategoryEnum.BEBIDA).isNotNull();
        assertThat(CategoryEnum.SOBREMESA).isNotNull();
    }
}
