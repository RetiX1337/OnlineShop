package com.company.core.services.logicservices.impl;

import com.company.core.models.EntityNotFoundException;
import com.company.core.services.logicservices.ProductStorageService;
import com.company.core.services.persistenceservices.ProductStoragePersistence;
import com.company.core.services.persistenceservices.dbimpl.ProductStoragePersistenceServiceDatabase;

public class ProductStorageServiceImpl implements ProductStorageService {
    ProductStoragePersistence productStoragePersistence;

    public ProductStorageServiceImpl(ProductStoragePersistence productStoragePersistence) {
        this.productStoragePersistence = productStoragePersistence;
    }

    @Override
    public Integer getQuantity(Long storageId, Long productId) {
        if (productStoragePersistence.isPresent(storageId, productId)) {
            return getQuantity(storageId, productId);
        } else {
            return null;
        }
    }

    @Override
    public void updateQuantity(Integer quantity, Long storageId, Long productId) {
        if (productStoragePersistence.isPresent(storageId, productId)) {
            productStoragePersistence.updateQuantity(quantity, storageId, productId);
        }
    }

    @Override
    public void addQuantity(Integer quantity, Long storageId, Long productId) {
        if (productStoragePersistence.isPresent(storageId, productId)) {
            updateQuantity(quantity, storageId, productId);
        } else {
            productStoragePersistence.addQuantity(quantity, storageId, productId);
        }
    }
}
