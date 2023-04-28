package com.company.core.controllers;

import com.company.core.services.impl.ItemServiceImpl;

public class ItemController {
    private final ItemServiceImpl itemService;

    public ItemController(ItemServiceImpl itemService) {
        this.itemService = itemService;
    }
}
