package com.company.core.services.logicservices;

import com.company.core.models.EntityNotFoundException;
import com.company.core.models.Shop;
import com.company.core.models.Storage;

import java.util.List;

public interface StorageService {
    Storage createStorage(String name, String address);
    void addStorage(Storage storage);
    Storage getStorage(Long id) throws EntityNotFoundException;
    List<Storage> getAllStorages();
    Integer getQuantityPerShop(Long shopId, Long productId);
    boolean removeQuantityFromShopStorages(Integer quantity, Long shopId, Long productId);
    void addProductQuantity(Integer quantity, Long storageId, Long productId) throws EntityNotFoundException;
    void deleteProductQuantity(Integer quantity, Long storageId, Long productId);
    void addShop(Long storageId, Long shopId);
    void deleteShop(Long storageId, Long shopId);
}
