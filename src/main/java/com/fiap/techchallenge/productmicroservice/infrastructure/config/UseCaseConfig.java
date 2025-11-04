package com.fiap.techchallenge.productmicroservice.infrastructure.config;

import com.fiap.techchallenge.productmicroservice.domain.repositories.ProductRepository;
import com.fiap.techchallenge.productmicroservice.domain.usecases.*;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UseCaseConfig {

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    @Bean
    public CreateProductUseCase createProductUseCase(ProductRepository productRepository) {
        return new CreateProductUseCase(productRepository);
    }

    @Bean
    public FindProductByIdUseCase findProductByIdUseCase(ProductRepository productRepository) {
        return new FindProductByIdUseCase(productRepository);
    }

    @Bean
    public FindAllProductsUseCase findAllProductsUseCase(ProductRepository productRepository) {
        return new FindAllProductsUseCase(productRepository);
    }

    @Bean
    public DeleteProductByIdUseCase deleteProductByIdUseCase(ProductRepository productRepository) {
        return new DeleteProductByIdUseCase(productRepository);
    }

    @Bean
    public FindProductsByCategoryUseCase findProductsByCategoryUseCase(ProductRepository productRepository) {
        return new FindProductsByCategoryUseCase(productRepository);
    }

    @Bean
    public FindProductsByNameUseCase findProductsByNameUseCase(ProductRepository productRepository) {
        return new FindProductsByNameUseCase(productRepository);
    }

    @Bean
    public FindProductsByCategoryAndPriceRangeUseCase findProductsByCategoryAndPriceRangeUseCase(ProductRepository productRepository) {
        return new FindProductsByCategoryAndPriceRangeUseCase(productRepository);
    }

    @Bean
    public FindProductsByCategoryAndPriceRangeManualUseCase findProductsByCategoryAndPriceRangeManualUseCase(ProductRepository productRepository) {
        return new FindProductsByCategoryAndPriceRangeManualUseCase(productRepository);
    }
}