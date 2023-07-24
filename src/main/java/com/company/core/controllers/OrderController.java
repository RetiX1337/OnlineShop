package com.company.core.controllers;

import com.company.core.models.goods.Order;
import com.company.core.models.user.customer.Customer;
import com.company.core.services.logicservices.OrderService;
import org.springframework.stereotype.Controller;

import java.util.Collection;
@Controller
public class OrderController {
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    public Collection<Order> getOrders(Customer customer) {
        return orderService.findByCustomer(customer.getId());
    }

    public Order findOrderById(Long orderId) {
        return orderService.findOrder(orderId);
    }
}
