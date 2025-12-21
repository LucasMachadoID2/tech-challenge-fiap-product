package com.fiap.techchallenge.productmicroservice.infrastructure.config;

import com.fiap.techchallenge.productmicroservice.domain.entities.CategoryEnum;
import com.fiap.techchallenge.productmicroservice.domain.entities.Product;
import com.fiap.techchallenge.productmicroservice.domain.repositories.ProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class DataLoader {
    
    private static final Logger logger = LoggerFactory.getLogger(DataLoader.class);

    @Bean
    CommandLineRunner initDatabase(ProductRepository repository) {
        return args -> {
            // Verifica se já existem produtos no banco
            if (repository.findAll().isEmpty()) {
                logger.info("Iniciando população do banco de dados com produtos...");
                
                List<Product> products = List.of(
                    // LANCHES
                    new Product(
                        "X-Burger Clássico",
                        "Hambúrguer artesanal 180g, queijo, alface, tomate e molho especial",
                        "https://example.com/images/x-burger.jpg",
                        2890L,
                        2500L,
                        CategoryEnum.LANCHE,
                        50L
                    ),
                    new Product(
                        "X-Bacon",
                        "Hambúrguer 180g, bacon crocante, queijo cheddar e cebola caramelizada",
                        "https://example.com/images/x-bacon.jpg",
                        3190L,
                        2800L,
                        CategoryEnum.LANCHE,
                        40L
                    ),
                    new Product(
                        "X-Tudo",
                        "Hambúrguer duplo, bacon, ovo, presunto, queijo e salada completa",
                        "https://example.com/images/x-tudo.jpg",
                        3990L,
                        3500L,
                        CategoryEnum.LANCHE,
                        30L
                    ),
                    new Product(
                        "X-Salada Premium",
                        "Hambúrguer grelhado, mix de folhas, tomate, pepino e molho iogurte",
                        "https://example.com/images/x-salada.jpg",
                        2690L,
                        2300L,
                        CategoryEnum.LANCHE,
                        45L
                    ),
                    
                    // BEBIDAS
                    new Product(
                        "Refrigerante Lata 350ml",
                        "Refrigerante gelado em lata de 350ml - diversos sabores",
                        "https://example.com/images/refri-lata.jpg",
                        590L,
                        500L,
                        CategoryEnum.BEBIDA,
                        100L
                    ),
                    new Product(
                        "Suco Natural de Laranja 500ml",
                        "Suco 100% natural de laranja, sem açúcar adicionado",
                        "https://example.com/images/suco-laranja.jpg",
                        990L,
                        800L,
                        CategoryEnum.BEBIDA,
                        60L
                    ),
                    new Product(
                        "Água Mineral 500ml",
                        "Água mineral natural sem gás",
                        "https://example.com/images/agua.jpg",
                        390L,
                        300L,
                        CategoryEnum.BEBIDA,
                        150L
                    ),
                    new Product(
                        "Milk Shake 400ml",
                        "Milk shake cremoso - sabores chocolate, morango ou baunilha",
                        "https://example.com/images/milkshake.jpg",
                        1490L,
                        1200L,
                        CategoryEnum.BEBIDA,
                        40L
                    ),
                    
                    // ACOMPANHAMENTOS
                    new Product(
                        "Batata Frita Média",
                        "Batatas fritas crocantes com sal - porção média",
                        "https://example.com/images/batata-media.jpg",
                        990L,
                        800L,
                        CategoryEnum.ACOMPANHAMENTO,
                        70L
                    ),
                    new Product(
                        "Batata Frita Grande",
                        "Batatas fritas crocantes com sal - porção grande",
                        "https://example.com/images/batata-grande.jpg",
                        1490L,
                        1200L,
                        CategoryEnum.ACOMPANHAMENTO,
                        50L
                    ),
                    new Product(
                        "Onion Rings",
                        "Anéis de cebola empanados e fritos até ficarem dourados",
                        "https://example.com/images/onion-rings.jpg",
                        1290L,
                        1000L,
                        CategoryEnum.ACOMPANHAMENTO,
                        45L
                    ),
                    new Product(
                        "Nuggets de Frango 10un",
                        "Pedaços de frango empanados e fritos - 10 unidades",
                        "https://example.com/images/nuggets.jpg",
                        1790L,
                        1500L,
                        CategoryEnum.ACOMPANHAMENTO,
                        60L
                    ),
                    
                    // SOBREMESAS
                    new Product(
                        "Sorvete Casquinha",
                        "Sorvete cremoso na casquinha crocante - diversos sabores",
                        "https://example.com/images/casquinha.jpg",
                        790L,
                        600L,
                        CategoryEnum.SOBREMESA,
                        80L
                    ),
                    new Product(
                        "Torta de Chocolate",
                        "Fatia generosa de torta de chocolate com calda",
                        "https://example.com/images/torta-chocolate.jpg",
                        1190L,
                        900L,
                        CategoryEnum.SOBREMESA,
                        35L
                    ),
                    new Product(
                        "Brownie com Sorvete",
                        "Brownie de chocolate quente com bola de sorvete de baunilha",
                        "https://example.com/images/brownie.jpg",
                        1490L,
                        1200L,
                        CategoryEnum.SOBREMESA,
                        40L
                    ),
                    new Product(
                        "Petit Gateau",
                        "Bolinho de chocolate com recheio cremoso e sorvete",
                        "https://example.com/images/petit-gateau.jpg",
                        1890L,
                        1500L,
                        CategoryEnum.SOBREMESA,
                        30L
                    )
                );
                
                products.forEach(repository::save);
                logger.info("Banco de dados populado com {} produtos!", products.size());
            } else {
                logger.info("Banco de dados já possui produtos. Pulando população inicial.");
            }
        };
    }
}
