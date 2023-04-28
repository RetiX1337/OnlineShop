package com.company.core.controllers;

import com.company.core.services.impl.OrderListServiceImpl;

import java.math.BigDecimal;

public class OrderController {

    private final OrderListServiceImpl orderListServiceImpl;

    public OrderController(OrderListServiceImpl orderListServiceImpl) {
        this.orderListServiceImpl = orderListServiceImpl;
    }

    public void printOrder(Long id) {
        System.out.println(orderListServiceImpl.getOrderString(id));
    }

    public BigDecimal getPrice(Long id) {
        return orderListServiceImpl.getPrice(id);
    }
}
