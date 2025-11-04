package com.fiap.techchallenge.productmicroservice.domain.entities;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ProductTest {

    @Test
    @DisplayName("Should create product with valid data")
    void shouldCreateProductWithValidData() {
        Product product = new Product("Burger", "Delicious burger", "http://image.url", 
                2590L, 2000L, CategoryEnum.LANCHE, 10L);

        assertThat(product.getName()).isEqualTo("Burger");
        assertThat(product.getDescription()).isEqualTo("Delicious burger");
        assertThat(product.getImage()).isEqualTo("http://image.url");
        assertThat(product.getPrice()).isEqualTo(2590L);
        assertThat(product.getPriceForClient()).isEqualTo(2000L);
        assertThat(product.getCategory()).isEqualTo(CategoryEnum.LANCHE);
        assertThat(product.getQuantity()).isEqualTo(10L);
        assertThat(product.getCreatedAt()).isNotNull();
        assertThat(product.getUpdatedAt()).isNotNull();
    }

    @Test
    @DisplayName("Should throw exception when name is null")
    void shouldThrowExceptionWhenNameIsNull() {
        assertThatThrownBy(() -> new Product(null, "Description", "image.url", 2590L, 2000L, CategoryEnum.LANCHE, 10L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Nome do produto é obrigatório");
    }

    @Test
    @DisplayName("Should throw exception when name is empty")
    void shouldThrowExceptionWhenNameIsEmpty() {
        assertThatThrownBy(() -> new Product("", "Description", "image.url", 2590L, 2000L, CategoryEnum.LANCHE, 10L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Nome do produto é obrigatório");
    }

    @Test
    @DisplayName("Should throw exception when price is null")
    void shouldThrowExceptionWhenPriceIsNull() {
        assertThatThrownBy(() -> new Product("Product", "Description", "image.url", null, 2000L, CategoryEnum.LANCHE, 10L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Preço deve ser maior que zero");
    }

    @Test
    @DisplayName("Should throw exception when price is zero")
    void shouldThrowExceptionWhenPriceIsZero() {
        assertThatThrownBy(() -> new Product("Product", "Description", "image.url", 0L, 2000L, CategoryEnum.LANCHE, 10L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Preço deve ser maior que zero");
    }

    @Test
    @DisplayName("Should throw exception when price is negative")
    void shouldThrowExceptionWhenPriceIsNegative() {
        assertThatThrownBy(() -> new Product("Product", "Description", "image.url", -10L, 2000L, CategoryEnum.LANCHE, 10L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Preço deve ser maior que zero");
    }

    @Test
    @DisplayName("Should throw exception when category is null")
    void shouldThrowExceptionWhenCategoryIsNull() {
        assertThatThrownBy(() -> new Product("Product", "Description", "image.url", 2590L, 2000L, null, 10L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Categoria é obrigatória");
    }

    @Test
    @DisplayName("Should throw exception when quantity is negative")
    void shouldThrowExceptionWhenQuantityIsNegative() {
        assertThatThrownBy(() -> new Product("Product", "Description", "image.url", 2590L, 2000L, CategoryEnum.LANCHE, -5L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Quantidade não pode ser negativa");
    }

    @Test
    @DisplayName("Should update price successfully")
    void shouldUpdatePriceSuccessfully() {
        Product product = new Product("Product", "Description", "image.url", 5000L, 4000L, CategoryEnum.LANCHE, 10L);
        product.updatePrice(6000L);

        assertThat(product.getPrice()).isEqualTo(6000L);
    }

    @Test
    @DisplayName("Should update quantity successfully")
    void shouldUpdateQuantitySuccessfully() {
        Product product = new Product("Product", "Description", "image.url", 5000L, 4000L, CategoryEnum.LANCHE, 10L);
        product.updateQuantity(20L);

        assertThat(product.getQuantity()).isEqualTo(20L);
    }

    @Test
    @DisplayName("Should set name successfully")
    void shouldSetNameSuccessfully() {
        Product product = new Product("Product", "Description", "image.url", 5000L, 4000L, CategoryEnum.LANCHE, 10L);
        product.setName("New Name");

        assertThat(product.getName()).isEqualTo("New Name");
    }

    @Test
    @DisplayName("Should throw exception when setting null name")
    void shouldThrowExceptionWhenSettingNullName() {
        Product product = new Product("Product", "Description", "image.url", 5000L, 4000L, CategoryEnum.LANCHE, 10L);

        assertThatThrownBy(() -> product.setName(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Nome do produto é obrigatório");
    }

    @Test
    @DisplayName("Should set description successfully")
    void shouldSetDescriptionSuccessfully() {
        Product product = new Product("Product", "Description", "image.url", 5000L, 4000L, CategoryEnum.LANCHE, 10L);
        product.setDescription("New Description");

        assertThat(product.getDescription()).isEqualTo("New Description");
    }

    @Test
    @DisplayName("Should handle null description")
    void shouldHandleNullDescription() {
        Product product = new Product("Product", null, "image.url", 5000L, 4000L, CategoryEnum.LANCHE, 10L);
        assertThat(product.getDescription()).isEmpty();

        product.setDescription(null);
        assertThat(product.getDescription()).isEmpty();
    }

    @Test
    @DisplayName("Should set category successfully")
    void shouldSetCategorySuccessfully() {
        Product product = new Product("Product", "Description", "image.url", 5000L, 4000L, CategoryEnum.LANCHE, 10L);
        product.setCategory(CategoryEnum.BEBIDA);

        assertThat(product.getCategory()).isEqualTo(CategoryEnum.BEBIDA);
    }

    @Test
    @DisplayName("Should throw exception when setting null category")
    void shouldThrowExceptionWhenSettingNullCategory() {
        Product product = new Product("Product", "Description", "image.url", 5000L, 4000L, CategoryEnum.LANCHE, 10L);

        assertThatThrownBy(() -> product.setCategory(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Categoria é obrigatória");
    }

    @Test
    @DisplayName("Should set image successfully")
    void shouldSetImageSuccessfully() {
        Product product = new Product("Product", "Description", "image.url", 5000L, 4000L, CategoryEnum.LANCHE, 10L);
        product.setImage("new-image.url");

        assertThat(product.getImage()).isEqualTo("new-image.url");
    }

    @Test
    @DisplayName("Should set priceForClient successfully")
    void shouldSetPriceForClientSuccessfully() {
        Product product = new Product("Product", "Description", "image.url", 5000L, 4000L, CategoryEnum.LANCHE, 10L);
        product.setPriceForClient(3500L);

        assertThat(product.getPriceForClient()).isEqualTo(3500L);
    }

    @Test
    @DisplayName("Should test equals and hashCode")
    void shouldTestEqualsAndHashCode() {
        Product product1 = new Product("Product", "Description", "image.url", 5000L, 4000L, CategoryEnum.LANCHE, 10L);
        product1.setId("1");

        Product product2 = new Product("Product", "Description", "image.url", 5000L, 4000L, CategoryEnum.LANCHE, 10L);
        product2.setId("1");

        Product product3 = new Product("Other", "Description", "image.url", 5000L, 4000L, CategoryEnum.LANCHE, 10L);
        product3.setId("2");

        assertThat(product1).isEqualTo(product2);
        assertThat(product1).isNotEqualTo(product3);
        assertThat(product1.hashCode()).isEqualTo(product2.hashCode());
    }

    @Test
    @DisplayName("Should test toString")
    void shouldTestToString() {
        Product product = new Product("Product", "Description", "image.url", 5000L, 4000L, CategoryEnum.LANCHE, 10L);
        product.setId("1");

        String toString = product.toString();

        assertThat(toString).contains("Product");
        assertThat(toString).contains("1");
        assertThat(toString).contains("LANCHE");
    }

    @Test
    @DisplayName("Should trim name")
    void shouldTrimName() {
        Product product = new Product("  Product  ", "Description", "image.url", 5000L, 4000L, CategoryEnum.LANCHE, 10L);

        assertThat(product.getName()).isEqualTo("Product");
    }

    @Test
    @DisplayName("Should create product with constructor with id")
    void shouldCreateProductWithConstructorWithId() {
        Product product = new Product("1", "Product", "Description", "image.url",
                5000L, 4000L, CategoryEnum.LANCHE, 10L);

        assertThat(product.getId()).isEqualTo("1");
        assertThat(product.getName()).isEqualTo("Product");
        assertThat(product.getQuantity()).isEqualTo(10L);
    }

    @Test
    @DisplayName("Should default quantity to zero when null")
    void shouldDefaultQuantityToZeroWhenNull() {
        Product product = new Product("Product", "Description", "image.url", 5000L, 4000L, CategoryEnum.LANCHE, null);

        assertThat(product.getQuantity()).isEqualTo(0L);
    }
}
