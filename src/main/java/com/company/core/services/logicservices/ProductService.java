package com.company.core.services.logicservices;

import com.company.core.models.goods.Product;
import com.company.core.models.goods.ProductBase;
import com.company.core.models.goods.ProductType;

import java.math.BigDecimal;
import java.util.List;

public interface ProductService {
    ProductBase createProduct(String brand, String name, ProductType productType, BigDecimal price);
    void addProduct(ProductBase product);
    void deleteProduct(Long id);
    ProductBase updateProduct(ProductBase product, Long id);
    ProductBase getProduct(Long id);
    List<ProductBase> getAllProducts();
}
