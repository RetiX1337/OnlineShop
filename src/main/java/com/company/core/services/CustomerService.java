package com.company.core.services;

import com.company.core.models.user.customer.Customer;

import java.math.BigDecimal;

public interface CustomerService {
    boolean addToCart(Customer customer, Long productId, Integer quantity);
}
