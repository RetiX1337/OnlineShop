package com.company.core.services.persistenceservices;

import com.company.core.models.goods.Order;

import java.util.HashMap;
import java.util.List;

public class OrderPersistenceService implements PersistenceInterface<Order> {
    private final HashMap<Long, Order> orders;
    private static Long idCounter = 0L;

    public OrderPersistenceService() {
        orders = new HashMap<>();
    }

    @Override
    public Order save(Order entity) {
        orders.put(idCounter, entity);
        entity.setId(idCounter);
        idCounter++;
        return entity;
    }

    @Override
    public Order findById(Long id) {
        return orders.get(id);
    }

    @Override
    public List<Order> findAll() {
        return orders.values().stream().toList();
    }

    @Override
    public Order update(Order entity, Long id){
        orders.put(id, entity);
        return entity;
    }

    @Override
    public void deleteById(Long id) {
        orders.remove(id);
    }


    @Override
    public boolean isPresent(Long id){
        return orders.containsKey(id);
    }
}
