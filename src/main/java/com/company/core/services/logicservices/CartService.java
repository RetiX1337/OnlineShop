package com.company.core.services.logicservices;

import com.company.core.models.user.customer.Cart;

public interface CartService {
    boolean checkoutCart(Cart cart, Long shopId);
    boolean addToCart(Cart cart, Long productId, Integer quantity, Long shopId);
    boolean deleteFromCart(Cart cart, Long productId, Integer quantity);
}
