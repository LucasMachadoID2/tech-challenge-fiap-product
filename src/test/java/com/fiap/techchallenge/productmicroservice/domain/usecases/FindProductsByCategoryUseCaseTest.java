package com.fiap.techchallenge.productmicroservice.domain.usecases;

import com.fiap.techchallenge.productmicroservice.domain.entities.Product;
import com.fiap.techchallenge.productmicroservice.domain.repositories.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FindProductsByCategoryUseCaseTest {
    @Mock
    private ProductRepository productRepository;
    @InjectMocks
    private FindProductsByCategoryUseCase useCase;

    @Test
    void shouldFindProductsByCategory() {
        Product p1 = new Product();
        p1.setId("1");
        p1.setName("Burger");
        p1.setCategory("LANCHE");
        p1.setPrice(new BigDecimal("25.00"));
        
        when(productRepository.findByCategory("LANCHE")).thenReturn(Arrays.asList(p1));
        List<Product> result = useCase.execute("LANCHE");
        
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("LANCHE", result.get(0).getCategory());
    }
}
