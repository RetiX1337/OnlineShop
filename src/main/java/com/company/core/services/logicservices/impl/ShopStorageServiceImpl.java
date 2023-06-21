package com.company.core.services.logicservices.impl;

import com.company.core.services.logicservices.ShopStorageService;
import com.company.core.services.persistenceservices.ShopStoragePersistence;

import java.util.Collection;
import java.util.List;

public class ShopStorageServiceImpl implements ShopStorageService {
    private final ShopStoragePersistence shopStoragePersistence;

    public ShopStorageServiceImpl(ShopStoragePersistence shopStoragePersistence) {
        this.shopStoragePersistence = shopStoragePersistence;
    }

    @Override
    public List<Long> getShops(Long storageId) {
        return shopStoragePersistence.getShops(storageId);
    }

    @Override
    public List<Long> getStorages(Long shopId) {
        return shopStoragePersistence.getStorages(shopId);
    }

    @Override
    public void addBond(Long shopId, Long storageId) {
        shopStoragePersistence.addBond(shopId, storageId);
    }

    @Override
    public void deleteBond(Long shopId, Long storageId) {
        shopStoragePersistence.deleteBond(shopId, storageId);
    }
}
