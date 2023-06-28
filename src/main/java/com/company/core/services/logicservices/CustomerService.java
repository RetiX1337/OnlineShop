package com.company.core.services.logicservices;

import com.company.core.models.EntityNotFoundException;
import com.company.core.models.user.customer.Customer;

public interface CustomerService {
    Customer createCustomer(String name, String password, String email);
    Customer addCustomer(Customer customer);
    Customer updateCustomer(Customer customer, Long id) throws EntityNotFoundException;
    void deleteById(Long id) throws EntityNotFoundException;
    Customer findCustomer(Long id) throws EntityNotFoundException;
}