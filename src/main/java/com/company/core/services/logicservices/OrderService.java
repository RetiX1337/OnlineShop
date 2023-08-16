package com.company.core.services.logicservices;

import com.company.core.models.goods.Item;
import com.company.core.models.goods.Order;
import com.company.core.models.user.User;

import java.util.List;
import java.util.Set;

public interface OrderService {
    Order createOrder(Set<Item> items, User user);
    Order addOrder(Order order);
    boolean processOrder(Order order, User user, Long shopId);
    Order updateOrder(Order order, Long id);
    void deleteOrder(Long id);
    Order findOrder(Long id);
    List<Order> findByUser(Long userId);
}
