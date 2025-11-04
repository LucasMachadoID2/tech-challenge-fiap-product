package com.fiap.techchallenge.productmicroservice.domain.usecases;

import com.fiap.techchallenge.productmicroservice.domain.entities.CategoryEnum;
import com.fiap.techchallenge.productmicroservice.domain.entities.Product;
import com.fiap.techchallenge.productmicroservice.domain.repositories.ProductRepository;

import java.util.List;

public class FindProductsByCategoryUseCase {
    private final ProductRepository productRepository;

    public FindProductsByCategoryUseCase(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<Product> execute(CategoryEnum category) {
        if (category == null) {
            throw new IllegalArgumentException("Categoria é obrigatória");
        }
        return productRepository.findByCategory(category);
    }
}