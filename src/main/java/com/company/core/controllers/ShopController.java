package com.company.core.controllers;

import com.company.core.models.user.customer.Customer;
import com.company.core.services.ShopService;

public class ShopController {
    private final ShopService shopService;

    public ShopController(ShopService shopService) {
        this.shopService = shopService;
    }

    public void addToCart() {

    }

    public void checkout(Customer customer) {
        shopService.checkout(customer);
    }
}
