package com.company.core.services.impl;

import com.company.core.models.user.customer.Customer;
import com.company.core.models.user.customer.ShoppingCart;
import com.company.core.services.persistenceservices.CustomerListPersistenceService;

public class CustomerListServiceImpl {
    //WILL BE USED IN FUTURE

    private final CustomerListPersistenceService clps;
    private static CustomerListServiceImpl instance;

    private CustomerListServiceImpl(CustomerListPersistenceService clps) {
        this.clps = clps;
    }

    public Customer createCustomer(String name, String password) {
        return new Customer(name, password, new ShoppingCart());
    }

    public Customer addCustomer(Customer customer) {
        return clps.save(customer);
    }

    public static CustomerListServiceImpl getInstance(CustomerListPersistenceService clps) {
        if (instance == null) {
            instance = new CustomerListServiceImpl(clps);
        }
        return instance;
    }
}
