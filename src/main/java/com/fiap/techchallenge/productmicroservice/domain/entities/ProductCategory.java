package com.fiap.techchallenge.productmicroservice.domain.entities;

public final class ProductCategory {
    public static final String LANCHE = "LANCHE";
    public static final String ACOMPANHAMENTO = "ACOMPANHAMENTO";
    public static final String BEBIDA = "BEBIDA";
    public static final String SOBREMESA = "SOBREMESA";

    private ProductCategory() {
    }

    public static boolean isValidCategory(String category) {
        return LANCHE.equals(category) || 
               ACOMPANHAMENTO.equals(category) || 
               BEBIDA.equals(category) || 
               SOBREMESA.equals(category);
    }

    public static String[] getAllCategories() {
        return new String[]{LANCHE, ACOMPANHAMENTO, BEBIDA, SOBREMESA};
    }
}