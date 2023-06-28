package com.company.core.services.logicservices;

import com.company.core.models.EntityNotFoundException;
import com.company.core.models.goods.Item;
import com.company.core.models.goods.Order;
import com.company.core.models.user.customer.Customer;

import java.util.Collection;
import java.util.List;

public interface OrderService {
    Order createOrder(Collection<Item> items, Customer customer);
    Order addOrder(Order order);
    boolean processOrder(Order order, Customer customer, Long shopId);
    Order updateOrder(Order order, Long id) throws EntityNotFoundException;
    void deleteOrder(Long id) throws EntityNotFoundException;
    Order findOrder(Long id) throws EntityNotFoundException;
    List<Order> findByCustomer(Long customerId);
}
