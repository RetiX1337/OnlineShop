package com.company.core.services.persistenceservices.mapimpl;

import com.company.core.models.user.customer.Customer;
import com.company.core.services.persistenceservices.PersistenceInterface;

import java.util.HashMap;
import java.util.List;


public class CustomerPersistenceService implements PersistenceInterface<Customer> {
    private final HashMap<Long, Customer> customers;
    private static Long idCounter = 0L;

    public CustomerPersistenceService() {
        customers = new HashMap<>();
    }

    @Override
    public Customer save(Customer entity) {
        customers.put(idCounter, entity);
        entity.setId(idCounter);
        idCounter++;
        return entity;
    }

    @Override
    public Customer findById(Long id) {
        return customers.get(id);
    }

    @Override
    public List<Customer> findAll() {
        return customers.values().stream().toList();
    }

    @Override
    public Customer update(Customer entity, Long id){
        customers.put(id, entity);
        return entity;
    }

    @Override
    public void deleteById(Long id) {
        customers.remove(id);
    }


    @Override
    public boolean isPresent(Long id) {
        return customers.containsKey(id);
    }
}
