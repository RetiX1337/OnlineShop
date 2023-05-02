package com.company.core.services;

import com.company.core.models.user.customer.ShoppingCart;

public interface ShoppingCartService {
    boolean addToCart(ShoppingCart shoppingCart, Long productId, Integer quantity);
}
