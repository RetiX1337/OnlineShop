package com.company.core.services.persistenceservices;

import com.company.core.lists.GoodList;
import com.company.core.lists.OrderList;
import com.company.core.models.EntityNotFoundException;
import com.company.core.models.goods.Identifiable;
import com.company.core.models.goods.Order;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class OrderListPersistenceService implements PersistenceInterface<Order> {
    private static OrderListPersistenceService instance;
    private final OrderList orderList;
    private static Long idCounter = 0L;

    private OrderListPersistenceService(OrderList orderList) {
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
        return null;
    }

    @Override
    public void deleteById(Long id) {
        getList().remove(id);
    }

    @Override
    public void delete(Order entity) {
        getList().remove(entity.getId(), entity);
    }

    public static OrderListPersistenceService getInstance(OrderList orderList) {
        if (instance == null) {
            instance = new OrderListPersistenceService(orderList);
        }
        return instance;
    }
}
