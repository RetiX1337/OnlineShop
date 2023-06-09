package com.company.core.services.impl;

import com.company.core.models.EntityNotFoundException;
import com.company.core.models.goods.Product;
import com.company.core.models.goods.ProductBase;
import com.company.core.models.goods.ProductWithQuantity;
import com.company.core.models.goods.Type;
import com.company.core.services.ProductService;
import com.company.core.services.persistenceservices.ProductPersistenceService;

import java.math.BigDecimal;
import java.util.List;

public class ProductServiceImpl implements ProductService {
    private final ProductPersistenceService productPersistenceService;

    public ProductServiceImpl(ProductPersistenceService productPersistenceService) {
        this.productPersistenceService = productPersistenceService;
    }

    @Override
    public Product createProduct(String brand, String name, Type type, BigDecimal price, Integer quantity) {
        return new ProductWithQuantity(new ProductBase(brand, name, type, price), quantity);
    }

    @Override
    public void addProduct(Product product) {
        productPersistenceService.save(product);
    }

    @Override
    public void deleteProduct(Long id) throws EntityNotFoundException {
        if (productPersistenceService.isPresent(id)) {
            productPersistenceService.deleteById(id);
        } else {
            throw new EntityNotFoundException();
        }
    }

    @Override
    public Product updateProduct(Product product, Long id) throws EntityNotFoundException {
        if (productPersistenceService.isPresent(id)) {
            return productPersistenceService.update(product, id);
        } else {
            throw new EntityNotFoundException();
        }
    }

    @Override
    public List<Product> getAllProducts() {
        return productPersistenceService.findAll();
    }

    @Override
    public Product getProduct(Long id) throws EntityNotFoundException {
        if (productPersistenceService.isPresent(id)) {
            return productPersistenceService.findById(id);
        } else {
            throw new EntityNotFoundException();
        }
    }
}
