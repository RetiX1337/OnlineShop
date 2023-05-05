package com.company.core.services.impl;

import com.company.core.models.EntityNotFoundException;
import com.company.core.models.goods.Product;
import com.company.core.models.goods.Type;
import com.company.core.services.ProductListService;
import com.company.core.services.persistenceservices.ProductPersistenceService;

import java.math.BigDecimal;
import java.util.List;

public class ProductServiceImpl implements ProductListService {
    private final ProductPersistenceService pps;

    public ProductServiceImpl(ProductPersistenceService pps) {
        this.pps = pps;
    }

    @Override
    public Product createProduct(String brand, String name, Type type, BigDecimal price, Integer quantity) {
        return new Product(brand, name, type, price, quantity);
    }

    @Override
    public void addProduct(Product product) {
        pps.save(product);
    }

    @Override
    public void deleteProduct(Product product) {
        pps.deleteById(product.getId());
    }

    @Override
    public void updateProduct(Product product) {
        try {
            pps.update(product);
        } catch (EntityNotFoundException e) {
            System.out.println("This product doesn't exist, can't update");
        }
    }

    @Override
    public List<Product> getAllProducts() {
        return pps.findAll();
    }

    @Override
    public Product getProduct(Long id) {
        return pps.findById(id);
    }
}
