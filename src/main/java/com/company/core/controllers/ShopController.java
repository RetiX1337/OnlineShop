package com.company.core.controllers;

import com.company.core.models.user.customer.Customer;
import com.company.core.services.impl.ShopServiceImpl;

public class ShopController {
    private final ShopServiceImpl shopServiceImpl;

    public ShopController(ShopServiceImpl shopServiceImpl) {
        this.shopServiceImpl = shopServiceImpl;
    }


}
