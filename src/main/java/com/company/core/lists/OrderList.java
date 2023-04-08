package com.company.core.lists;

import com.company.core.models.goods.Order;

import java.util.ArrayList;

public class OrderList {
    private final ArrayList<Order> orderList = new ArrayList<>();

    public void save(Order order) {
        orderList.add(order);
    }

    public Order getOrder(int id) {
        return orderList.get(id);
    }

    @Override
    public String toString() {
        return orderList.toString();
    }
}
