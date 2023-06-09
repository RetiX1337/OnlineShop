package com.company.core.controllers;

import com.company.core.models.goods.Type;
import com.company.core.services.ProductService;

import java.math.BigDecimal;

public class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    public void addProduct(String brand, String name, Type type, BigDecimal price, Integer quantity) {
        productService.addProduct(productService.createProduct(brand, name, type, price, quantity));
    }
}
