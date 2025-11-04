package com.fiap.techchallenge.productmicroservice.infrastructure.persistence;

import com.fiap.techchallenge.productmicroservice.domain.entities.CategoryEnum;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductMongoRepository extends MongoRepository<ProductDocument, String> {
    List<ProductDocument> findByCategory(CategoryEnum category);
    List<ProductDocument> findByNameContainingIgnoreCase(String name);
    List<ProductDocument> findByCategoryAndPriceBetween(CategoryEnum category, Long minPrice, Long maxPrice);
    
    @Query("{ 'category': ?0, 'price': { $gte: ?1, $lte: ?2 } }")
    List<ProductDocument> findByCategoryAndPriceRangeManual(CategoryEnum category, Long minPrice, Long maxPrice);
}