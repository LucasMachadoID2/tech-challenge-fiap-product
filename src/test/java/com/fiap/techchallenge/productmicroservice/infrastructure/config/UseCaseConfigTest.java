package com.fiap.techchallenge.productmicroservice.infrastructure.config;

import com.fiap.techchallenge.productmicroservice.domain.repositories.ProductRepository;
import com.fiap.techchallenge.productmicroservice.domain.usecases.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@DisplayName("UseCaseConfig Tests")
class UseCaseConfigTest {

    private UseCaseConfig useCaseConfig;

    @Mock
    private ProductRepository productRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        useCaseConfig = new UseCaseConfig();
    }

    @Test
    @DisplayName("Should create ModelMapper bean")
    void shouldCreateModelMapperBean() {
        ModelMapper modelMapper = useCaseConfig.modelMapper();
        assertNotNull(modelMapper);
    }

    @Test
    @DisplayName("Should create CreateProductUseCase bean")
    void shouldCreateCreateProductUseCaseBean() {
        CreateProductUseCase useCase = useCaseConfig.createProductUseCase(productRepository);
        assertNotNull(useCase);
    }

    @Test
    @DisplayName("Should create FindProductByIdUseCase bean")
    void shouldCreateFindProductByIdUseCaseBean() {
        FindProductByIdUseCase useCase = useCaseConfig.findProductByIdUseCase(productRepository);
        assertNotNull(useCase);
    }

    @Test
    @DisplayName("Should create FindAllProductsUseCase bean")
    void shouldCreateFindAllProductsUseCaseBean() {
        FindAllProductsUseCase useCase = useCaseConfig.findAllProductsUseCase(productRepository);
        assertNotNull(useCase);
    }

    @Test
    @DisplayName("Should create DeleteProductByIdUseCase bean")
    void shouldCreateDeleteProductByIdUseCaseBean() {
        DeleteProductByIdUseCase useCase = useCaseConfig.deleteProductByIdUseCase(productRepository);
        assertNotNull(useCase);
    }

    @Test
    @DisplayName("Should create FindProductsByCategoryUseCase bean")
    void shouldCreateFindProductsByCategoryUseCaseBean() {
        FindProductsByCategoryUseCase useCase = useCaseConfig.findProductsByCategoryUseCase(productRepository);
        assertNotNull(useCase);
    }

    @Test
    @DisplayName("Should create FindProductsByNameUseCase bean")
    void shouldCreateFindProductsByNameUseCaseBean() {
        FindProductsByNameUseCase useCase = useCaseConfig.findProductsByNameUseCase(productRepository);
        assertNotNull(useCase);
    }

    @Test
    @DisplayName("Should create FindProductsByCategoryAndPriceRangeUseCase bean")
    void shouldCreateFindProductsByCategoryAndPriceRangeUseCaseBean() {
        FindProductsByCategoryAndPriceRangeUseCase useCase = 
            useCaseConfig.findProductsByCategoryAndPriceRangeUseCase(productRepository);
        assertNotNull(useCase);
    }
}
