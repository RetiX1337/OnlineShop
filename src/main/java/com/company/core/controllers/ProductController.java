package com.company.core.controllers;

import com.company.core.models.goods.Product;
import com.company.core.services.ProductService;

public class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    public Product getProduct(int id) {
        return productService.getProduct(id);
    }

    public void printProduct(int id) {
        System.out.println(productService.getProductString(id));
    }

    public void printProductList() {
        System.out.println(productService.getProductListString());
    }
}
