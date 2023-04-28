package com.company.core.services.impl;

import com.company.core.models.goods.Item;
import com.company.core.models.goods.Product;
import com.company.core.models.user.customer.Customer;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Stack;

public class CustomerServiceImpl {
    private final Customer customer;
    private static CustomerServiceImpl instance;

    private CustomerServiceImpl(Customer customer) {
        this.customer = customer;
    }

    public BigDecimal getWallet() {
        return customer.getWallet();
    }

    private BigDecimal countPrice(BigDecimal summaryPrice, HashMap<Product, Stack<Item>> goods) {
        for (Stack<Item> itemStack : goods.values()) {
            for (Item item : itemStack) {
                summaryPrice = summaryPrice.add(item.getProduct().getPrice());
            }
        }
        return summaryPrice;
    }

    public static CustomerServiceImpl getInstance(Customer customer) {
        if (instance == null) {
            instance = new CustomerServiceImpl(customer);
        }
        return instance;
    }
}
