package com.company.core.models.user.customer;

import com.company.core.models.goods.Item;
import com.company.core.models.goods.Product;

import java.math.BigDecimal;
import java.util.*;

public class Cart {
    private final Set<Item> items = new HashSet<>();
    private BigDecimal summaryPrice = BigDecimal.ZERO;

    public Set<Item> getCartItems() {
        return items;
    }

    public void addItem(Item item) {
        items.add(item);
    }

    public void deleteItem(Item item) {
        items.remove(item);
    }

    public Item getItem(Product product) {
        return items.stream()
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
        items.clear();
        summaryPrice = new BigDecimal(0);
    }

    public boolean isEmpty() {
        return items.isEmpty();
    }

    @Override
    public String toString() {
        String result = "";
        ArrayList<Item> items = new ArrayList<>(this.items);
        for (Item item : items) {
            result = result.concat("> " + item + ", Product ID: " + item.getProduct().getId() + "\n");
        }
        return result.concat("Summary price: " + summaryPrice);
    }
}
