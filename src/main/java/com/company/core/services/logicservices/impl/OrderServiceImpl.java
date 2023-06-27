package com.company.core.services.logicservices.impl;

import com.company.core.models.EntityNotFoundException;
import com.company.core.models.goods.Item;
import com.company.core.models.goods.Order;
import com.company.core.models.goods.OrderStatus;
import com.company.core.models.user.customer.Customer;
import com.company.core.services.logicservices.ItemService;
import com.company.core.services.logicservices.OrderService;
import com.company.core.services.persistenceservices.PersistenceInterface;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;

public class OrderServiceImpl implements OrderService {
    private PersistenceInterface<Order> orderPersistenceService;
    private ItemService itemService;

    public OrderServiceImpl(PersistenceInterface<Order> orderPersistenceService,
                            ItemService itemService) {
        this.orderPersistenceService = orderPersistenceService;
        this.itemService = itemService;
    }

    @Override
    public Order createOrder(Collection<Item> items, Customer customer) {
        Order order = new Order(items, customer);
        order.setOrderStatus(OrderStatus.NEW);
        return order;
    }

    @Override
    public boolean processOrder(Order order, Customer customer) {
        if (processPayment(customer, order)) {
            order.setOrderStatus(OrderStatus.PAID);
            addOrder(order);
            return true;
        }
        return false;
    }

    @Override
    public Order addOrder(Order order) {
        Order savedOrder = orderPersistenceService.save(order);
        savedOrder.setOrderStatus(OrderStatus.PAID);
        savedOrder.getItems().forEach(item -> {
            item.setOrderId(savedOrder.getId());
            itemService.addItem(item);
        });
        return savedOrder;
    }

    @Override
    public Order updateOrder(Order order, Long id) throws EntityNotFoundException {
        if (orderPersistenceService.isPresent(id)) {
            Order updatedOrder = orderPersistenceService.update(order, id);
            updatedOrder.getItems().forEach(item -> {
                item.setOrderId(updatedOrder.getId());
                itemService.addItem(item);
            });
            return orderPersistenceService.update(order, id);
        } else {
            throw new EntityNotFoundException();
        }
    }

    @Override
    public void deleteOrder(Long id) throws EntityNotFoundException {
        if (orderPersistenceService.isPresent(id)) {
            Order orderToDelete = orderPersistenceService.findById(id);
            for (Item item : orderToDelete.getItems()) {
                itemService.deleteItem(item.getId());
            }
            orderPersistenceService.deleteById(id);
        } else {
            throw new EntityNotFoundException();
        }
    }

    @Override
    public Order findOrder(Long id) throws EntityNotFoundException {
        if (orderPersistenceService.isPresent(id)) {
            return orderPersistenceService.findById(id);
        } else {
            throw new EntityNotFoundException();
        }
    }

    @Override
    public List<Order> findByCustomer(Customer customer) {
        return orderPersistenceService.findAll().stream().filter(order -> order.getCustomer().getId().equals(customer.getId())).toList();
    }

    private boolean processPayment(Customer customer, Order order) {
        if (enoughMoney(customer.getWallet(), order.getSummaryPrice())) {
            customer.setWallet(customer.getWallet().subtract(order.getSummaryPrice()));
            return true;
        }
        return false;
    }

    private boolean enoughMoney(BigDecimal wallet, BigDecimal summaryPrice) {
        return summaryPrice.compareTo(wallet) == 0 || summaryPrice.compareTo(wallet) == -1;
    }
}
