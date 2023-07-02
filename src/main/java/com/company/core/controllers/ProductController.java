package com.company.core.controllers;

import com.company.core.models.goods.Product;
import com.company.core.models.goods.ProductType;
import com.company.core.models.goods.ProductWithQuantity;
import com.company.core.services.logicservices.ProductService;
import com.company.core.services.logicservices.StorageService;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ProductController {
    private final ProductService productService;
    private final StorageService storageService;

    public ProductController(ProductService productService, StorageService storageService) {
        this.productService = productService;
        this.storageService = storageService;
    }

    public void addProduct(String brand, String name, ProductType productType, BigDecimal price) {
        productService.addProduct(productService.createProduct(brand, name, productType, price));
    }

    public Collection<ProductWithQuantity> getProducts(Long shopId) {
        List<Product> products = productService.getAllProducts();
        List<ProductWithQuantity> productsWithQuantity = new ArrayList<>();

        for (Product product : products) {
            productsWithQuantity.add(new ProductWithQuantity(product, storageService.getQuantityPerShop(shopId, product.getId())));
        }

        return productsWithQuantity;
    }
}
