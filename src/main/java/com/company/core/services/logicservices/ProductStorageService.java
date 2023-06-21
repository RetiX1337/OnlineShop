package com.company.core.services.logicservices;

public interface ProductStorageService {
    Integer getQuantityPerShop(Long shopId, Long productId);
    boolean removeFromStorage(Integer quantity, Long shopId, Long productId);
    Integer getQuantity(Long storageId, Long productId);
    void updateQuantity(Integer quantity, Long storageId, Long productId);
    void addQuantity(Integer quantity, Long storageId, Long productId);
}
