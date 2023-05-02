package com.company.core.models.goods;

import com.company.core.models.user.customer.Customer;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.HashMap;

public class Order implements Identifiable {
    private final HashMap<Product, Item> items = new HashMap<>();
    private final Customer customer;
    private Long id;
    private OrderStatus orderStatus;
    private BigDecimal summaryPrice = new BigDecimal(0);

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
        return "ID: " + id +
                " Items: " + items +
                " Customer: " + customer +
                " summaryPrice=" + summaryPrice;
    }

    private void addItem(Item item) {
        items.put(item.getProduct(), item);
    }

    public Customer getCustomer() {
        return customer;
    }
}
