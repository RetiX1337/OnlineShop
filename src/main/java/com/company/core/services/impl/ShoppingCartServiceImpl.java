package com.company.core.services.impl;

import com.company.core.models.goods.Item;
import com.company.core.models.goods.Product;
import com.company.core.models.user.customer.ShoppingCart;

public class ShoppingCartServiceImpl {
    private final ShoppingCart shoppingCart;
    private static ShoppingCartServiceImpl instance;
    private final ItemServiceImpl itemService;

    private ShoppingCartServiceImpl(ShoppingCart shoppingCart, ItemServiceImpl itemService) {
        this.shoppingCart = shoppingCart;
        this.itemService = itemService;
    }

    private void addOrUpdate(Item item) {
        if (shoppingCart.containsProduct(item.getProduct().getId())) {
            addToCartItem(item);
        } else {
            shoppingCart.addItem(item);
        }
        countPrice();
    }

    public void addToCart(Long productId, Integer quantity) {
        System.out.println(shoppingCart.containsProduct(productId));
        if (shoppingCart.containsProduct(productId)) {
            if (availableOnStorage(itemService.createItem(productId, quantity))) {
                addOrUpdate(itemService.createItem(productId, quantity));
            } else {
                System.out.println("You've picked more than available on the storage");
            }
        } else {
            addOrUpdate(itemService.createItem(productId, quantity));
        }
    }

    public void addToCartItem(Item item) {
        itemService.addToItem(item);
    }

    private void countPrice() {
        for (Item it : shoppingCart.getShoppingCart().values()) {
            shoppingCart.setSummaryPrice(shoppingCart.getSummaryPrice().add(it.getPrice()));
        }
        System.out.println(shoppingCart.getSummaryPrice());
    }

    private boolean availableOnStorage(Item item) {
        if (item == null) return false;
        Product product = item.getProduct();
        return item.getQuantity() - (shoppingCart.getItem(product.getId()).getQuantity() + item.getProduct().getQuantity()) >= 0;
    }

    public static ShoppingCartServiceImpl getInstance(ShoppingCart shoppingCart, ItemServiceImpl itemService) {
        if (instance == null) {
            instance = new ShoppingCartServiceImpl(shoppingCart, itemService);
        }
        return instance;
    }
}
