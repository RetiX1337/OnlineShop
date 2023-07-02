package com.company.core.controllers;

import com.company.core.models.EntityNotFoundException;
import com.company.core.models.Shop;
import com.company.core.services.logicservices.ShopService;

public class ShopController {
    private final ShopService shopService;

    public ShopController(ShopService shopService) {
        this.shopService = shopService;
    }

    public Shop findShop(Long shopId) {
        try {
            return shopService.getShop(shopId);
        } catch (EntityNotFoundException e) {
            System.out.println("This shop doesn't exist");
        }
        return null;
    }

    public boolean isPresent(Long shopId) {
        return shopService.isPresent(shopId);
    }
}
