package com.company.core.lists;

import com.company.core.models.goods.Product;
import com.company.core.models.goods.Type;

import java.math.BigDecimal;
import java.util.HashMap;

public class ProductList {
    private final HashMap<Long, Product> productList = new HashMap<>();

    public void printProductList() {
        for (int i = 0; i < productList.size(); i++) {
            System.out.println(i + ". " + productList.get(i));
        }
    }

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
