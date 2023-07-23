package com.company.core.controllers;

import com.company.core.models.Shop;
import com.company.core.models.Storage;
import com.company.core.services.logicservices.ShopService;

public class ShopController {
    private final ShopService shopService;

    public ShopController(ShopService shopService) {
        this.shopService = shopService;
    }

    public Shop findShop(Long shopId) {
        return shopService.getShop(shopId);
    }

    public void addStorage(Long shopId, Long storageId) {
        shopService.addStorage(shopId, storageId);
    }

    public boolean isPresent(Long shopId) {
        return shopService.isPresent(shopId);
    }
}
