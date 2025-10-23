package com.fiap.techchallenge.productmicroservice.domain.entities;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

public class Product {
    private static final String REQUIRED_NAME_MESSAGE = "Nome do produto é obrigatório";
    private static final String REQUIRED_CATEGORY_MESSAGE = "Categoria é obrigatória";
    private static final String INVALID_PRICE_MESSAGE = "Preço deve ser maior que zero";
    private static final String INVALID_PROMOTION_PRICE_MESSAGE = "Preço promocional deve ser menor que o preço normal";

    private String id;
    private String name;
    private String description;
    private BigDecimal price;
    private String category;
    private boolean onPromotion;
    private BigDecimal promotionPrice;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Product() {
    }

    public Product(String name, String description, BigDecimal price, String category) {
        validateRequiredFields(name, price, category);
        this.name = name.trim();
        this.description = description != null ? description.trim() : "";
        this.price = price;
        this.category = category.trim();
        this.onPromotion = false;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public Product(String id, String name, String description, BigDecimal price, String category, 
                   boolean onPromotion, BigDecimal promotionPrice) {
        this(name, description, price, category);
        this.id = id;
        if (onPromotion && promotionPrice != null) {
            applyPromotion(promotionPrice);
        }
    }

    public BigDecimal getEffectivePrice() {
        return isValidPromotion() ? promotionPrice : price;
    }

    public void applyPromotion(BigDecimal promotionPrice) {
        validatePromotionPrice(promotionPrice);
        this.promotionPrice = promotionPrice;
        this.onPromotion = true;
        updateTimestamp();
    }

    public void removePromotion() {
        this.onPromotion = false;
        this.promotionPrice = null;
        updateTimestamp();
    }

    public void updatePrice(BigDecimal newPrice) {
        validatePrice(newPrice);
        this.price = newPrice;
        
        if (isValidPromotion() && newPrice.compareTo(promotionPrice) <= 0) {
            removePromotion();
        }
        updateTimestamp();
    }

    public boolean isValidPromotion() {
        return onPromotion && promotionPrice != null && promotionPrice.compareTo(price) < 0;
    }

    private void validateRequiredFields(String name, BigDecimal price, String category) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException(REQUIRED_NAME_MESSAGE);
        }
        validatePrice(price);
        if (category == null || category.trim().isEmpty()) {
            throw new IllegalArgumentException(REQUIRED_CATEGORY_MESSAGE);
        }
    }

    private void validatePrice(BigDecimal price) {
        if (price == null || price.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException(INVALID_PRICE_MESSAGE);
        }
    }

    private void validatePromotionPrice(BigDecimal promotionPrice) {
        if (promotionPrice == null || promotionPrice.compareTo(this.price) >= 0) {
            throw new IllegalArgumentException(INVALID_PROMOTION_PRICE_MESSAGE);
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

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        updatePrice(price);
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        if (category == null || category.trim().isEmpty()) {
            throw new IllegalArgumentException(REQUIRED_CATEGORY_MESSAGE);
        }
        this.category = category.trim();
        updateTimestamp();
    }

    public boolean isOnPromotion() {
        return onPromotion;
    }

    public void setOnPromotion(boolean onPromotion) {
        this.onPromotion = onPromotion;
        updateTimestamp();
    }

    public BigDecimal getPromotionPrice() {
        return promotionPrice;
    }

    public void setPromotionPrice(BigDecimal promotionPrice) {
        if (promotionPrice != null) {
            applyPromotion(promotionPrice);
        } else {
            removePromotion();
        }
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
                ", category='" + category + '\'' +
                ", price=" + price +
                ", effectivePrice=" + getEffectivePrice() +
                ", onPromotion=" + onPromotion +
                '}';
    }
}