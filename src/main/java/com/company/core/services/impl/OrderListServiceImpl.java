package com.company.core.services.impl;

import com.company.core.models.EntityNotFoundException;
import com.company.core.models.goods.Item;
import com.company.core.models.goods.Order;
import com.company.core.models.goods.Product;
import com.company.core.models.user.customer.Customer;
import com.company.core.services.persistenceservices.OrderListPersistenceService;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;

public class OrderListServiceImpl {

    private OrderListPersistenceService olps;
    private static OrderListServiceImpl instance;

    private OrderListServiceImpl(OrderListPersistenceService olps) {
        this.olps = olps;
    }

    public Order createOrder(HashMap<Product, Item> items, Customer customer) {
        return new Order(items, customer);
    }

    public void addOrder(Order order) {
        olps.save(order);
    }

    public void updateOrder(Order order) {
        try {
            olps.update(order);
        } catch (EntityNotFoundException e) {
            System.out.println("This order doesn't exist, can't update");
        }
    }

    public void deleteOrder(Long id) {
        olps.deleteById(id);
    }

    public BigDecimal getPrice(Long id) {
        return olps.findById(id).getSummaryPrice();
    }

    public String getOrderString(Long id) {
        return "\nOrder: " +
                olps.findById(id).getItems() +
                "\nTotal price: " +
                getPrice(id) +
                "\n";
    }

    public static OrderListServiceImpl getInstance(OrderListPersistenceService olps) {
        if (instance == null) {
            instance = new OrderListServiceImpl(olps);
        }
        return instance;
    }
}
