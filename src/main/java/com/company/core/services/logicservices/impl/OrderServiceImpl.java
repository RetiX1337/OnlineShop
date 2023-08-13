package com.company.core.services.logicservices.impl;

import com.company.core.models.goods.Item;
import com.company.core.models.goods.Order;
import com.company.core.models.goods.OrderStatus;
import com.company.core.models.user.customer.Customer;
import com.company.core.services.logicservices.ItemService;
import com.company.core.services.logicservices.OrderService;
import com.company.core.services.logicservices.StorageService;
import com.company.core.services.persistenceservices.PersistenceInterface;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;
import java.util.Set;

public class OrderServiceImpl implements OrderService {
    private PersistenceInterface<Order> orderPersistenceService;
    private final StorageService storageService;
    private ItemService itemService;

    public OrderServiceImpl(PersistenceInterface<Order> orderPersistenceService,
                            ItemService itemService,
                            StorageService storageService) {
        this.orderPersistenceService = orderPersistenceService;
        this.itemService = itemService;
        this.storageService = storageService;
    }

    @Override
    public Order createOrder(Set<Item> items, Customer customer) {
        Order order = new Order(items, customer);
        order.setOrderStatus(OrderStatus.NEW);
        return order;
    }

    @Override
    public boolean processOrder(Order order, Customer customer, Long shopId) {
        if (processPayment(customer, order)) {
            order.setOrderStatus(OrderStatus.PAID);
            addOrder(order);
            for (Item item : order.getItems()) {
                storageService.removeQuantityFromShopStorages(item.getQuantity(), shopId, item.getProduct().getId());
            }
            return true;
        }
        return false;
    }

    @Override
    public Order addOrder(Order order) {
        return orderPersistenceService.save(order);
    }

    @Override
    public Order updateOrder(Order order, Long id) {
        order.setId(id);
        return orderPersistenceService.update(order);
    }

    @Override
    public void deleteOrder(Long id) {
        orderPersistenceService.deleteById(id);
    }

    @Override
    public Order findOrder(Long id) {
        return orderPersistenceService.findById(id);
    }

    @Override
    public List<Order> findByCustomer(Long customerId) {
        return orderPersistenceService.findAll().stream()
                .filter(order -> order.getCustomer()!=null)
                .filter(order -> order.getCustomer().getId().equals(customerId))
                .toList();
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
