package com.company.core.services.impl;

import com.company.core.models.EntityNotFoundException;
import com.company.core.models.goods.Product;
import com.company.core.models.goods.Type;
import com.company.core.services.ProductListService;
import com.company.core.services.persistenceservices.ProductListPersistenceService;

import java.math.BigDecimal;
import java.util.List;

public class ProductListServiceImpl implements ProductListService {
    private final ProductListPersistenceService plps;
    private static ProductListService instance;

    private ProductListServiceImpl(ProductListPersistenceService plps) {
        this.plps = plps;
    }

    @Override
    public Product createProduct(String brand, String name, Type type, BigDecimal price, Integer quantity) {
        return new Product(brand, name, type, price, quantity);
    }

    @Override
    public void addProduct(Product product) {
        plps.save(product);
    }

    @Override
    public void deleteProduct(Product product) {
        plps.deleteById(product.getId());
    }

    @Override
    public void updateProduct(Product product) {
        try {
            plps.update(product);
        } catch (EntityNotFoundException e) {
            System.out.println("This product doesn't exist, can't update");
        }
    }

    @Override
    public List<Product> getAllProducts() {
        return plps.findAll();
    }

    @Override
    public Product getProduct(Long id) {
        return plps.findById(id);
    }

    public static ProductListService getInstance(ProductListPersistenceService plps) {
        if (instance == null) {
            instance = new ProductListServiceImpl(plps);
        }
        return instance;
    }
}
