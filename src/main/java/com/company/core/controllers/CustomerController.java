package com.company.core.controllers;

import com.company.core.services.CustomerService;

public class CustomerController {
    private final CustomerService customerService;
    private final ShoppingCartController shoppingCartController;

    public CustomerController(CustomerService customerService, ShoppingCartController shoppingCartController) {
        this.customerService = customerService;
        this.shoppingCartController = shoppingCartController;
    }

    public boolean buy() {
        if (customerService.buy()) {
            System.out.println("You have bought successfully!");
            System.out.println("Money left: " + customerService.getWallet());
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
