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
    private OrderPersistenceService orderPersistenceService;

    public OrderServiceImpl(OrderPersistenceService orderPersistenceService) {
        this.orderPersistenceService = orderPersistenceService;
    }

    @Override
    public Order createOrder(Collection<Item> items, Customer customer) {
        return new Order(items, customer);
    }

    @Override
    public Order addOrder(Order order) {
        return orderPersistenceService.save(order);
    }

    @Override
    public Order updateOrder(Order order, Long id) throws EntityNotFoundException {
        if (orderPersistenceService.isPresent(id)) {
            return orderPersistenceService.update(order, id);
        } else {
            throw new EntityNotFoundException();
        }
    }

    @Override
    public void deleteOrder(Long id) throws EntityNotFoundException {
        if (orderPersistenceService.isPresent(id)) {
            orderPersistenceService.deleteById(id);
        } else {
            throw new EntityNotFoundException();
        }
    }

    @Override
    public Order findOrder(Long id) throws EntityNotFoundException {
        if (orderPersistenceService.isPresent(id)) {
            return orderPersistenceService.findById(id);
        } else {
            throw new EntityNotFoundException();
        }
    }

    @Override
    public List<Order> findByCustomer(Customer customer) {
        return orderPersistenceService.findAll().stream().filter(order -> order.getCustomer().getId().equals(customer.getId())).toList();
    }
}
