package com.company.core.services.logicservices;

import com.company.core.models.Storage;
import com.company.core.models.goods.ProductWithQuantity;
import org.stringtemplate.v4.ST;

import java.util.List;

public interface StorageService {
    Storage createStorage(String name, String address);
    Storage addStorage(Storage storage);
    Storage getStorage(Long id);
    List<Storage> getAllStorages();
    Integer getQuantityPerShop(Long shopId, Long productId);

    List<ProductWithQuantity> getProductsWithQuantity(Long shopId);

    boolean removeQuantityFromShopStorages(Integer quantity, Long shopId, Long productId);
    void addProductQuantity(Integer quantity, Long storageId, Long productId);
    void deleteProductQuantity(Integer quantity, Long storageId, Long productId);
}
