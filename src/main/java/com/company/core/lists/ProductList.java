package com.company.core.lists;

import com.company.core.models.goods.Product;

import java.util.HashMap;

public class ProductList {
    private final HashMap<Long, Product> productList = new HashMap<>();

    public HashMap<Long, Product> getProductList() {
        return productList;
    }
}
