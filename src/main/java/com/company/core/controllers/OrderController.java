package com.company.core.controllers;

import com.company.core.models.goods.Good;
import com.company.core.models.goods.Order;
import com.company.core.models.goods.Product;
import com.company.core.models.user.customer.Customer;
import com.company.core.services.OrderService;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Stack;

public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    public Order getOrder(int id) {
        return orderService.getOrder(id);
    }

    public void printOrder(int id) {
        System.out.println(orderService.getOrderString(id));
    }

    public void createOrder(HashMap<Product, Stack<Good>> goods, Customer customer) {
        orderService.createOrder(goods, customer);
    }

    public void createOrder(HashMap<Product, Stack<Good>> goods) {
        orderService.createOrder(goods);
    }

    public BigDecimal getPrice(int id) {
        return orderService.getPrice(id);
    }
}
