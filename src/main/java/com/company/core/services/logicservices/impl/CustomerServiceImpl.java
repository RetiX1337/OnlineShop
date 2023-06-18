package com.company.core.services.logicservices.impl;

import com.company.core.models.EntityNotFoundException;
import com.company.core.models.user.customer.Customer;
import com.company.core.models.user.customer.Cart;
import com.company.core.services.logicservices.CustomerService;
import com.company.core.services.persistenceservices.PersistenceInterface;

public class CustomerServiceImpl implements CustomerService {
    private final PersistenceInterface<Customer> customerPersistenceService;

    public CustomerServiceImpl(PersistenceInterface<Customer> customerPersistenceService) {
        this.customerPersistenceService = customerPersistenceService;
    }

    @Override
    public Customer createCustomer(String name, String password) {
        return new Customer(name, password, new Cart());
    }

    @Override
    public Customer addCustomer(Customer customer) {
        return customerPersistenceService.save(customer);
    }

    @Override
    public Customer updateCustomer(Customer customer, Long id) throws EntityNotFoundException {
        if (customerPersistenceService.isPresent(id)) {
            return customerPersistenceService.update(customer, id);
        } else {
            throw new EntityNotFoundException();
        }
    }

    @Override
    public void deleteById(Long id) throws EntityNotFoundException {
        if (customerPersistenceService.isPresent(id)) {
            customerPersistenceService.deleteById(id);
        } else {
            throw new EntityNotFoundException();
        }
    }

    @Override
    public Customer findCustomer(Long id) throws EntityNotFoundException {
        if (customerPersistenceService.isPresent(id)) {
            return customerPersistenceService.findById(id);
        } else {
            throw new EntityNotFoundException();
        }
    }
}
