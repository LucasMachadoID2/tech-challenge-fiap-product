package com.fiap.techchallenge.productmicroservice.infrastructure.persistence;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.index.Indexed;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Document(collection = "products")
public class ProductDocument {
    @Id
    private String id;
    
    @Indexed
    private String name;
    
    private String description;
    
    @Indexed
    private BigDecimal price;
    
    @Indexed
    private String category;
    
    @Indexed
    private boolean onPromotion;
    
    private BigDecimal promotionPrice;
    
    @Indexed
    private LocalDateTime createdAt;
    
    private LocalDateTime updatedAt;

    public ProductDocument() {}

    public ProductDocument(String name, String description, BigDecimal price, String category, 
                          boolean onPromotion, BigDecimal promotionPrice, 
                          LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.category = category;
        this.onPromotion = onPromotion;
        this.promotionPrice = promotionPrice;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
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
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public boolean isOnPromotion() {
        return onPromotion;
    }

    public void setOnPromotion(boolean onPromotion) {
        this.onPromotion = onPromotion;
    }

    public BigDecimal getPromotionPrice() {
        return promotionPrice;
    }

    public void setPromotionPrice(BigDecimal promotionPrice) {
        this.promotionPrice = promotionPrice;
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
}