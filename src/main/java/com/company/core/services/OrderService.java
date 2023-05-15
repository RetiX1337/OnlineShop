package com.company.core.services;

import com.company.core.models.goods.Item;
import com.company.core.models.goods.Order;
import com.company.core.models.user.customer.Customer;

import java.util.Collection;
import java.util.List;

public interface OrderService {
    Order createOrder(Collection<Item> items, Customer customer);
    Order addOrder(Order order);
    void updateOrder(Order order);
    void deleteOrder(Long id);
    List<Order> findByCustomer(Customer customer);
}
