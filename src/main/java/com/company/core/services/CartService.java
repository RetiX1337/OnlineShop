package com.company.core.services;

import com.company.core.models.user.customer.Cart;

public interface CartService {
    boolean addToCart(Cart cart, Long productId, Integer quantity);

    boolean deleteFromCart(Cart cart, Long productId, Integer quantity);
}
