package com.company.core.models.user.customer;

import com.company.core.models.goods.Item;
import com.company.core.models.goods.Product;

import java.math.BigDecimal;
import java.util.*;

public class Cart {
    private final Set<Item> cart = new HashSet<>();
    private BigDecimal summaryPrice = BigDecimal.valueOf(0);

    public Set<Item> getProductsFromCart() {
        return cart;
    }

    public void addItem(Item item) {
        cart.add(item);
    }

    public void deleteItem(Item item) {
        cart.remove(item);
    }

    public Item getItem(Product product) {
        return cart.stream()
                .filter(item -> product.getId().equals(item.getProduct().getId()))
                .findFirst()
                .orElse(null);
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
        ArrayList<Item> items = new ArrayList<>(cart);
        for (Item item : items) {
            result = result.concat("> " + item + ", Product ID: " + item.getProduct().getId() + "\n");
        }
        return result.concat("Summary price: " + summaryPrice);
    }
}
