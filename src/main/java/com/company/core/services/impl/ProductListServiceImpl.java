package com.company.core.services.impl;

import com.company.Observer;
import com.company.core.ProductListObserver;
import com.company.core.models.EntityNotFoundException;
import com.company.core.models.goods.Product;
import com.company.core.models.goods.Type;
import com.company.core.services.persistenceservices.ProductListPersistenceService;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class ProductListServiceImpl {
    private final ProductListPersistenceService plps;
    private final ProductListObserver productListObserver;
    private static ProductListServiceImpl instance;

    private ProductListServiceImpl(ProductListPersistenceService plps, ProductListObserver productListObserver) {
        this.plps = plps;
        this.productListObserver = productListObserver;
    }

    public Product createProduct(String brand, String name, Type type, BigDecimal price) {
        return new Product(brand, name, type, price);
    }

    public void addProduct(Product product) {
        productListObserver.notifyObservers(plps.save(product));
    }

    public void deleteProduct(Product product) {
        plps.deleteById(product.getId());
        productListObserver.notifyObservers(product);
    }

    public void updateProduct(Product product) {
        try {
            plps.update(product);
        } catch (EntityNotFoundException e) {
            System.out.println("This product doesn't exist, can't update");
        }
    }

    public boolean productIsPresent(Product product) {
        return plps.productIsPresent(product);
    }

    public Product getProduct(Long id) {
        return plps.findById(id);
    }

    public String getProductString(Long id) {
        return "Product ID: " + id +
                " Brand: " + getProduct(id).getBrand() +
                " Name: " + getProduct(id).getName() +
                " Type: " + getProduct(id).getType() +
                " Price: " + getProduct(id).getPrice();
    }

    public String getProductListString() {
        String result = "";
        for (int i = 0; i < plps.findAll().size(); i++) {
            result = result.concat(i + ". " + getProductString((long) i) + "\n");
        }
        return result;
    }

    public static ProductListServiceImpl getInstance(ProductListPersistenceService plps, ProductListObserver productListObserver) {
        if (instance == null) {
            instance = new ProductListServiceImpl(plps, productListObserver);
        }
        return instance;
    }
}
