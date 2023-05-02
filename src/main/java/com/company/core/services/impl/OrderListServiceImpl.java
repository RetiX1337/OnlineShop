package com.company.core.services.impl;

import com.company.core.models.EntityNotFoundException;
import com.company.core.models.goods.Item;
import com.company.core.models.goods.Order;
import com.company.core.models.user.customer.Customer;
import com.company.core.services.OrderListService;
import com.company.core.services.persistenceservices.OrderListPersistenceService;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class OrderListServiceImpl implements OrderListService {

    private OrderListPersistenceService olps;
    private static OrderListServiceImpl instance;

    private OrderListServiceImpl(OrderListPersistenceService olps) {
        this.olps = olps;
    }

    @Override
    public Order createOrder(Collection<Item> items, Customer customer) {
        return new Order(items, customer);
    }

    @Override
    public void addOrder(Order order) {
        olps.save(order);
    }

    @Override
    public void updateOrder(Order order) {
        try {
            olps.update(order);
        } catch (EntityNotFoundException e) {
            System.out.println("This order doesn't exist, can't update");
        }
    }

    @Override
    public void deleteOrder(Long id) {
        olps.deleteById(id);
    }

    @Override
    public List<Order> findByCustomer(Customer customer) {
        return olps.findAll().stream().filter(order -> order.getCustomer().getId().equals(customer.getId())).toList();
    }

    public static OrderListServiceImpl getInstance(OrderListPersistenceService olps) {
        if (instance == null) {
            instance = new OrderListServiceImpl(olps);
        }
        return instance;
    }
}
