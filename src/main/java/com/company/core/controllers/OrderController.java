package com.company.core.controllers;

import com.company.core.models.goods.Good;
import com.company.core.models.goods.Product;
import com.company.core.models.user.customer.Customer;
import com.company.core.services.impl.OrderListServiceImpl;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Stack;

public class OrderController {

    private final OrderListServiceImpl orderListServiceImpl;

    public OrderController(OrderListServiceImpl orderListServiceImpl) {
        this.orderListServiceImpl = orderListServiceImpl;
    }

    public void printOrder(Long id) {
        System.out.println(orderListServiceImpl.getOrderString(id));
    }

    public void createOrder(HashMap<Product, Stack<Good>> goods, Customer customer) {
        orderListServiceImpl.createOrder(goods, customer);
    }

    public void createOrder(HashMap<Product, Stack<Good>> goods) {
        orderListServiceImpl.createOrder(goods);
    }

    public BigDecimal getPrice(Long id) {
        return orderListServiceImpl.getPrice(id);
    }
}
