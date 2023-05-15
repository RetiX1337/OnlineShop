package com.company.core.lists;

import com.company.core.models.goods.Order;

import java.util.ArrayList;
import java.util.HashMap;

public class OrderList {
    private final HashMap<Long, Order> orderList = new HashMap<>();

    public HashMap<Long, Order> getOrderList() {
        return orderList;
    }

    @Override
    public String toString() {
        return orderList.toString();
    }
}
