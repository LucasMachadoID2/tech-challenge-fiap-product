package com.fiap.techchallenge.productmicroservice.domain.usecases;

import com.fiap.techchallenge.productmicroservice.domain.entities.Product;
import com.fiap.techchallenge.productmicroservice.domain.repositories.ProductRepository;

import java.util.List;

public class FindProductsByCategoryUseCase {
    private final ProductRepository productRepository;

    public FindProductsByCategoryUseCase(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<Product> execute(String category) {
        if (category == null || category.trim().isEmpty()) {
            throw new IllegalArgumentException("Categoria é obrigatória");
        }
        return productRepository.findByCategory(category);
    }
}