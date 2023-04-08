package com.company.core.lists;

import com.company.core.models.goods.Good;
import com.company.core.models.goods.Product;

import java.util.*;

public class GoodList {
    private final ProductList productList;
    private final HashMap<Product, Stack<Good>> goodList = new HashMap<>();

    public GoodList(ProductList productList) {
        this.productList = productList;
        for (int i = 0; i < productList.getProductList().size(); i++) {
            goodList.put(productList.getProductList().get(i), new Stack<>());
        }
    }

    public void save(Good good) {
        goodList.get(good.getProduct()).push(good);
    }

    public Good getGood(int productId) {
        return goodList.get(getProduct(productId)).peek();
    }

    public int size() {
        return goodList.size();
    }

    public int elementSize(Product product) {
        return goodList.get(product).size();
    }

    public Product getProduct(int productId) {
        return productList.getProductList().get(productId);
    }

    public void delete(Product product) {
        System.out.println("pop");
        goodList.get(product).pop();
    }
}
