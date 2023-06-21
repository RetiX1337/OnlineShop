package com.company.core.services.logicservices;

import java.util.Collection;
import java.util.List;

public interface ShopStorageService {
    List<Long> getShops(Long storageId);
    List<Long> getStorages(Long shopId);
    void addBond(Long shopId, Long storageId);
    void deleteBond(Long shopId, Long storageId);
}
