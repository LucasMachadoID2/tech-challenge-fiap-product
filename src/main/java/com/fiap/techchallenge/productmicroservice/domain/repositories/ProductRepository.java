package com.fiap.techchallenge.productmicroservice.domain.repositories;

import com.fiap.techchallenge.productmicroservice.domain.entities.Product;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface ProductRepository {
    Product save(Product product);
    Optional<Product> findById(String id);
    List<Product> findAll();
    void deleteById(String id);
    List<Product> findByCategory(String category);
    List<Product> findByOnPromotion(boolean onPromotion);
    List<Product> findByNameContaining(String name);
    List<Product> findByCategoryAndPriceBetween(String category, BigDecimal minPrice, BigDecimal maxPrice);
    List<Product> findByCategoryAndPriceRangeManual(String category, BigDecimal minPrice, BigDecimal maxPrice);
}