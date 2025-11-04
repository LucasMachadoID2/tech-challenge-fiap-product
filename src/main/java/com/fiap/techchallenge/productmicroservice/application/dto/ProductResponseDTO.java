package com.fiap.techchallenge.productmicroservice.application.dto;

import com.fiap.techchallenge.productmicroservice.domain.entities.CategoryEnum;

import java.time.LocalDateTime;

public class ProductResponseDTO {
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

    public ProductResponseDTO() {}

    public ProductResponseDTO(String id, String name, String description, String image, 
                             Long price, Long priceForClient, CategoryEnum category, 
                             Long quantity, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.image = image;
        this.price = price;
        this.priceForClient = priceForClient;
        this.category = category;
        this.quantity = quantity;
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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Long getPrice() {
        return price;
    }

    public void setPrice(Long price) {
        this.price = price;
    }

    public Long getPriceForClient() {
        return priceForClient;
    }

    public void setPriceForClient(Long priceForClient) {
        this.priceForClient = priceForClient;
    }

    public CategoryEnum getCategory() {
        return category;
    }

    public void setCategory(CategoryEnum category) {
        this.category = category;
    }

    public Long getQuantity() {
        return quantity;
    }

    public void setQuantity(Long quantity) {
        this.quantity = quantity;
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