package com.company.core.services.persistenceservices;

import java.util.Collection;
import java.util.List;

public interface ShopStoragePersistence {
    List<Long> getStorages(Long shopId);
    List<Long> getShops(Long storageId);
    void addBond(Long shopId, Long storageId);
    void deleteBond(Long shopId, Long storageId);

}
