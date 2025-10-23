package com.fiap.techchallenge.productmicroservice.domain.usecases;

import com.fiap.techchallenge.productmicroservice.domain.entities.Product;
import com.fiap.techchallenge.productmicroservice.domain.repositories.ProductRepository;

import java.math.BigDecimal;
import java.util.List;

public class FindProductsByCategoryAndPriceRangeManualUseCase {
    private final ProductRepository productRepository;

    public FindProductsByCategoryAndPriceRangeManualUseCase(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<Product> execute(String category, BigDecimal minPrice, BigDecimal maxPrice) {
        validateParameters(category, minPrice, maxPrice);
        return productRepository.findByCategoryAndPriceRangeManual(category, minPrice, maxPrice);
    }

    private void validateParameters(String category, BigDecimal minPrice, BigDecimal maxPrice) {
        if (category == null || category.trim().isEmpty()) {
            throw new IllegalArgumentException("Categoria é obrigatória");
        }
        if (minPrice == null || minPrice.signum() < 0) {
            throw new IllegalArgumentException("Preço mínimo deve ser maior ou igual a zero");
        }
        if (maxPrice == null || maxPrice.signum() <= 0) {
            throw new IllegalArgumentException("Preço máximo deve ser maior que zero");
        }
        if (minPrice.compareTo(maxPrice) > 0) {
            throw new IllegalArgumentException("Preço mínimo não pode ser maior que o preço máximo");
        }
    }
}