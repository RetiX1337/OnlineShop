package com.company.core.lists;

import com.company.core.models.goods.Product;

import java.util.HashMap;

public class ProductList {
    private final HashMap<Long, Product> productList = new HashMap<>();

    public Product getProduct(Long id) {
        return productList.get(id);
    }

    public int size() {
        return productList.size();
    }

    public HashMap<Long, Product> getProductList() {
        return productList;
    }
}
