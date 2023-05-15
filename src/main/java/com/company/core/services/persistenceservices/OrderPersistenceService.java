package com.company.core.services.persistenceservices;

import com.company.core.lists.OrderList;
import com.company.core.models.EntityNotFoundException;
import com.company.core.models.goods.Order;

import java.util.HashMap;
import java.util.List;

public class OrderPersistenceService implements PersistenceInterface<Order> {
    private final OrderList orderList;
    private static Long idCounter = 0L;

    public OrderPersistenceService(OrderList orderList) {
        this.orderList = orderList;
    }

    private HashMap<Long, Order> getList() {
        return orderList.getOrderList();
    }

    @Override
    public Order save(Order entity) {
        getList().put(idCounter, entity);
        entity.setId(idCounter);
        idCounter++;
        return entity;
    }

    @Override
    public Order findById(Long id) {
        return getList().get(id);
    }

    @Override
    public List<Order> findAll() {
        return getList().values().stream().toList();
    }

    @Override
    public Order update(Order entity) throws EntityNotFoundException {
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
    public void delete(Order entity) {
        getList().remove(entity.getId(), entity);
    }
}
