package com.company.core.controllers;

import com.company.core.models.user.customer.Customer;
import com.company.core.services.impl.ShopServiceImpl;

public class ShopController {
    private final ShopServiceImpl shopServiceImpl;

    public ShopController(ShopServiceImpl shopServiceImpl) {
        this.shopServiceImpl = shopServiceImpl;
    }

    public void checkout(Customer customer) {
        if (shopServiceImpl.checkout(customer)) {
            System.out.println("You have bought successfully!");
            System.out.println("Money left: " + customer.getWallet());
        } else {
            System.out.println("You don't have enough money to do that or your cart is empty");
        }
    }
}
