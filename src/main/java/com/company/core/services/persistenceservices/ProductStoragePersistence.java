package com.company.core.services.persistenceservices;

public interface ProductStoragePersistence {
    Integer getQuantity(Long storageId, Long productId);

    void updateQuantity(Integer quantity, Long storageId, Long productId);
    void addQuantity(Integer quantity, Long storageId, Long productId);
    boolean isPresent(Long storageId, Long productId);
}
