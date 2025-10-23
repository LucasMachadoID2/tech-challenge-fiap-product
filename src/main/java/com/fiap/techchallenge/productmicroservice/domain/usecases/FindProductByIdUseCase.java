package com.fiap.techchallenge.productmicroservice.domain.usecases;

import com.fiap.techchallenge.productmicroservice.domain.entities.Product;
import com.fiap.techchallenge.productmicroservice.domain.repositories.ProductRepository;

import java.util.Optional;

public class FindProductByIdUseCase {
    private final ProductRepository productRepository;

    public FindProductByIdUseCase(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Optional<Product> execute(String id) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("ID do produto é obrigatório");
        }
        return productRepository.findById(id);
    }
}