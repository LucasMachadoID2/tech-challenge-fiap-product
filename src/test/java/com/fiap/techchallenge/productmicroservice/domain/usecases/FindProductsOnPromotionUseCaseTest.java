package com.fiap.techchallenge.productmicroservice.domain.usecases;

import com.fiap.techchallenge.productmicroservice.domain.entities.Product;
import com.fiap.techchallenge.productmicroservice.domain.repositories.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FindProductsOnPromotionUseCaseTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private FindProductsOnPromotionUseCase useCase;

    private Product productOnPromotion1;
    private Product productOnPromotion2;

    @BeforeEach
    void setUp() {
        productOnPromotion1 = new Product("Burger Combo", "Special combo", new BigDecimal("50.00"), "LANCHE");
        productOnPromotion1.setId("1");
        productOnPromotion1.applyPromotion(new BigDecimal("35.00"));

        productOnPromotion2 = new Product("Pizza", "Large pizza", new BigDecimal("60.00"), "LANCHE");
        productOnPromotion2.setId("2");
        productOnPromotion2.applyPromotion(new BigDecimal("45.00"));
    }

    @Test
    @DisplayName("Should find all products on promotion")
    void shouldFindAllProductsOnPromotion() {
        when(productRepository.findByOnPromotion(true))
                .thenReturn(Arrays.asList(productOnPromotion1, productOnPromotion2));

        List<Product> result = useCase.execute();

        assertThat(result).isNotNull();
        assertThat(result).hasSize(2);
        assertThat(result.get(0).isOnPromotion()).isTrue();
        assertThat(result.get(1).isOnPromotion()).isTrue();
        assertThat(result.get(0).getPromotionPrice()).isEqualByComparingTo(new BigDecimal("35.00"));
        assertThat(result.get(1).getPromotionPrice()).isEqualByComparingTo(new BigDecimal("45.00"));
        verify(productRepository, times(1)).findByOnPromotion(true);
    }

    @Test
    @DisplayName("Should return empty list when no products on promotion")
    void shouldReturnEmptyListWhenNoProductsOnPromotion() {
        when(productRepository.findByOnPromotion(true))
                .thenReturn(Collections.emptyList());

        List<Product> result = useCase.execute();

        assertThat(result).isNotNull();
        assertThat(result).isEmpty();
        verify(productRepository, times(1)).findByOnPromotion(true);
    }

    @Test
    @DisplayName("Should find single product on promotion")
    void shouldFindSingleProductOnPromotion() {
        when(productRepository.findByOnPromotion(true))
                .thenReturn(Collections.singletonList(productOnPromotion1));

        List<Product> result = useCase.execute();

        assertThat(result).isNotNull();
        assertThat(result).hasSize(1);
        assertThat(result.get(0).isOnPromotion()).isTrue();
        assertThat(result.get(0).getEffectivePrice()).isEqualByComparingTo(new BigDecimal("35.00"));
        verify(productRepository, times(1)).findByOnPromotion(true);
    }

    @Test
    @DisplayName("Should verify effective price for products on promotion")
    void shouldVerifyEffectivePriceForProductsOnPromotion() {
        when(productRepository.findByOnPromotion(true))
                .thenReturn(Arrays.asList(productOnPromotion1, productOnPromotion2));

        List<Product> result = useCase.execute();

        assertThat(result).isNotNull();
        assertThat(result).allSatisfy(product -> {
            assertThat(product.isOnPromotion()).isTrue();
            assertThat(product.getEffectivePrice()).isLessThan(product.getPrice());
            assertThat(product.getPromotionPrice()).isNotNull();
        });
        verify(productRepository, times(1)).findByOnPromotion(true);
    }
}
