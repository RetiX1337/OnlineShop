package com.company.core.controllers;

import com.company.core.models.goods.Type;
import com.company.core.services.ProductListService;

import java.math.BigDecimal;

public class ProductController {
    private final ProductListService productListService;

    public ProductController(ProductListService productListService) {
        this.productListService = productListService;
    }

    public void addProduct(String brand, String name, Type type, BigDecimal price, Integer quantity) {
        productListService.addProduct(productListService.createProduct(brand, name, type, price, quantity));
    }
}
