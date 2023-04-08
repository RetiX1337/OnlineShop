package com.company.core.services;

import com.company.core.lists.OrderList;
import com.company.core.lists.ProductList;
import com.company.core.models.goods.Product;

public class ProductService {
    private final ProductList productList;
    private static ProductService instance;

    private ProductService(ProductList productList) {
        this.productList = productList;
    }

    public Product getProduct(int id) {
        return productList.getProduct(id);
    }

    public String getProductString(int id) {
        return "Product ID: " + id +
                " Brand: " + getProduct(id).getBrand() +
                " Name: " + getProduct(id).getName() +
                " Type: " + getProduct(id).getType() +
                " Price: " + getProduct(id).getPrice();
    }

    public String getProductListString() {
        String result = "";
        for (int i = 0; i < productList.size(); i++) {
            result = result.concat(i + ". " + getProductString(i) + "\n");
        }
        return result;
    }

    public static ProductService getInstance(ProductList productList) {
        if (instance == null) {
            instance = new ProductService(productList);
        }
        return instance;
    }
}
