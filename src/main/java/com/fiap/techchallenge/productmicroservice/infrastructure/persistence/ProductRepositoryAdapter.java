package com.fiap.techchallenge.productmicroservice.infrastructure.persistence;

import com.fiap.techchallenge.productmicroservice.domain.entities.CategoryEnum;
import com.fiap.techchallenge.productmicroservice.domain.entities.Product;
import com.fiap.techchallenge.productmicroservice.domain.repositories.ProductRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class ProductRepositoryAdapter implements ProductRepository {
    private final ProductMongoRepository productMongoRepository;
    private final ModelMapper modelMapper;

    public ProductRepositoryAdapter(ProductMongoRepository productMongoRepository, ModelMapper modelMapper) {
        this.productMongoRepository = productMongoRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public Product save(Product product) {
        ProductDocument document = modelMapper.map(product, ProductDocument.class);
        ProductDocument savedDocument = productMongoRepository.save(document);
        return modelMapper.map(savedDocument, Product.class);
    }

    @Override
    public Optional<Product> findById(String id) {
        return productMongoRepository.findById(id)
                .map(document -> modelMapper.map(document, Product.class));
    }

    @Override
    public List<Product> findAll() {
        return productMongoRepository.findAll()
                .stream()
                .map(document -> modelMapper.map(document, Product.class))
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(String id) {
        productMongoRepository.deleteById(id);
    }

    @Override
    public List<Product> findByCategory(CategoryEnum category) {
        return productMongoRepository.findByCategory(category)
                .stream()
                .map(document -> modelMapper.map(document, Product.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<Product> findByNameContaining(String name) {
        return productMongoRepository.findByNameContainingIgnoreCase(name)
                .stream()
                .map(document -> modelMapper.map(document, Product.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<Product> findByCategoryAndPriceBetween(CategoryEnum category, Long minPrice, Long maxPrice) {
        return productMongoRepository.findByCategoryAndPriceBetween(category, minPrice, maxPrice)
                .stream()
                .map(document -> modelMapper.map(document, Product.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<Product> findByCategoryAndPriceRangeManual(CategoryEnum category, Long minPrice, Long maxPrice) {
        return productMongoRepository.findByCategoryAndPriceRangeManual(category, minPrice, maxPrice)
                .stream()
                .map(document -> modelMapper.map(document, Product.class))
                .collect(Collectors.toList());
    }
}