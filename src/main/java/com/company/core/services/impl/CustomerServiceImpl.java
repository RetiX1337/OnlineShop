package com.company.core.services.impl;

import com.company.core.models.user.customer.Customer;
import com.company.core.services.CustomerService;
import com.company.core.services.ShoppingCartService;

public class CustomerServiceImpl implements CustomerService {
    private final ShoppingCartService shoppingCartService;
    private static CustomerServiceImpl instance;

    private CustomerServiceImpl(ShoppingCartService shoppingCartService) {
        this.shoppingCartService = shoppingCartService;
    }

    @Override
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
