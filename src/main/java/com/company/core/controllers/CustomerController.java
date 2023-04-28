package com.company.core.controllers;

import com.company.core.services.impl.CustomerServiceImpl;

public class CustomerController {
    private final CustomerServiceImpl customerServiceImpl;
    private final ShoppingCartController shoppingCartController;

    public CustomerController(CustomerServiceImpl customerServiceImpl, ShoppingCartController shoppingCartController) {
        this.customerServiceImpl = customerServiceImpl;
        this.shoppingCartController = shoppingCartController;
    }

    public void addToCart() {
        shoppingCartController.addToCart();
    }
}
