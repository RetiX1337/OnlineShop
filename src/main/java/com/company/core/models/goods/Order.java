package com.company.core.models.goods;

import com.company.core.models.user.customer.Customer;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.*;

@Entity
@Table(name = "order")
public class Order implements Identifiable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @OneToMany(mappedBy = "order", cascade = {CascadeType.ALL, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REMOVE})
    private Set<Item> items;
    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;
    @Enumerated(EnumType.ORDINAL)
    @Column(name = "order_status_id")
    private OrderStatus orderStatus;
    @Column(name = "summary_price")
    private BigDecimal summaryPrice = BigDecimal.valueOf(0);

    public Order(Set<Item> items, Customer customer) {
        this.customer = customer;
        this.items = items;
        countPrice();
    }

    public Order(Long id, Set<Item> items, Customer customer, BigDecimal summaryPrice, OrderStatus orderStatus) {
        this.id = id;
        this.customer = customer;
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

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
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
                "\nCustomer: " + customer.getUsername() +
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
