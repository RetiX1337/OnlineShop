package com.company.core.controllers;

import com.company.core.models.goods.ProductType;
import com.company.core.services.logicservices.ProductService;

import java.math.BigDecimal;

public class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    public void addProduct(String brand, String name, ProductType productType, BigDecimal price, Integer quantity) {
        productService.addProduct(productService.createProduct(brand, name, productType, price, quantity));
    }
}
