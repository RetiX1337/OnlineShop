package com.company.core.models.user.customer;

import com.company.core.models.goods.Item;
import com.company.core.models.goods.Product;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

public class ShoppingCart {
    private final HashMap<Product, Item> shoppingCart = new HashMap<>();
    private BigDecimal summaryPrice = new BigDecimal(0);

    public Collection<Item> getProductsFromCart() {
        return shoppingCart.values();
    }

    public void addItem(Item item) {
        shoppingCart.put(item.getProduct(), item);
    }

    public Item getItem(Product product) {
        return shoppingCart.get(product);
    }

    public void setSummaryPrice(BigDecimal summaryPrice) {
        this.summaryPrice = summaryPrice;
    }

    public BigDecimal getSummaryPrice() {
        return summaryPrice;
    }

    public void clear() {
        shoppingCart.clear();
        summaryPrice = new BigDecimal(0);
    }

    public boolean isEmpty() {
        return shoppingCart.isEmpty();
    }

    @Override
    public String toString() {
        String result = "";
        ArrayList<Item> items = new ArrayList<>(shoppingCart.values());
        for (int i = 0; i < shoppingCart.values().size(); i++) {
            result = result.concat(i + ". " + items.get(i) + ", Summary Price: " + summaryPrice + "\n");
        }
        return result;
    }
}
