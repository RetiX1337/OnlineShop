package com.company.core.controllers;

import com.company.core.services.impl.CustomerServiceImpl;

public class CustomerController {
    private final CustomerServiceImpl customerServiceImpl;
    private final ShoppingCartController shoppingCartController;

    public CustomerController(CustomerServiceImpl customerServiceImpl, ShoppingCartController shoppingCartController) {
        this.customerServiceImpl = customerServiceImpl;
        this.shoppingCartController = shoppingCartController;
    }

    public boolean buyFromCart() {
        if (customerServiceImpl.buyFromCart()) {
            System.out.println("You have bought successfully!");
            System.out.println("Money left: " + customerServiceImpl.getWallet());
            return true;
        } else {
            System.out.println("You don't have enough money to do that or your cart is empty");
            return false;
        }
    }

    public void addToCart() {
        shoppingCartController.addToCart();
    }
}
