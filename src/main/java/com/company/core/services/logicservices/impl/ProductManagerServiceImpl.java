package com.company.core.services.logicservices.impl;

import com.company.core.models.EntityNotFoundException;
import com.company.core.models.goods.Product;
import com.company.core.models.goods.ProductWithQuantity;
import com.company.core.services.logicservices.ProductManagerService;
import com.company.core.services.logicservices.ProductService;
import com.company.core.services.logicservices.ProductStorageService;

public class ProductManagerServiceImpl implements ProductManagerService {
    private final ProductService productService;
    private final ProductStorageService productStorageService;

    public ProductManagerServiceImpl(ProductService productService, ProductStorageService productStorageService) {
        this.productService = productService;
        this.productStorageService = productStorageService;
    }

    @Override
    public ProductWithQuantity getProductWithQuantity(Long shopId, Long productId) throws EntityNotFoundException {
        Product product = productService.getProduct(productId);
        Integer quantity = productStorageService.getQuantityPerShop(shopId, productId);
        return new ProductWithQuantity(product, quantity);
    }

    @Override
    public boolean removeFromStorage(Integer quantity, Long shopId, Long productId) {
        return productStorageService.removeFromStorage(quantity, shopId, productId);
    }
}
