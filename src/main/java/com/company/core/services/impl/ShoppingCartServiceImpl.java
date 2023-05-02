package com.company.core.services.impl;

import com.company.core.models.goods.Item;
import com.company.core.models.goods.Product;
import com.company.core.models.user.customer.ShoppingCart;

public class ShoppingCartServiceImpl {
    private static ShoppingCartServiceImpl instance;
    private final ItemServiceImpl itemService;
    private final ProductListServiceImpl productListService;

    private ShoppingCartServiceImpl(ItemServiceImpl itemService, ProductListServiceImpl productListService) {
        this.itemService = itemService;
        this.productListService = productListService;
    }

    public boolean addToCart(ShoppingCart shoppingCart, Long productId, Integer quantity) {
        if (containsProduct(shoppingCart, productListService.getProduct(productId))) {
            if (notMoreThanAvailable(shoppingCart, itemService.createItem(productId, quantity))) {
                addOrUpdate(shoppingCart, itemService.createItem(productId, quantity));
                return true;
            } else {
                return false;
            }
        } else {
            addOrUpdate(shoppingCart, itemService.createItem(productId, quantity));
            return true;
        }
    }

    private void addToCartItem(Item item) {
        itemService.addToItem(item);
    }

    private void countPrice(ShoppingCart shoppingCart) {
        for (Item it : shoppingCart.getProductsFromCart()) {
            shoppingCart.setSummaryPrice(shoppingCart.getSummaryPrice().add(it.getPrice()));
        }
        System.out.println(shoppingCart.getSummaryPrice());
    }

    private boolean notMoreThanAvailable(ShoppingCart shoppingCart, Item item) {
        if (item == null) return false;
        Product product = item.getProduct();
        return item.getQuantity() - (shoppingCart.getItem(product.getId()).getQuantity() + item.getProduct().getQuantity()) >= 0;
    }

    private boolean containsProduct(ShoppingCart shoppingCart, Product product) {
        return shoppingCart.getProductsFromCart().stream().anyMatch(item -> item.getProduct().getId().equals(product.getId()));
    }

    private void addOrUpdate(ShoppingCart shoppingCart, Item item) {
        if (containsProduct(shoppingCart, item.getProduct())) {
            addToCartItem(item);
        } else {
            shoppingCart.addItem(item);
        }
        countPrice(shoppingCart);
    }

    public static ShoppingCartServiceImpl getInstance(ItemServiceImpl itemService, ProductListServiceImpl productListService) {
        if (instance == null) {
            instance = new ShoppingCartServiceImpl(itemService, productListService);
        }
        return instance;
    }
}
