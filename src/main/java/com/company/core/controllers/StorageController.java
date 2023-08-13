package com.company.core.controllers;

import com.company.core.services.logicservices.StorageService;

public class StorageController {
    private final StorageService storageService;

    public StorageController(StorageService storageService) {
        this.storageService = storageService;
    }

    public void addProductQuantity(Integer quantity, Long storageId, Long productId) {
        storageService.addProductQuantity(quantity, storageId, productId);
    }
}
