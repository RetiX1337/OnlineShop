package com.company.core.services.impl;

import com.company.core.models.user.customer.Customer;
import com.company.core.models.user.customer.Cart;
import com.company.core.services.CustomerService;
import com.company.core.services.CartService;
import com.company.core.services.persistenceservices.CustomerPersistenceService;

public class CustomerServiceImpl implements CustomerService {
    private final CartService cartService;
    private final CustomerPersistenceService cps;

    public CustomerServiceImpl(CartService cartService, CustomerPersistenceService cps) {
        this.cartService = cartService;
        this.cps = cps;
    }

    public Customer createCustomer(String name, String password) {
        return new Customer(name, password, new Cart());
    }

    public Customer addCustomer(Customer customer) {
        return cps.save(customer);
    }

    @Override
    public boolean addToCart(Customer customer, Long productId, Integer quantity) {
        return cartService.addToCart(customer.getShoppingCart(), productId, quantity);
    }

    @Override
    public boolean deleteFromCart(Customer customer, Long productId, Integer quantity) {
        return cartService.deleteFromCart(customer.getShoppingCart(), productId, quantity);
    }
}
