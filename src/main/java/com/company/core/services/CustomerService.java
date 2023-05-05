package com.company.core.services;

import com.company.core.models.user.customer.Customer;
import com.company.core.models.user.customer.ShoppingCart;

public interface CustomerService {
    boolean addToCart(Customer customer, Long productId, Integer quantity);
}
