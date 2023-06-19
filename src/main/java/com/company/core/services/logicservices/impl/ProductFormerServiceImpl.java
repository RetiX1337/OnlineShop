package com.company.core.services.logicservices.impl;

import com.company.core.models.EntityNotFoundException;
import com.company.core.models.goods.Product;
import com.company.core.models.goods.ProductBase;
import com.company.core.models.goods.ProductWithQuantity;
import com.company.core.services.logicservices.ProductFormerService;
import com.company.core.services.logicservices.ProductService;
import com.company.core.services.logicservices.ProductStorageService;

public class ProductFormerServiceImpl implements ProductFormerService {
    private final ProductService productService;
    private final ProductStorageService productStorageService;

    public ProductFormerServiceImpl(ProductService productService, ProductStorageService productStorageService) {
        this.productService = productService;
        this.productStorageService = productStorageService;
    }

    public ProductWithQuantity getProductWithQuantity(Long productId) throws EntityNotFoundException {
        Product product = productService.getProduct(productId);
        Integer quantity = productStorageService.getQuantity(productId);
        return new ProductWithQuantity(product, quantity);
    }
}
