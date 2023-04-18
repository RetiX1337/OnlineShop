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

    public void addProduct(String brand, String name, Type type, BigDecimal price) {
        productListServiceImpl.addProduct(productListServiceImpl.createProduct(brand, name, type, price));
    }

    public Product getProduct(Long id) {
        return productListServiceImpl.getProduct(id);
    }

    public void printProduct(Long id) {
        System.out.println(productListServiceImpl.getProductString(id));
    }

    public void printProductList() {
        System.out.println(productListServiceImpl.getProductListString());
    }
}
