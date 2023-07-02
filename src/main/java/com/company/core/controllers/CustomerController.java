package com.company.core.controllers;

import com.company.core.models.EntityNotFoundException;
import com.company.core.models.user.customer.Customer;
import com.company.core.services.logicservices.CustomerService;

public class CustomerController {
    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    public Customer findCustomer(Long customerId) {
        try {
            return customerService.findCustomer(customerId);
        } catch (EntityNotFoundException e) {
            System.out.println("This customer doesn't exist");
        }
        return null;
    }

    public boolean isPresent(Long customerId) {
        return customerService.isPresent(customerId);
    }
}
