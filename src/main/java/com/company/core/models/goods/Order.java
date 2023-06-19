package com.company.core.models.goods;

import com.company.core.models.user.customer.Customer;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

public class Order implements Identifiable {
    private final HashMap<Product, Item> items = new HashMap<>();
    private final Customer customer;
    private Long id;
    private OrderStatus orderStatus;
    private BigDecimal summaryPrice = BigDecimal.valueOf(0);

    public Order(Collection<Item> items, Customer customer) {
        this.customer = customer;
        for (Item it : items) {
            this.items.put(it.getProduct(), it);
        }
        countPrice();
    }

    private void countPrice() {
        for (Item it : items.values()) {
            summaryPrice = summaryPrice.add(it.getPrice());
        }
    }

    public void setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public BigDecimal getSummaryPrice() {
        return summaryPrice;
    }

    public Collection<Item> getItems() {
        return items.values();
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        String result = "";
        ArrayList<Item> list = new ArrayList<>(items.values());
        result = result.concat("ID: " + id +
                "\nCustomer: " + customer.getUsername() +
                "\nOrder Status: " + orderStatus +
                "\nItems: ");
        for (Item item : list) {
            result = result.concat("> " + item.getProduct().getBrand() + " " + item.getProduct().getName() + ", Price: " + item.getProduct().getPrice() + "\n");
        }
        return result.concat("Summary price: " + summaryPrice + "\n");
    }


    public Customer getCustomer() {
        return customer;
    }
}
