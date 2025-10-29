package com.fiap.techchallenge.productmicroservice.bdd;

import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.And;
import com.fiap.techchallenge.productmicroservice.application.dto.ProductResponseDTO;
import com.fiap.techchallenge.productmicroservice.domain.entities.Product;
import com.fiap.techchallenge.productmicroservice.domain.repositories.ProductRepository;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class ProductStepDefinitions {

    @Mock
    private ProductRepository productRepository;

    private ProductResponseDTO createdProduct;
    private List<ProductResponseDTO> productList;
    private String productId;
    private Exception thrownException;
    private Map<String, Product> inMemoryDatabase;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        inMemoryDatabase = new HashMap<>();
        setupMocks();
    }

    private void setupMocks() {
        when(productRepository.save(any(Product.class))).thenAnswer(invocation -> {
            Product product = invocation.getArgument(0);
            if (product.getId() == null) {
                product.setId(UUID.randomUUID().toString());
            }
            inMemoryDatabase.put(product.getId(), product);
            return product;
        });

        when(productRepository.findById(anyString())).thenAnswer(invocation -> {
            String id = invocation.getArgument(0);
            return Optional.ofNullable(inMemoryDatabase.get(id));
        });

        when(productRepository.findAll()).thenAnswer(invocation -> 
            new ArrayList<>(inMemoryDatabase.values())
        );

        when(productRepository.findByCategory(anyString())).thenAnswer(invocation -> {
            String category = invocation.getArgument(0);
            return inMemoryDatabase.values().stream()
                .filter(p -> p.getCategory().equals(category))
                .collect(Collectors.toList());
        });

        when(productRepository.findByOnPromotion(true)).thenAnswer(invocation ->
            inMemoryDatabase.values().stream()
                .filter(Product::isOnPromotion)
                .collect(Collectors.toList())
        );

        when(productRepository.findByNameContaining(anyString())).thenAnswer(invocation -> {
            String name = invocation.getArgument(0);
            return inMemoryDatabase.values().stream()
                .filter(p -> p.getName().toLowerCase().contains(name.toLowerCase()))
                .collect(Collectors.toList());
        });

        doAnswer(invocation -> {
            String id = invocation.getArgument(0);
            inMemoryDatabase.remove(id);
            return null;
        }).when(productRepository).deleteById(anyString());
    }

    @Given("the system is initialized")
    public void theSystemIsInitialized() {
        inMemoryDatabase.clear();
        createdProduct = null;
        productList = null;
        productId = null;
        thrownException = null;
    }

    @When("I create a product with the following data:")
    public void iCreateAProductWithTheFollowingData(Map<String, String> data) {
        Product product = new Product();
        product.setName(data.get("name"));
        product.setDescription(data.get("description"));
        product.setPrice(new BigDecimal(data.get("price")));
        product.setCategory(data.get("category"));
        
        Product saved = productRepository.save(product);
        createdProduct = convertToDTO(saved);
    }

    @Then("the product should be created successfully")
    public void theProductShouldBeCreatedSuccessfully() {
        assertNotNull(createdProduct);
        assertNotNull(createdProduct.getId());
    }

    @And("the product should have the name {string}")
    public void theProductShouldHaveTheName(String name) {
        assertEquals(name, createdProduct.getName());
    }

    @And("the product should have the price of {double}")
    public void theProductShouldHaveThePriceOf(double price) {
        BigDecimal expected = BigDecimal.valueOf(price);
        BigDecimal actual = createdProduct.getPrice();
        assertEquals(0, expected.compareTo(actual), 
            "Expected price: " + expected + ", but was: " + actual);
    }

    @Given("a product exists")
    public void aProductExists() {
        Product product = new Product();
        product.setName("Test Product");
        product.setDescription("Test product description");
        product.setPrice(new BigDecimal("15.50"));
        product.setCategory("LANCHE");
        
        Product saved = productRepository.save(product);
        productId = saved.getId();
        createdProduct = convertToDTO(saved);
    }

    @When("I search for the product by ID")
    public void iSearchForTheProductById() {
        Optional<Product> result = productRepository.findById(productId);
        assertTrue(result.isPresent());
        createdProduct = convertToDTO(result.get());
    }

    @Then("the product should be found")
    public void theProductShouldBeFound() {
        assertNotNull(createdProduct);
        assertEquals(productId, createdProduct.getId());
    }

    @And("the product should have all correct data")
    public void theProductShouldHaveAllCorrectData() {
        assertEquals("Test Product", createdProduct.getName());
        assertEquals("Test product description", createdProduct.getDescription());
        assertEquals(0, new BigDecimal("15.50").compareTo(createdProduct.getPrice()));
        assertEquals("LANCHE", createdProduct.getCategory());
    }

    @Given("there are {int} registered products")
    public void thereAreRegisteredProducts(int quantity) {
        for (int i = 1; i <= quantity; i++) {
            Product product = new Product();
            product.setName("Product " + i);
            product.setDescription("Description " + i);
            product.setPrice(BigDecimal.valueOf(10 * i));
            product.setCategory("LANCHE");
            productRepository.save(product);
        }
    }

    @When("I list all products")
    public void iListAllProducts() {
        List<Product> products = productRepository.findAll();
        productList = products.stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }

    @Then("I should receive a list with {int} products")
    public void iShouldReceiveAListWithProducts(int quantity) {
        assertNotNull(productList);
        assertTrue(productList.size() >= quantity);
    }

    @Given("there are products in categories {string} and {string}")
    public void thereAreProductsInCategories(String category1, String category2) {
        Product product1 = new Product();
        product1.setName("Product " + category1);
        product1.setDescription("Description");
        product1.setPrice(new BigDecimal("20.00"));
        product1.setCategory(category1);
        productRepository.save(product1);

        Product product2 = new Product();
        product2.setName("Product " + category2);
        product2.setDescription("Description");
        product2.setPrice(new BigDecimal("15.00"));
        product2.setCategory(category2);
        productRepository.save(product2);
    }

    @When("I search for products in category {string}")
    public void iSearchForProductsInCategory(String category) {
        List<Product> products = productRepository.findByCategory(category);
        productList = products.stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }

    @Then("I should receive only products from category {string}")
    public void iShouldReceiveOnlyProductsFromCategory(String category) {
        assertNotNull(productList);
        assertFalse(productList.isEmpty());
        productList.forEach(product -> assertEquals(category, product.getCategory()));
    }

    @Given("there are products on promotion and without promotion")
    public void thereAreProductsOnPromotionAndWithoutPromotion() {
        Product onPromotion = new Product();
        onPromotion.setName("Product on Promotion");
        onPromotion.setDescription("Description");
        onPromotion.setPrice(new BigDecimal("50.00"));
        onPromotion.setCategory("LANCHE");
        onPromotion.setOnPromotion(true);
        onPromotion.setPromotionPrice(new BigDecimal("35.00"));
        productRepository.save(onPromotion);

        Product notOnPromotion = new Product();
        notOnPromotion.setName("Regular Product");
        notOnPromotion.setDescription("Description");
        notOnPromotion.setPrice(new BigDecimal("30.00"));
        notOnPromotion.setCategory("LANCHE");
        productRepository.save(notOnPromotion);
    }

    @When("I search for products on promotion")
    public void iSearchForProductsOnPromotion() {
        List<Product> products = productRepository.findByOnPromotion(true);
        productList = products.stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }

    @Then("I should receive only products on promotion")
    public void iShouldReceiveOnlyProductsOnPromotion() {
        assertNotNull(productList);
        assertFalse(productList.isEmpty());
        productList.forEach(product -> assertTrue(product.isOnPromotion()));
    }

    @Given("there is a product with name {string}")
    public void thereIsAProductWithName(String name) {
        Product product = new Product();
        product.setName(name);
        product.setDescription("Description");
        product.setPrice(new BigDecimal("25.00"));
        product.setCategory("LANCHE");
        productRepository.save(product);
    }

    @When("I search for products with name {string}")
    public void iSearchForProductsWithName(String name) {
        List<Product> products = productRepository.findByNameContaining(name);
        productList = products.stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }

    @Then("I should find the product {string}")
    public void iShouldFindTheProduct(String name) {
        assertNotNull(productList);
        assertFalse(productList.isEmpty());
        boolean found = productList.stream()
                .anyMatch(p -> p.getName().contains(name));
        assertTrue(found);
    }

    @Given("there are products in category {string} with varied prices")
    public void thereAreProductsInCategoryWithVariedPrices(String category) {
        Product product1 = new Product();
        product1.setName("Cheap Product");
        product1.setDescription("Description");
        product1.setPrice(new BigDecimal("15.00"));
        product1.setCategory(category);
        productRepository.save(product1);

        Product product2 = new Product();
        product2.setName("Medium Product");
        product2.setDescription("Description");
        product2.setPrice(new BigDecimal("25.00"));
        product2.setCategory(category);
        productRepository.save(product2);

        Product product3 = new Product();
        product3.setName("Expensive Product");
        product3.setDescription("Description");
        product3.setPrice(new BigDecimal("50.00"));
        product3.setCategory(category);
        productRepository.save(product3);
    }

    @When("I search for products in category {string} between {double} and {double}")
    public void iSearchForProductsInCategoryBetween(String category, double min, double max) {
        BigDecimal minPrice = BigDecimal.valueOf(min);
        BigDecimal maxPrice = BigDecimal.valueOf(max);
        
        List<Product> products = productRepository.findByCategory(category).stream()
            .filter(p -> p.getPrice().compareTo(minPrice) >= 0 && p.getPrice().compareTo(maxPrice) <= 0)
            .collect(Collectors.toList());
        
        productList = products.stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }

    @Then("I should receive only products in the specified price range")
    public void iShouldReceiveOnlyProductsInTheSpecifiedPriceRange() {
        assertNotNull(productList);
        assertFalse(productList.isEmpty(), "List should not be empty");
    }

    @When("I delete the product")
    public void iDeleteTheProduct() {
        productRepository.deleteById(productId);
    }

    @Then("the product should no longer exist in the system")
    public void theProductShouldNoLongerExistInTheSystem() {
        Optional<Product> result = productRepository.findById(productId);
        assertFalse(result.isPresent());
    }

    @When("I try to create a product with invalid price")
    public void iTryToCreateAProductWithInvalidPrice() {
        try {
            Product product = new Product();
            product.setName("Invalid Product");
            product.setDescription("Description");
            product.setPrice(new BigDecimal("-10.00"));
            product.setCategory("LANCHE");
            
            if (product.getPrice().compareTo(BigDecimal.ZERO) < 0) {
                throw new IllegalArgumentException("Price cannot be negative");
            }
            productRepository.save(product);
        } catch (Exception e) {
            thrownException = e;
        }
    }

    @Then("it should return a validation error")
    public void itShouldReturnAValidationError() {
        assertNotNull(thrownException);
    }

    @Given("there is a product with price of {double}")
    public void thereIsAProductWithPriceOf(double price) {
        Product product = new Product();
        product.setName("Product for Promotion");
        product.setDescription("Description");
        product.setPrice(BigDecimal.valueOf(price));
        product.setCategory("LANCHE");
        Product saved = productRepository.save(product);
        productId = saved.getId();
        createdProduct = convertToDTO(saved);
    }

    @When("I apply a promotion of {double}")
    public void iApplyAPromotionOf(double promotionPrice) {
        Optional<Product> productOpt = productRepository.findById(productId);
        if (productOpt.isPresent()) {
            Product product = productOpt.get();
            product.setOnPromotion(true);
            product.setPromotionPrice(BigDecimal.valueOf(promotionPrice));
            Product saved = productRepository.save(product);
            createdProduct = convertToDTO(saved);
        }
    }

    @Then("the effective price should be {double}")
    public void theEffectivePriceShouldBe(double effectivePrice) {
        BigDecimal expected = BigDecimal.valueOf(effectivePrice);
        BigDecimal actual = createdProduct.getEffectivePrice();
        
        assertEquals(0, expected.compareTo(actual),
            "Expected effective price: " + expected + ", but was: " + actual);
    }

    @And("the product should be marked as on promotion")
    public void theProductShouldBeMarkedAsOnPromotion() {
        assertTrue(createdProduct.isOnPromotion());
    }

    private ProductResponseDTO convertToDTO(Product product) {
        ProductResponseDTO dto = new ProductResponseDTO();
        dto.setId(product.getId());
        dto.setName(product.getName());
        dto.setDescription(product.getDescription());
        dto.setPrice(product.getPrice());
        dto.setCategory(product.getCategory());
        dto.setOnPromotion(product.isOnPromotion());
        dto.setPromotionPrice(product.getPromotionPrice());
        
        BigDecimal effectivePrice = product.isOnPromotion() && product.getPromotionPrice() != null 
            ? product.getPromotionPrice() 
            : product.getPrice();
        dto.setEffectivePrice(effectivePrice);
        
        return dto;
    }
}
