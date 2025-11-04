package com.fiap.techchallenge.productmicroservice.domain.entities;

import java.time.LocalDateTime;
import java.util.Objects;

public class Product {
    private static final String REQUIRED_NAME_MESSAGE = "Nome do produto é obrigatório";
    private static final String REQUIRED_CATEGORY_MESSAGE = "Categoria é obrigatória";
    private static final String INVALID_PRICE_MESSAGE = "Preço deve ser maior que zero";
    private static final String INVALID_QUANTITY_MESSAGE = "Quantidade não pode ser negativa";

    private String id;
    private String name;
    private String description;
    private String image;
    private Long price;
    private Long priceForClient;
    private CategoryEnum category;
    private Long quantity;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Product() {
    }

    public Product(String name, String description, String image, Long price, 
                   Long priceForClient, CategoryEnum category, Long quantity) {
        validateRequiredFields(name, price, category);
        validateQuantity(quantity);
        this.name = name.trim();
        this.description = description != null ? description.trim() : "";
        this.image = image;
        this.price = price;
        this.priceForClient = priceForClient;
        this.category = category;
        this.quantity = quantity != null ? quantity : 0L;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public Product(String id, String name, String description, String image, 
                   Long price, Long priceForClient, CategoryEnum category, Long quantity) {
        this(name, description, image, price, priceForClient, category, quantity);
        this.id = id;
    }

    public void updatePrice(Long newPrice) {
        validatePrice(newPrice);
        this.price = newPrice;
        updateTimestamp();
    }

    public void updateQuantity(Long newQuantity) {
        validateQuantity(newQuantity);
        this.quantity = newQuantity;
        updateTimestamp();
    }

    private void validateRequiredFields(String name, Long price, CategoryEnum category) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException(REQUIRED_NAME_MESSAGE);
        }
        validatePrice(price);
        if (category == null) {
            throw new IllegalArgumentException(REQUIRED_CATEGORY_MESSAGE);
        }
    }

    private void validatePrice(Long price) {
        if (price == null || price <= 0) {
            throw new IllegalArgumentException(INVALID_PRICE_MESSAGE);
        }
    }

    private void validateQuantity(Long quantity) {
        if (quantity != null && quantity < 0) {
            throw new IllegalArgumentException(INVALID_QUANTITY_MESSAGE);
        }
    }

    private void updateTimestamp() {
        this.updatedAt = LocalDateTime.now();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException(REQUIRED_NAME_MESSAGE);
        }
        this.name = name.trim();
        updateTimestamp();
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description != null ? description.trim() : "";
        updateTimestamp();
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
        updateTimestamp();
    }

    public Long getPrice() {
        return price;
    }

    public void setPrice(Long price) {
        updatePrice(price);
    }

    public Long getPriceForClient() {
        return priceForClient;
    }

    public void setPriceForClient(Long priceForClient) {
        this.priceForClient = priceForClient;
        updateTimestamp();
    }

    public CategoryEnum getCategory() {
        return category;
    }

    public void setCategory(CategoryEnum category) {
        if (category == null) {
            throw new IllegalArgumentException(REQUIRED_CATEGORY_MESSAGE);
        }
        this.category = category;
        updateTimestamp();
    }

    public Long getQuantity() {
        return quantity;
    }

    public void setQuantity(Long quantity) {
        updateQuantity(quantity);
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return Objects.equals(id, product.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Product{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", category=" + category +
                ", price=" + price +
                ", priceForClient=" + priceForClient +
                ", quantity=" + quantity +
                '}';
    }
}