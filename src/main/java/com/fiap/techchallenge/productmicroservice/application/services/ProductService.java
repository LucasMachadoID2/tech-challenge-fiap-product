package com.fiap.techchallenge.productmicroservice.application.services;

import com.fiap.techchallenge.productmicroservice.application.dto.CreateProductRequestDTO;
import com.fiap.techchallenge.productmicroservice.application.dto.ProductResponseDTO;
import com.fiap.techchallenge.productmicroservice.domain.entities.Product;
import com.fiap.techchallenge.productmicroservice.domain.usecases.*;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductService {
    private final CreateProductUseCase createProductUseCase;
    private final FindProductByIdUseCase findProductByIdUseCase;
    private final FindAllProductsUseCase findAllProductsUseCase;
    private final DeleteProductByIdUseCase deleteProductByIdUseCase;
    private final FindProductsByCategoryUseCase findProductsByCategoryUseCase;
    private final FindProductsOnPromotionUseCase findProductsOnPromotionUseCase;
    private final FindProductsByNameUseCase findProductsByNameUseCase;
    private final FindProductsByCategoryAndPriceRangeUseCase findProductsByCategoryAndPriceRangeUseCase;
    private final FindProductsByCategoryAndPriceRangeManualUseCase findProductsByCategoryAndPriceRangeManualUseCase;
    private final ModelMapper modelMapper;

    public ProductService(CreateProductUseCase createProductUseCase,
                         FindProductByIdUseCase findProductByIdUseCase,
                         FindAllProductsUseCase findAllProductsUseCase,
                         DeleteProductByIdUseCase deleteProductByIdUseCase,
                         FindProductsByCategoryUseCase findProductsByCategoryUseCase,
                         FindProductsOnPromotionUseCase findProductsOnPromotionUseCase,
                         FindProductsByNameUseCase findProductsByNameUseCase,
                         FindProductsByCategoryAndPriceRangeUseCase findProductsByCategoryAndPriceRangeUseCase,
                         FindProductsByCategoryAndPriceRangeManualUseCase findProductsByCategoryAndPriceRangeManualUseCase,
                         ModelMapper modelMapper) {
        this.createProductUseCase = createProductUseCase;
        this.findProductByIdUseCase = findProductByIdUseCase;
        this.findAllProductsUseCase = findAllProductsUseCase;
        this.deleteProductByIdUseCase = deleteProductByIdUseCase;
        this.findProductsByCategoryUseCase = findProductsByCategoryUseCase;
        this.findProductsOnPromotionUseCase = findProductsOnPromotionUseCase;
        this.findProductsByNameUseCase = findProductsByNameUseCase;
        this.findProductsByCategoryAndPriceRangeUseCase = findProductsByCategoryAndPriceRangeUseCase;
        this.findProductsByCategoryAndPriceRangeManualUseCase = findProductsByCategoryAndPriceRangeManualUseCase;
        this.modelMapper = modelMapper;
    }

    public ProductResponseDTO createProduct(CreateProductRequestDTO request) {
        Product product = modelMapper.map(request, Product.class);
        Product savedProduct = createProductUseCase.execute(product);
        return convertToResponseDTO(savedProduct);
    }

    public Optional<ProductResponseDTO> findById(String id) {
        return findProductByIdUseCase.execute(id)
                .map(this::convertToResponseDTO);
    }

    public List<ProductResponseDTO> findAll() {
        return findAllProductsUseCase.execute()
                .stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    public void deleteById(String id) {
        deleteProductByIdUseCase.execute(id);
    }

    public List<ProductResponseDTO> findByCategory(String category) {
        return findProductsByCategoryUseCase.execute(category)
                .stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    public List<ProductResponseDTO> findProductsOnPromotion() {
        return findProductsOnPromotionUseCase.execute()
                .stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    public List<ProductResponseDTO> findByName(String name) {
        return findProductsByNameUseCase.execute(name)
                .stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    public List<ProductResponseDTO> findByCategoryAndPriceRange(String category, BigDecimal minPrice, BigDecimal maxPrice) {
        return findProductsByCategoryAndPriceRangeUseCase.execute(category, minPrice, maxPrice)
                .stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    public List<ProductResponseDTO> findByCategoryAndPriceRangeManual(String category, BigDecimal minPrice, BigDecimal maxPrice) {
        return findProductsByCategoryAndPriceRangeManualUseCase.execute(category, minPrice, maxPrice)
                .stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    private ProductResponseDTO convertToResponseDTO(Product product) {
        ProductResponseDTO dto = modelMapper.map(product, ProductResponseDTO.class);
        dto.setEffectivePrice(product.getEffectivePrice());
        return dto;
    }
}