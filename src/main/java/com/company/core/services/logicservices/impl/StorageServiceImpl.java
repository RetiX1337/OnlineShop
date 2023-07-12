package com.company.core.services.logicservices.impl;

import com.company.core.models.EntityNotFoundException;
import com.company.core.models.Shop;
import com.company.core.models.Storage;
import com.company.core.models.goods.Product;
import com.company.core.models.goods.ProductWithQuantity;
import com.company.core.services.logicservices.ProductService;
import com.company.core.services.logicservices.StorageService;
import com.company.core.services.persistenceservices.PersistenceInterface;

import java.util.HashMap;
import java.util.List;

public class StorageServiceImpl implements StorageService {
    private final PersistenceInterface<Storage> storagePersistenceService;
    private final PersistenceInterface<Shop> shopPersistenceService;
    private final ProductService productService;

    public StorageServiceImpl(PersistenceInterface<Storage> storagePersistenceService,
                              PersistenceInterface<Shop> shopPersistenceService,
                              ProductService productService) {
        this.storagePersistenceService = storagePersistenceService;
        this.shopPersistenceService = shopPersistenceService;
        this.productService = productService;
    }

    @Override
    public Storage createStorage(String name, String address) {
        return new Storage(name, address);
    }

    @Override
    public void addStorage(Storage storage) {
        storagePersistenceService.save(storage);
    }

    @Override
    public Storage getStorage(Long id) {
        return storagePersistenceService.findById(id);
    }

    @Override
    public List<Storage> getAllStorages() {
        return storagePersistenceService.findAll();
    }

    @Override
    public Integer getQuantityPerShop(Long shopId, Long productId) {
        List<Long> storages = shopPersistenceService.findById(shopId).getStorages();
        Integer finalQuantity = 0;

        for (Long storage : storages) {
            HashMap<Long, ProductWithQuantity> productQuantities = storagePersistenceService.findById(storage).getProductQuantities();

            if (productQuantities.get(productId) != null) {
                finalQuantity += productQuantities.get(productId).getQuantity();
            }
        }

        return finalQuantity;
    }

    @Override
    public boolean removeQuantityFromShopStorages(Integer quantity, Long shopId, Long productId) {
        List<Long> storages = shopPersistenceService.findById(shopId).getStorages();
        Integer tempQuantity = quantity;

        if ((getQuantityPerShop(shopId, productId)) < quantity) {
            return false;
        }

        for (Long storageId : storages) {
            Storage storage = storagePersistenceService.findById(storageId);
            Integer storageQuantity = storage.getProductQuantities().get(productId).getQuantity();

            if (tempQuantity > storageQuantity) {
                tempQuantity -= storageQuantity;
                storage.updateQuantity(productId, 0);
            } else {
                storage.updateQuantity(productId, storageQuantity - tempQuantity);
                tempQuantity = 0;
            }

            if (tempQuantity == 0) {
                storagePersistenceService.update(storage);
                return true;
            }
        }

        return false;
    }

    @Override
    public void addProductQuantity(Integer quantity, Long storageId, Long productId) throws EntityNotFoundException {
        ProductWithQuantity productWithQuantity = new ProductWithQuantity(productService.getProduct(productId), quantity);
        Storage storage = storagePersistenceService.findById(storageId);

        if (storage.getProductQuantities().containsKey(productId)) {
            ProductWithQuantity oldProductWithQuantity = storage.getProductQuantities().get(productId);
            oldProductWithQuantity.setQuantity(oldProductWithQuantity.getQuantity() + quantity);
        } else {
            storage.getProductQuantities().put(productId, productWithQuantity);
        }

        storagePersistenceService.update(storage);
    }

    @Override
    public void deleteProductQuantity(Integer quantity, Long storageId, Long productId) {
        Storage storage = storagePersistenceService.findById(storageId);

        if (storage.getProductQuantities().containsKey(productId)) {
            ProductWithQuantity productWithQuantity = storage.getProductQuantities().get(productId);
            productWithQuantity.setQuantity(Math.max(productWithQuantity.getQuantity() - quantity, 0));
            storagePersistenceService.update(storage);
        }
    }

    @Override
    public void addShop(Long storageId, Long shopId) {
        Storage storage = storagePersistenceService.findById(storageId);

        if (shopPersistenceService.isPresent(shopId)) {
            storage.addShop(shopId);
            storagePersistenceService.update(storage);
        }
    }

    @Override
    public void deleteShop(Long storageId, Long shopId) {
        Storage storage = storagePersistenceService.findById(storageId);

        if (storage.getShops().contains(shopId)) {
            storage.deleteShop(shopId);
        }
    }
}
