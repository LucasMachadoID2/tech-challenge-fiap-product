package com.fiap.techchallenge.productmicroservice.domain.repositories;

import com.fiap.techchallenge.productmicroservice.domain.entities.CategoryEnum;
import com.fiap.techchallenge.productmicroservice.domain.entities.Product;

import java.util.List;
import java.util.Optional;

public interface ProductRepository {
    Product save(Product product);
    Optional<Product> findById(String id);
    List<Product> findAll();
    void deleteById(String id);
    List<Product> findByCategory(CategoryEnum category);
    List<Product> findByNameContaining(String name);
    List<Product> findByCategoryAndPriceBetween(CategoryEnum category, Long minPrice, Long maxPrice);
    List<Product> findByCategoryAndPriceRangeManual(CategoryEnum category, Long minPrice, Long maxPrice);
}