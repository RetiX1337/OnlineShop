package com.company.core.models.user.customer;

import com.company.core.models.goods.Item;
import com.company.core.models.goods.Product;

import java.math.BigDecimal;
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

    public Item getItem(Long productId) {
        return shoppingCart.values().stream().filter(item -> item.getProduct().getId().equals(productId)).findFirst().get();
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
}
