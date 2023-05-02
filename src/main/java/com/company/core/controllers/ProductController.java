package com.company.core.controllers;

import com.company.core.models.goods.Product;
import com.company.core.models.goods.Type;
import com.company.core.services.impl.ProductListServiceImpl;

import java.math.BigDecimal;

public class ProductController {
    private final ProductListServiceImpl productListServiceImpl;

    public ProductController(ProductListServiceImpl productListServiceImpl) {
        this.productListServiceImpl = productListServiceImpl;
    }

    public void addProduct(String brand, String name, Type type, BigDecimal price, Integer quantity) {
        productListServiceImpl.addProduct(productListServiceImpl.createProduct(brand, name, type, price, quantity));
    }
}
