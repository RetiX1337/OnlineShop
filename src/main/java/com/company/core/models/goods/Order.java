package com.company.core.models.goods;

import com.company.core.models.user.User;

import java.math.BigDecimal;
import java.util.*;

public class Order implements Identifiable {
    private Set<Item> items;
    private User user;
    private Long id;
    private OrderStatus orderStatus;
    private BigDecimal summaryPrice = BigDecimal.valueOf(0);

    public Order(Set<Item> items, User user) {
        this.user = user;
        this.items = items;
        countPrice();
    }

    public Order(Long id, Set<Item> items, User user, BigDecimal summaryPrice, OrderStatus orderStatus) {
        this.id = id;
        this.user = user;
        this.summaryPrice = summaryPrice;
        this.orderStatus = orderStatus;
        this.items = items;
    }

    public Order() {}

    public void setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public BigDecimal getSummaryPrice() {
        return summaryPrice;
    }

    public void setSummaryPrice(BigDecimal summaryPrice) {
        this.summaryPrice = summaryPrice;
    }

    public Set<Item> getItems() {
        return new HashSet<>(items);
    }

    public void setItems(Set<Item> items) {
        this.items = items;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
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
        List<Item> list = new ArrayList<>(items);
        result = result.concat("ID: " + id +
                "\nUser: " + user.getUsername() +
                "\nOrder Status: " + orderStatus +
                "\nItems: ");
        for (Item item : list) {
            result = result.concat("> " + item.getProduct().getBrand() + " " + item.getProduct().getName() + ", Price: " + item.getProduct().getPrice() + "\n");
        }
        return result.concat("Summary price: " + summaryPrice + "\n");
    }

    private void countPrice() {
        for (Item item : items) {
            summaryPrice = summaryPrice.add(item.getPrice());
        }
    }
}
