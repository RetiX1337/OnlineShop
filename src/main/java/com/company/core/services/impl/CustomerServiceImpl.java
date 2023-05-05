package com.company.core.services.impl;

import com.company.core.models.user.customer.Customer;
import com.company.core.models.user.customer.ShoppingCart;
import com.company.core.services.CustomerService;
import com.company.core.services.ShoppingCartService;
import com.company.core.services.persistenceservices.CustomerPersistenceService;

public class CustomerServiceImpl implements CustomerService {
    private final ShoppingCartService shoppingCartService;
    private final CustomerPersistenceService cps;

    public CustomerServiceImpl(ShoppingCartService shoppingCartService, CustomerPersistenceService cps) {
        this.shoppingCartService = shoppingCartService;
        this.cps = cps;
    }

    public Customer createCustomer(String name, String password) {
        return new Customer(name, password, new ShoppingCart());
    }

    public Customer addCustomer(Customer customer) {
        return cps.save(customer);
    }

    @Override
    public boolean addToCart(Customer customer, Long productId, Integer quantity) {
        return shoppingCartService.addToCart(customer.getShoppingCart(), productId, quantity);
    }

}
