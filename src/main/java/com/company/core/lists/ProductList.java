package com.company.core.lists;

import com.company.core.models.goods.Product;
import com.company.core.models.goods.Type;

import java.math.BigDecimal;
import java.util.HashMap;

public class ProductList {
    private final HashMap<Integer, Product> productList = new HashMap<>();
    //test commit
    public ProductList() {
        fillProductList();
    }
    private void fillProductList() {
        productList.put(0, new Product("ovs1", "Super Platki", "Ovsianka", Type.FOOD, new BigDecimal("4.99")));
        productList.put(1, new Product("ovs2", "Platki Gorskie", "Ovsianka Dziwna", Type.FOOD, new BigDecimal("5.49")));
        productList.put(2, new Product("pivo1", "Desperados", "Desperados Mochito", Type.ALCOHOL, new BigDecimal("6.49")));
        productList.put(3, new Product("toipap1", "Miekka dupa", "Rumianek", Type.HOUSEHOLD, new BigDecimal("10.99")));
        productList.put(4, new Product("cola1", "Coca-Cola", "Coca-Cola", Type.BEVERAGE, new BigDecimal("4.49")));
    }

    public void printProductList() {
        for (int i = 0; i < productList.size(); i++) {
            System.out.println(i + ". " + productList.get(i));
        }
    }

    public Product getProduct(int id) {
        return productList.get(id);
    }

    public int size() {
        return productList.size();
    }

    public HashMap<Integer, Product> getProductList() {
        return productList;
    }
}
