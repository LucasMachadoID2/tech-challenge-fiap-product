package com.fiap.techchallenge.productmicroservice.domain.usecases;

import com.fiap.techchallenge.productmicroservice.domain.entities.Product;
import com.fiap.techchallenge.productmicroservice.domain.repositories.ProductRepository;

import java.util.List;

public class FindProductsOnPromotionUseCase {
    private final ProductRepository productRepository;

    public FindProductsOnPromotionUseCase(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<Product> execute() {
        return productRepository.findByOnPromotion(true);
    }
}