package com.fiap.techchallenge.productmicroservice.infrastructure.persistence;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface ProductMongoRepository extends MongoRepository<ProductDocument, String> {
    List<ProductDocument> findByCategory(String category);
    List<ProductDocument> findByOnPromotion(boolean onPromotion);
    List<ProductDocument> findByNameContainingIgnoreCase(String name);
    List<ProductDocument> findByCategoryAndPriceBetween(String category, BigDecimal minPrice, BigDecimal maxPrice);
    
    @Query("{ 'category': ?0, 'price': { $gte: ?1, $lte: ?2 } }")
    List<ProductDocument> findByCategoryAndPriceRangeManual(String category, BigDecimal minPrice, BigDecimal maxPrice);
}