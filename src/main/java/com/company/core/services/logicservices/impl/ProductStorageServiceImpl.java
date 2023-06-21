package com.company.core.services.logicservices.impl;

import com.company.core.services.logicservices.ProductStorageService;
import com.company.core.services.logicservices.ShopStorageService;
import com.company.core.services.persistenceservices.ProductStoragePersistence;

import java.util.List;

public class ProductStorageServiceImpl implements ProductStorageService {
    ProductStoragePersistence productStoragePersistence;
    ShopStorageService shopStorageService;

    public ProductStorageServiceImpl(ProductStoragePersistence productStoragePersistence, ShopStorageService shopStorageService) {
        this.productStoragePersistence = productStoragePersistence;
        this.shopStorageService = shopStorageService;
    }

    @Override
    public boolean removeFromStorage(Integer quantity, Long shopId, Long productId) {
        List<Long> storages = shopStorageService.getStorages(shopId);
        Integer tempQuantity = quantity;
        if (getQuantityPerShop(shopId, productId) >= quantity) {
            for (int i = 0; i < storages.size(); i++) {
                Integer storageQuantity = productStoragePersistence.getQuantity(storages.get(i), productId);
                if (tempQuantity > storageQuantity) {
                    tempQuantity -= storageQuantity;
                    productStoragePersistence.updateQuantity(0, storages.get(i), productId);
                } else {
                    tempQuantity -= tempQuantity;
                    productStoragePersistence.updateQuantity(storageQuantity-tempQuantity, storages.get(i), productId);
                }
                if (tempQuantity==0) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public Integer getQuantityPerShop(Long shopId, Long productId) {
        List<Long> storages = shopStorageService.getStorages(shopId);
        Integer finalQuantity = 0;
        for (int i = 0; i < storages.size(); i++) {
            finalQuantity += productStoragePersistence.getQuantity(storages.get(i), productId);
        }
        return finalQuantity;
    }

    @Override
    public Integer getQuantity(Long storageId, Long productId) {
        return productStoragePersistence.getQuantity(storageId, productId);
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
