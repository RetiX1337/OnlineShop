package com.company.core.services.logicservices;

public interface ProductStorageService {
    Integer getQuantity(Long productId);
    void updateQuantity(Integer quantity, Long storageId, Long productId);
    void addQuantity(Integer quantity, Long storageId, Long productId);
}
