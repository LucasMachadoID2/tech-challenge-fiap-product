package com.fiap.techchallenge.productmicroservice.domain.usecases;

import com.fiap.techchallenge.productmicroservice.domain.entities.CategoryEnum;
import com.fiap.techchallenge.productmicroservice.domain.entities.Product;
import com.fiap.techchallenge.productmicroservice.domain.repositories.ProductRepository;

import java.util.List;

public class FindProductsByCategoryAndPriceRangeManualUseCase {
    private final ProductRepository productRepository;

    public FindProductsByCategoryAndPriceRangeManualUseCase(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<Product> execute(CategoryEnum category, Long minPrice, Long maxPrice) {
        validateParameters(category, minPrice, maxPrice);
        return productRepository.findByCategoryAndPriceRangeManual(category, minPrice, maxPrice);
    }

    private void validateParameters(CategoryEnum category, Long minPrice, Long maxPrice) {
        if (category == null) {
            throw new IllegalArgumentException("Categoria é obrigatória");
        }
        if (minPrice == null || minPrice < 0) {
            throw new IllegalArgumentException("Preço mínimo deve ser maior ou igual a zero");
        }
        if (maxPrice == null || maxPrice <= 0) {
            throw new IllegalArgumentException("Preço máximo deve ser maior que zero");
        }
        if (minPrice > maxPrice) {
            throw new IllegalArgumentException("Preço mínimo não pode ser maior que o preço máximo");
        }
    }
}