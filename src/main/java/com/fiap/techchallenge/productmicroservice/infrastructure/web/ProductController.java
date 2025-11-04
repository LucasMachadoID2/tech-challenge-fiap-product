package com.fiap.techchallenge.productmicroservice.infrastructure.web;

import com.fiap.techchallenge.productmicroservice.application.dto.CreateProductRequestDTO;
import com.fiap.techchallenge.productmicroservice.application.dto.ProductResponseDTO;
import com.fiap.techchallenge.productmicroservice.application.services.ProductService;
import com.fiap.techchallenge.productmicroservice.domain.entities.CategoryEnum;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/products")
@Tag(name = "Products", description = "API para gerenciamento de produtos")
public class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    @Operation(summary = "Criar um novo produto", description = "Cria um novo produto no sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Produto criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos")
    })
    public ResponseEntity<ProductResponseDTO> createProduct(@Valid @RequestBody CreateProductRequestDTO request) {
        ProductResponseDTO product = productService.createProduct(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(product);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar produto por ID", description = "Retorna um produto específico pelo seu ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Produto encontrado"),
            @ApiResponse(responseCode = "404", description = "Produto não encontrado")
    })
    public ResponseEntity<ProductResponseDTO> findById(@PathVariable String id) {
        Optional<ProductResponseDTO> product = productService.findById(id);
        return product.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    @Operation(summary = "Listar todos os produtos", description = "Retorna lista de todos os produtos")
    @ApiResponse(responseCode = "200", description = "Lista de produtos retornada com sucesso")
    public ResponseEntity<List<ProductResponseDTO>> findAll() {
        List<ProductResponseDTO> products = productService.findAll();
        return ResponseEntity.ok(products);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar produto por ID", description = "Remove um produto do sistema pelo seu ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Produto deletado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Produto não encontrado")
    })
    public ResponseEntity<Void> deleteById(@PathVariable String id) {
        try {
            productService.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/category/{category}")
    @Operation(summary = "Buscar produtos por categoria", description = "Retorna todos os produtos de uma categoria específica")
    @ApiResponse(responseCode = "200", description = "Lista de produtos da categoria retornada com sucesso")
    public ResponseEntity<List<ProductResponseDTO>> findByCategory(@PathVariable CategoryEnum category) {
        List<ProductResponseDTO> products = productService.findByCategory(category);
        return ResponseEntity.ok(products);
    }

    @GetMapping("/search")
    @Operation(summary = "Buscar produtos por nome", description = "Retorna produtos que contenham o termo no nome")
    @ApiResponse(responseCode = "200", description = "Lista de produtos encontrados retornada com sucesso")
    public ResponseEntity<List<ProductResponseDTO>> findByName(
            @Parameter(description = "Termo de busca no nome do produto") 
            @RequestParam String name) {
        List<ProductResponseDTO> products = productService.findByName(name);
        return ResponseEntity.ok(products);
    }

    @GetMapping("/category/{category}/price-range")
    @Operation(summary = "Buscar produtos por categoria e faixa de preço", 
               description = "Retorna produtos de uma categoria dentro de uma faixa de preço")
    @ApiResponse(responseCode = "200", description = "Lista de produtos filtrados retornada com sucesso")
    public ResponseEntity<List<ProductResponseDTO>> findByCategoryAndPriceRange(
            @PathVariable CategoryEnum category,
            @Parameter(description = "Preço mínimo") @RequestParam Long minPrice,
            @Parameter(description = "Preço máximo") @RequestParam Long maxPrice) {
        List<ProductResponseDTO> products = productService.findByCategoryAndPriceRange(category, minPrice, maxPrice);
        return ResponseEntity.ok(products);
    }

    @GetMapping("/category/{category}/price-range-manual")
    @Operation(summary = "Buscar produtos por categoria e faixa de preço (implementação manual)", 
               description = "Retorna produtos de uma categoria dentro de uma faixa de preço usando consulta manual")
    @ApiResponse(responseCode = "200", description = "Lista de produtos filtrados retornada com sucesso")
    public ResponseEntity<List<ProductResponseDTO>> findByCategoryAndPriceRangeManual(
            @PathVariable CategoryEnum category,
            @Parameter(description = "Preço mínimo") @RequestParam Long minPrice,
            @Parameter(description = "Preço máximo") @RequestParam Long maxPrice) {
        List<ProductResponseDTO> products = productService.findByCategoryAndPriceRangeManual(category, minPrice, maxPrice);
        return ResponseEntity.ok(products);
    }
}