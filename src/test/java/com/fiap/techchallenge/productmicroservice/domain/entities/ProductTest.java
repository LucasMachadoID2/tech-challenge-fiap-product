package com.fiap.techchallenge.productmicroservice.domain.entities;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ProductTest {

    @Test
    @DisplayName("Should create product with valid data")
    void shouldCreateProductWithValidData() {
        Product product = new Product("Burger", "Delicious burger", new BigDecimal("25.90"), "LANCHE");

        assertThat(product.getName()).isEqualTo("Burger");
        assertThat(product.getDescription()).isEqualTo("Delicious burger");
        assertThat(product.getPrice()).isEqualByComparingTo(new BigDecimal("25.90"));
        assertThat(product.getCategory()).isEqualTo("LANCHE");
        assertThat(product.isOnPromotion()).isFalse();
        assertThat(product.getCreatedAt()).isNotNull();
        assertThat(product.getUpdatedAt()).isNotNull();
    }

    @Test
    @DisplayName("Should throw exception when name is null")
    void shouldThrowExceptionWhenNameIsNull() {
        assertThatThrownBy(() -> new Product(null, "Description", new BigDecimal("25.90"), "LANCHE"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Nome do produto é obrigatório");
    }

    @Test
    @DisplayName("Should throw exception when name is empty")
    void shouldThrowExceptionWhenNameIsEmpty() {
        assertThatThrownBy(() -> new Product("", "Description", new BigDecimal("25.90"), "LANCHE"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Nome do produto é obrigatório");
    }

    @Test
    @DisplayName("Should throw exception when price is null")
    void shouldThrowExceptionWhenPriceIsNull() {
        assertThatThrownBy(() -> new Product("Product", "Description", null, "LANCHE"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Preço deve ser maior que zero");
    }

    @Test
    @DisplayName("Should throw exception when price is zero")
    void shouldThrowExceptionWhenPriceIsZero() {
        assertThatThrownBy(() -> new Product("Product", "Description", BigDecimal.ZERO, "LANCHE"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Preço deve ser maior que zero");
    }

    @Test
    @DisplayName("Should throw exception when price is negative")
    void shouldThrowExceptionWhenPriceIsNegative() {
        assertThatThrownBy(() -> new Product("Product", "Description", new BigDecimal("-10"), "LANCHE"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Preço deve ser maior que zero");
    }

    @Test
    @DisplayName("Should throw exception when category is null")
    void shouldThrowExceptionWhenCategoryIsNull() {
        assertThatThrownBy(() -> new Product("Product", "Description", new BigDecimal("25.90"), null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Categoria é obrigatória");
    }

    @Test
    @DisplayName("Should throw exception when category is empty")
    void shouldThrowExceptionWhenCategoryIsEmpty() {
        assertThatThrownBy(() -> new Product("Product", "Description", new BigDecimal("25.90"), ""))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Categoria é obrigatória");
    }

    @Test
    @DisplayName("Should apply promotion successfully")
    void shouldApplyPromotionSuccessfully() {
        Product product = new Product("Product", "Description", new BigDecimal("50.00"), "LANCHE");
        product.applyPromotion(new BigDecimal("35.00"));

        assertThat(product.isOnPromotion()).isTrue();
        assertThat(product.getPromotionPrice()).isEqualByComparingTo(new BigDecimal("35.00"));
        assertThat(product.getEffectivePrice()).isEqualByComparingTo(new BigDecimal("35.00"));
    }

    @Test
    @DisplayName("Should throw exception when promotion price is greater than or equal to regular price")
    void shouldThrowExceptionWhenPromotionPriceIsGreaterThanRegularPrice() {
        Product product = new Product("Product", "Description", new BigDecimal("50.00"), "LANCHE");

        assertThatThrownBy(() -> product.applyPromotion(new BigDecimal("60.00")))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Preço promocional deve ser menor que o preço normal");
    }

    @Test
    @DisplayName("Should throw exception when promotion price equals regular price")
    void shouldThrowExceptionWhenPromotionPriceEqualsRegularPrice() {
        Product product = new Product("Product", "Description", new BigDecimal("50.00"), "LANCHE");

        assertThatThrownBy(() -> product.applyPromotion(new BigDecimal("50.00")))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Preço promocional deve ser menor que o preço normal");
    }

    @Test
    @DisplayName("Should remove promotion successfully")
    void shouldRemovePromotionSuccessfully() {
        Product product = new Product("Product", "Description", new BigDecimal("50.00"), "LANCHE");
        product.applyPromotion(new BigDecimal("35.00"));
        product.removePromotion();

        assertThat(product.isOnPromotion()).isFalse();
        assertThat(product.getPromotionPrice()).isNull();
        assertThat(product.getEffectivePrice()).isEqualByComparingTo(new BigDecimal("50.00"));
    }

    @Test
    @DisplayName("Should update price successfully")
    void shouldUpdatePriceSuccessfully() {
        Product product = new Product("Product", "Description", new BigDecimal("50.00"), "LANCHE");
        product.updatePrice(new BigDecimal("60.00"));

        assertThat(product.getPrice()).isEqualByComparingTo(new BigDecimal("60.00"));
    }

    @Test
    @DisplayName("Should get effective price as regular price when not on promotion")
    void shouldGetEffectivePriceAsRegularPriceWhenNotOnPromotion() {
        Product product = new Product("Product", "Description", new BigDecimal("50.00"), "LANCHE");

        assertThat(product.getEffectivePrice()).isEqualByComparingTo(new BigDecimal("50.00"));
    }

    @Test
    @DisplayName("Should validate promotion correctly")
    void shouldValidatePromotionCorrectly() {
        Product product = new Product("Product", "Description", new BigDecimal("50.00"), "LANCHE");
        assertThat(product.isValidPromotion()).isFalse();

        product.applyPromotion(new BigDecimal("35.00"));
        assertThat(product.isValidPromotion()).isTrue();
    }

    @Test
    @DisplayName("Should set name successfully")
    void shouldSetNameSuccessfully() {
        Product product = new Product("Product", "Description", new BigDecimal("50.00"), "LANCHE");
        product.setName("New Name");

        assertThat(product.getName()).isEqualTo("New Name");
    }

    @Test
    @DisplayName("Should throw exception when setting null name")
    void shouldThrowExceptionWhenSettingNullName() {
        Product product = new Product("Product", "Description", new BigDecimal("50.00"), "LANCHE");

        assertThatThrownBy(() -> product.setName(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Nome do produto é obrigatório");
    }

    @Test
    @DisplayName("Should set description successfully")
    void shouldSetDescriptionSuccessfully() {
        Product product = new Product("Product", "Description", new BigDecimal("50.00"), "LANCHE");
        product.setDescription("New Description");

        assertThat(product.getDescription()).isEqualTo("New Description");
    }

    @Test
    @DisplayName("Should handle null description")
    void shouldHandleNullDescription() {
        Product product = new Product("Product", null, new BigDecimal("50.00"), "LANCHE");
        assertThat(product.getDescription()).isEmpty();

        product.setDescription(null);
        assertThat(product.getDescription()).isEmpty();
    }

    @Test
    @DisplayName("Should set category successfully")
    void shouldSetCategorySuccessfully() {
        Product product = new Product("Product", "Description", new BigDecimal("50.00"), "LANCHE");
        product.setCategory("BEBIDA");

        assertThat(product.getCategory()).isEqualTo("BEBIDA");
    }

    @Test
    @DisplayName("Should throw exception when setting null category")
    void shouldThrowExceptionWhenSettingNullCategory() {
        Product product = new Product("Product", "Description", new BigDecimal("50.00"), "LANCHE");

        assertThatThrownBy(() -> product.setCategory(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Categoria é obrigatória");
    }

    @Test
    @DisplayName("Should set promotion price with setter")
    void shouldSetPromotionPriceWithSetter() {
        Product product = new Product("Product", "Description", new BigDecimal("50.00"), "LANCHE");
        product.setPromotionPrice(new BigDecimal("35.00"));

        assertThat(product.getPromotionPrice()).isEqualByComparingTo(new BigDecimal("35.00"));
        assertThat(product.isOnPromotion()).isTrue();
    }

    @Test
    @DisplayName("Should remove promotion when setting null promotion price")
    void shouldRemovePromotionWhenSettingNullPromotionPrice() {
        Product product = new Product("Product", "Description", new BigDecimal("50.00"), "LANCHE");
        product.applyPromotion(new BigDecimal("35.00"));
        product.setPromotionPrice(null);

        assertThat(product.getPromotionPrice()).isNull();
        assertThat(product.isOnPromotion()).isFalse();
    }

    @Test
    @DisplayName("Should test equals and hashCode")
    void shouldTestEqualsAndHashCode() {
        Product product1 = new Product("Product", "Description", new BigDecimal("50.00"), "LANCHE");
        product1.setId("1");

        Product product2 = new Product("Product", "Description", new BigDecimal("50.00"), "LANCHE");
        product2.setId("1");

        Product product3 = new Product("Other", "Description", new BigDecimal("50.00"), "LANCHE");
        product3.setId("2");

        assertThat(product1).isEqualTo(product2);
        assertThat(product1).isNotEqualTo(product3);
        assertThat(product1.hashCode()).isEqualTo(product2.hashCode());
    }

    @Test
    @DisplayName("Should test toString")
    void shouldTestToString() {
        Product product = new Product("Product", "Description", new BigDecimal("50.00"), "LANCHE");
        product.setId("1");

        String toString = product.toString();

        assertThat(toString).contains("Product");
        assertThat(toString).contains("1");
        assertThat(toString).contains("LANCHE");
    }

    @Test
    @DisplayName("Should trim name and category")
    void shouldTrimNameAndCategory() {
        Product product = new Product("  Product  ", "Description", new BigDecimal("50.00"), "  LANCHE  ");

        assertThat(product.getName()).isEqualTo("Product");
        assertThat(product.getCategory()).isEqualTo("LANCHE");
    }

    @Test
    @DisplayName("Should create product with constructor with all parameters")
    void shouldCreateProductWithConstructorWithAllParameters() {
        Product product = new Product("1", "Product", "Description", 
                new BigDecimal("50.00"), "LANCHE", true, new BigDecimal("35.00"));

        assertThat(product.getId()).isEqualTo("1");
        assertThat(product.getName()).isEqualTo("Product");
        assertThat(product.isOnPromotion()).isTrue();
        assertThat(product.getPromotionPrice()).isEqualByComparingTo(new BigDecimal("35.00"));
    }
}
