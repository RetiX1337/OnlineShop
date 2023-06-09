package com.company.core.services;

import com.company.core.models.EntityNotFoundException;
import com.company.core.models.goods.Product;
import com.company.core.models.goods.Type;

import java.math.BigDecimal;
import java.util.List;

public interface ProductService {
    Product createProduct(String brand, String name, Type type, BigDecimal price, Integer quantity);
    void addProduct(Product product);
    void deleteProduct(Long id) throws EntityNotFoundException;
    Product updateProduct(Product product, Long id) throws EntityNotFoundException;
    Product getProduct(Long id) throws EntityNotFoundException;
    List<Product> getAllProducts();
}