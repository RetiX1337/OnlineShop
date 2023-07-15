package com.company.core.services.logicservices.impl;

import com.company.core.models.user.customer.Customer;
import com.company.core.services.logicservices.CustomerService;
import com.company.core.services.persistenceservices.PersistenceInterface;

public class CustomerServiceImpl implements CustomerService {
    private final PersistenceInterface<Customer> customerPersistenceService;

    public CustomerServiceImpl(PersistenceInterface<Customer> customerPersistenceService) {
        this.customerPersistenceService = customerPersistenceService;
    }

    @Override
    public Customer createCustomer(String name, String password, String email) {
        return new Customer(name, password, email);
    }

    @Override
    public Customer addCustomer(Customer customer) {
        return customerPersistenceService.save(customer);
    }

    @Override
    public Customer updateCustomer(Customer customer, Long id) {
        customer.setId(id);
        return customerPersistenceService.update(customer);
    }

    @Override
    public void deleteById(Long id) {
        customerPersistenceService.deleteById(id);
    }

    @Override
    public Customer findCustomer(Long id) {
        return customerPersistenceService.findById(id);
    }

    @Override
    public boolean isPresent(Long customerId) {
        return customerPersistenceService.isPresent(customerId);
    }
}
