package com.company.core.services.persistenceservices;


import com.company.core.lists.CustomerList;
import com.company.core.models.EntityNotFoundException;
import com.company.core.models.user.customer.Customer;
import org.w3c.dom.Element;

import java.util.HashMap;
import java.util.List;


public class CustomerPersistenceService implements PersistenceInterface<Customer> {
    private final CustomerList customerList;
    private static Long idCounter = 0L;

    public CustomerPersistenceService(CustomerList customerList) {
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

}
