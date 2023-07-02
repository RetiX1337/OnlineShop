package com.company.core.models.user.customer;

import com.company.core.models.goods.Item;
import com.company.core.models.goods.Product;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

public class Cart {
    private final HashMap<Product, Item> cart = new HashMap<>();
    private BigDecimal summaryPrice = BigDecimal.valueOf(0);

    public Collection<Item> getProductsFromCart() {
        return cart.values();
    }

    public void addItem(Item item) {
        cart.put(item.getProduct(), item);
    }

    public void deleteItem(Item item) {
        cart.remove(item.getProduct(), item);
    }

    public Item getItem(Product product) {
        return cart.get(product);
    }

    public void setSummaryPrice(BigDecimal summaryPrice) {
        this.summaryPrice = summaryPrice;
    }

    public BigDecimal getSummaryPrice() {
        return summaryPrice;
    }

    public void clear() {
        cart.clear();
        summaryPrice = new BigDecimal(0);
    }

    public boolean isEmpty() {
        return cart.isEmpty();
    }

    @Override
    public String toString() {
        String result = "";
        ArrayList<Item> items = new ArrayList<>(cart.values());
        for (Item item : items) {
            result = result.concat("> " + item + ", Product ID: " + item.getProduct().getId() + "\n");
        }
        return result.concat("Summary price: " + summaryPrice);
    }
}
