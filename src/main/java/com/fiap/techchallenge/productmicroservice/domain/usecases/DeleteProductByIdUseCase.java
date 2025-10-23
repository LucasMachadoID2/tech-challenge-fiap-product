package com.fiap.techchallenge.productmicroservice.domain.usecases;

import com.fiap.techchallenge.productmicroservice.domain.repositories.ProductRepository;

public class DeleteProductByIdUseCase {
    private final ProductRepository productRepository;

    public DeleteProductByIdUseCase(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public void execute(String id) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("ID do produto é obrigatório");
        }
        
        if (productRepository.findById(id).isEmpty()) {
            throw new IllegalArgumentException("Produto não encontrado");
        }
        
        productRepository.deleteById(id);
    }
}