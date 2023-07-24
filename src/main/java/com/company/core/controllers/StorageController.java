package com.company.core.controllers;

import com.company.core.models.Storage;
import com.company.core.services.logicservices.StorageService;
import org.springframework.stereotype.Controller;

@Controller
public class StorageController {
    private final StorageService storageService;

    public StorageController(StorageService storageService) {
        this.storageService = storageService;
    }

    public void addProductQuantity(Integer quantity, Long storageId, Long productId) {
        storageService.addProductQuantity(quantity, storageId, productId);
    }

    public Storage addStorage(Storage storage) {
        return storageService.addStorage(storage);
    }

    public Storage getStorage(Long storageId) {
        return storageService.getStorage(storageId);
    }
}
