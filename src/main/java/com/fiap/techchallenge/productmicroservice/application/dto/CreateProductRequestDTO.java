package com.fiap.techchallenge.productmicroservice.application.dto;

import com.fiap.techchallenge.productmicroservice.domain.entities.CategoryEnum;
import jakarta.validation.constraints.*;

public class CreateProductRequestDTO {
    @NotBlank(message = "Nome é obrigatório")
    @Size(min = 2, max = 100, message = "Nome deve ter entre 2 e 100 caracteres")
    private String name;

    @Size(max = 500, message = "Descrição deve ter no máximo 500 caracteres")
    private String description;

    private String image;

    @NotNull(message = "Preço é obrigatório")
    @Min(value = 1, message = "Preço deve ser maior que zero")
    private Long price;

    private Long priceForClient;

    @NotNull(message = "Categoria é obrigatória")
    private CategoryEnum category;

    @Min(value = 0, message = "Quantidade não pode ser negativa")
    private Long quantity;

    public CreateProductRequestDTO() {}

    public CreateProductRequestDTO(String name, String description, String image, Long price, 
                                   Long priceForClient, CategoryEnum category, Long quantity) {
        this.name = name;
        this.description = description;
        this.image = image;
        this.price = price;
        this.priceForClient = priceForClient;
        this.category = category;
        this.quantity = quantity;
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
}