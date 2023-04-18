package com.company.core.services.impl;

import com.company.core.models.goods.Good;
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

    public boolean buyFromCart() {
        if (!customer.getShoppingCart().isEmpty()) {
            BigDecimal summaryPrice = new BigDecimal(0);
            HashMap<Product, Stack<Good>> goods = customer.getShoppingCart().getGoodsCart();
            summaryPrice = countPrice(summaryPrice, goods);
            if (summaryPrice.compareTo(customer.getWallet()) == 0 || summaryPrice.compareTo(customer.getWallet()) == -1) {
                customer.setWallet(customer.getWallet().subtract(summaryPrice));
                return true;
            } else {
                return false;
            }
        }
        return false;
    }

    public BigDecimal getWallet() {
        return customer.getWallet();
    }

    private BigDecimal countPrice(BigDecimal summaryPrice, HashMap<Product, Stack<Good>> goods) {
        for (Stack<Good> goodStack : goods.values()) {
            for (Good good : goodStack) {
                summaryPrice = summaryPrice.add(good.getProduct().getPrice());
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
