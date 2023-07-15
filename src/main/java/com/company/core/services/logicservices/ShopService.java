package com.company.core.services.logicservices;

import com.company.core.models.Shop;

import java.util.List;

public interface ShopService {
    Shop createShop(String name, String address);
    void addShop(Shop shop);
    Shop getShop(Long id);
    List<Shop> getAllShops();
    void addStorage(Long shopId, Long storageId);
    void deleteStorage(Long shopId, Long storageId);
    public boolean isPresent(Long shopId);
}
