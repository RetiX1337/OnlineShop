package com.company.core.services;

import com.company.core.models.goods.Product;
import com.company.core.models.goods.Type;

import java.math.BigDecimal;
import java.util.List;

public interface ProductListService {
    Product createProduct(String brand, String name, Type type, BigDecimal price, Integer quantity);
    void addProduct(Product product);
    void deleteProduct(Product product);
    void updateProduct(Product product);
    List<Product> getAllProducts();
    Product getProduct(Long id);
}
