package com.company.core.services.logicservices;

import com.company.core.models.EntityNotFoundException;
import com.company.core.models.goods.Product;
import com.company.core.models.goods.ProductType;

import java.math.BigDecimal;
import java.util.List;

public interface ProductService {
    Product createProduct(String brand, String name, ProductType productType, BigDecimal price);
    void addProduct(Product product);
    void deleteProduct(Long id) throws EntityNotFoundException;
    Product updateProduct(Product product, Long id) throws EntityNotFoundException;
    Product getProduct(Long id) throws EntityNotFoundException;
    List<Product> getAllProducts();
}
