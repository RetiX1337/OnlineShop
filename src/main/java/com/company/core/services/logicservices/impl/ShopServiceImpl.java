package com.company.core.services.logicservices.impl;

import com.company.core.models.EntityNotFoundException;
import com.company.core.models.Shop;
import com.company.core.models.Storage;
import com.company.core.models.goods.Order;
import com.company.core.models.user.customer.Customer;
import com.company.core.services.logicservices.ShopService;
import com.company.core.services.persistenceservices.PersistenceInterface;

import java.math.BigDecimal;
import java.util.List;

public class ShopServiceImpl implements ShopService {
    private final PersistenceInterface<Shop> shopPersistenceService;
    private final PersistenceInterface<Storage> storagePersistenceService;

    public ShopServiceImpl(PersistenceInterface<Shop> shopPersistenceService,
                           PersistenceInterface<Storage> storagePersistenceService) {
        this.shopPersistenceService = shopPersistenceService;
        this.storagePersistenceService = storagePersistenceService;
    }

    @Override
    public Shop createShop(String name, String address) {
        return new Shop(name, address);
    }

    @Override
    public void addShop(Shop shop) {
        shopPersistenceService.save(shop);
    }

    @Override
    public Shop getShop(Long id) throws EntityNotFoundException {
        if (shopPersistenceService.isPresent(id)) {
            return shopPersistenceService.findById(id);
        } else {
            throw new EntityNotFoundException();
        }
    }

    @Override
    public List<Shop> getAllShops() {
        return shopPersistenceService.findAll();
    }

    @Override
    public void addStorage(Long shopId, Long storageId) {

    }

    @Override
    public void deleteStorage(Long shopId, Long storageId) {

    }

    @Override
    public boolean isPresent(Long shopId) {
        return shopPersistenceService.isPresent(shopId);
    }


}
