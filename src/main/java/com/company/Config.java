package com.company;

import com.company.user.customer.Customer;

public class Config {
    private final Shop shop;
    private final Customer customer;

    public Config(Shop shop, Customer customer) {
        this.shop = shop;
        this.customer = customer;
    }

    public Customer getCustomer() {
        return customer;
    }

    public Shop getShop() {
        return shop;
    }
}
