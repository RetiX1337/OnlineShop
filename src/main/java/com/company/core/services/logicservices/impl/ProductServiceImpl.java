package com.company.core.services.logicservices.impl;

import com.company.core.PersistenceServiceBeanFactory;
import com.company.core.models.goods.Product;
import com.company.core.models.goods.ProductBase;
import com.company.core.models.goods.ProductType;
import com.company.core.services.logicservices.ProductService;
import com.company.core.services.persistenceservices.PersistenceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
@Service
public class ProductServiceImpl implements ProductService {
    private final PersistenceInterface<ProductBase> productPersistenceService;


    @Autowired
    public ProductServiceImpl(@Autowired PersistenceServiceBeanFactory persistenceServiceBeanFactory) {
        this.productPersistenceService = persistenceServiceBeanFactory.getPersistenceBean(ProductBase.class);
    }

    public ProductServiceImpl(PersistenceInterface<ProductBase> productPersistenceService) {
        this.productPersistenceService = productPersistenceService;
    }

    @Override
    public ProductBase createProduct(String brand, String name, ProductType productType, BigDecimal price) {
        return new ProductBase(brand, name, productType, price);
    }

    @Override
    public void addProduct(ProductBase product) {
        productPersistenceService.save(product);
    }

    @Override
    public void deleteProduct(Long id) {
        productPersistenceService.deleteById(id);
    }

    @Override
    public ProductBase updateProduct(ProductBase product, Long id) {
        product.setId(id);
        return productPersistenceService.update(product);
    }

    @Override
    public List<ProductBase> getAllProducts() {
        return productPersistenceService.findAll();
    }

    @Override
    public ProductBase getProduct(Long id) {
        return productPersistenceService.findById(id);
    }


}
