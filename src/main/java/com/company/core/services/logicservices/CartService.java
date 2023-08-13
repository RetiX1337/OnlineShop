package com.company.core.services.logicservices;

import com.company.core.models.user.customer.Cart;
import com.company.core.models.user.customer.Customer;

public interface CartService {
    boolean checkoutCart(Customer customer, Long shopId);
    boolean addToCart(Cart cart, Long productId, Integer quantity, Long shopId);
    boolean deleteFromCart(Cart cart, Long productId, Integer quantity);
}
