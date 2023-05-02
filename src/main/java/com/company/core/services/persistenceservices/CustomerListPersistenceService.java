package com.company.core.services.persistenceservices;


import com.company.core.lists.CustomerList;
import com.company.core.lists.OrderList;
import com.company.core.models.EntityNotFoundException;
import com.company.core.models.user.customer.Customer;

import java.util.HashMap;
import java.util.List;

public class CustomerListPersistenceService implements PersistenceInterface<Customer> {
    private static CustomerListPersistenceService instance;
    private final CustomerList customerList;
    private static Long idCounter = 0L;

    private CustomerListPersistenceService(CustomerList customerList) {
        this.customerList = customerList;
    }

    private HashMap<Long, Customer> getList() {
        return customerList.getCustomerList();
    }

    @Override
    public Customer save(Customer entity) {
        getList().put(idCounter, entity);
        entity.setId(idCounter);
        idCounter++;
        return entity;
    }

    @Override
    public Customer findById(Long id) {
        return getList().get(id);
    }

    @Override
    public List<Customer> findAll() {
        return getList().values().stream().toList();
    }

    @Override
    public Customer update(Customer entity) throws EntityNotFoundException {
        if (getList().containsKey(entity.getId())) {
            getList().put(entity.getId(), entity);
        } else {
            throw new EntityNotFoundException();
        }
        return entity;
    }

    @Override
    public void deleteById(Long id) {
        getList().remove(id);
    }

    @Override
    public void delete(Customer entity) {
        getList().remove(entity.getId(), entity);
    }

    public static CustomerListPersistenceService getInstance(CustomerList customerList) {
        if (instance == null) {
            instance = new CustomerListPersistenceService(customerList);
        }
        return instance;
    }
}
