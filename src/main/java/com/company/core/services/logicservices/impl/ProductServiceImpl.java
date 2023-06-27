package com.company.core.services.logicservices.impl;

import com.company.core.models.EntityNotFoundException;
import com.company.core.models.goods.Product;
import com.company.core.models.goods.ProductBase;
import com.company.core.models.goods.ProductType;
import com.company.core.services.logicservices.ProductService;
import com.company.core.services.persistenceservices.PersistenceInterface;

import java.math.BigDecimal;
import java.util.List;

public class ProductServiceImpl implements ProductService {
    private final PersistenceInterface<Product> productPersistenceService;


    public ProductServiceImpl(PersistenceInterface<Product> productPersistenceService) {
        this.productPersistenceService = productPersistenceService;
    }

    @Override
    public Product createProduct(String brand, String name, ProductType productType, BigDecimal price) {
        return new ProductBase(brand, name, productType, price);
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
