package com.company.core.services.impl;

import com.company.core.models.EntityNotFoundException;
import com.company.core.models.goods.Item;
import com.company.core.models.goods.Order;
import com.company.core.models.user.customer.Customer;
import com.company.core.services.OrderService;
import com.company.core.services.persistenceservices.OrderPersistenceService;

import java.util.Collection;
import java.util.List;

public class OrderServiceImpl implements OrderService {
    private OrderPersistenceService ops;

    public OrderServiceImpl(OrderPersistenceService ops) {
        this.ops = ops;
    }

    @Override
    public Order createOrder(Collection<Item> items, Customer customer) {
        return new Order(items, customer);
    }

    @Override
    public Order addOrder(Order order) {
        return ops.save(order);
    }

    @Override
    public void updateOrder(Order order) {
        try {
            ops.update(order);
        } catch (EntityNotFoundException e) {
            System.out.println("This order doesn't exist, can't update");
        }
    }

    @Override
    public void deleteOrder(Long id) {
        ops.deleteById(id);
    }

    @Override
    public List<Order> findByCustomer(Customer customer) {
        return ops.findAll().stream().filter(order -> order.getCustomer().getId().equals(customer.getId())).toList();
    }
}
