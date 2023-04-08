package com.company.core.services;

import com.company.core.lists.OrderList;
import com.company.core.models.goods.Good;
import com.company.core.models.goods.Order;
import com.company.core.models.goods.Product;
import com.company.core.models.user.customer.Customer;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Stack;

public class OrderService {

    private OrderList orderList;
    private static OrderService instance;

    public OrderService(OrderList orderList) {
        this.orderList = orderList;
    }

    public Order getOrder(int id) {
        return orderList.getOrder(id);
    }

    public void createOrder(HashMap<Product, Stack<Good>> goods, Customer customer) {
        orderList.save(new Order(goods, customer));
    }

    public BigDecimal getPrice(int id) {
        BigDecimal summaryPrice = new BigDecimal(0);
        for (Order.OrderElement oe : orderList.getOrder(id).getOrderElements()) {
            summaryPrice = summaryPrice.add(oe.getPrice());
        }
        return summaryPrice;
    }

    public String getOrderString(int id) {
        return "\nOrder: " +
                orderList.getOrder(id).getOrderElements() +
                "\nTotal price: " +
                getPrice(id) +
                "\n";
    }

    public void createOrder(HashMap<Product, Stack<Good>> goods) {
        orderList.save(new Order(goods));
    }

    public static OrderService getInstance(OrderList orderList) {
        if (instance == null) {
            instance = new OrderService(orderList);
        }
        return instance;
    }
}
