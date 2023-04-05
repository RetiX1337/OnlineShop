package com.company.core.order;

import com.company.core.good.Good;
import com.company.core.product.Product;
import com.company.core.user.customer.Customer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;

public class OrderList {
    private final ArrayList<Order> orderList = new ArrayList<>();

    public void addOrder(HashMap<Product, Stack<Good>> goods, Customer customer) {
        orderList.add(createOrder(goods, customer));
    }

    public void addOrder(HashMap<Product, Stack<Good>> goods) {
        orderList.add(createOrder(goods));
    }

    public Order createOrder(HashMap<Product, Stack<Good>> goods, Customer customer) {
        return new Order(goods, customer);
    }

    public Order createOrder(HashMap<Product, Stack<Good>> goods) {
        return new Order(goods);
    }

    public ArrayList<Order> getOrderList() {
        return orderList;
    }
}
