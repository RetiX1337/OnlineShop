package com.company.core.services.impl;

import com.company.core.models.user.customer.Customer;

public class CustomerServiceImpl {
    private final ShoppingCartServiceImpl shoppingCartService;
    private static CustomerServiceImpl instance;

    private CustomerServiceImpl(ShoppingCartServiceImpl shoppingCartService) {
        this.shoppingCartService = shoppingCartService;
    }

    public boolean addToCart(Customer customer, Long productId, Integer quantity) {
        return shoppingCartService.addToCart(customer.getShoppingCart(), productId, quantity);
    }

    public static CustomerServiceImpl getInstance(ShoppingCartServiceImpl shoppingCartService) {
        if (instance == null) {
            instance = new CustomerServiceImpl(shoppingCartService);
        }
        return instance;
    }
}
