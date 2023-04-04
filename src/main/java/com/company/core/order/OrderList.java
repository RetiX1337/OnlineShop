package com.company.core.order;

import com.company.core.good.Good;

import java.util.ArrayList;
import java.util.Stack;

public class OrderList {
    private final ArrayList<Order> orderList = new ArrayList<>();

    public void addOrder(Order order) {
        orderList.add(order);
    }

    public void createOrder(Stack<Good> goods) {
        orderList.add(new Order())
    }

    public ArrayList<Order> getOrderList() {
        return orderList;
    }
}
