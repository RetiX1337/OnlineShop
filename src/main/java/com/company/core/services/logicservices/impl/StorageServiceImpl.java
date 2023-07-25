package com.company.core.services.logicservices.impl;

import com.company.core.PersistenceServiceBeanFactory;
import com.company.core.models.Shop;
import com.company.core.models.Storage;
import com.company.core.models.goods.Product;
import com.company.core.models.goods.ProductBase;
import com.company.core.models.goods.ProductWithQuantity;
import com.company.core.services.logicservices.ProductService;
import com.company.core.services.logicservices.StorageService;
import com.company.core.services.persistenceservices.PersistenceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
@Service
public class StorageServiceImpl implements StorageService {
    private final PersistenceInterface<Storage> storagePersistenceService;
    private final PersistenceInterface<Shop> shopPersistenceService;
    private final ProductService productService;

    @Autowired
    public StorageServiceImpl(@Autowired PersistenceServiceBeanFactory persistenceServiceBeanFactory,
                              @Autowired ProductService productService) {
        this.storagePersistenceService = persistenceServiceBeanFactory.getPersistenceBean(Storage.class);
        this.shopPersistenceService = persistenceServiceBeanFactory.getPersistenceBean(Shop.class);
        this.productService = productService;
    }

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
    public Storage addStorage(Storage storage) {
        return storagePersistenceService.save(storage);
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
        Shop shop = shopPersistenceService.findById(shopId);

        if (shop == null) {
            return 0;
        }

        Set<Storage> storages = shop.getStorages();

        Integer finalQuantity = 0;

        for (Storage storage : storages) {
            if (storage.getProductWithQuantity(productId) != null) {
                finalQuantity += storage.getProductWithQuantity(productId).getQuantity();
            }
        }

        return finalQuantity;
    }

    @Override
    public List<ProductWithQuantity> getProductsWithQuantity(Long shopId) {
        List<ProductBase> products = productService.getAllProducts();
        List<ProductWithQuantity> productsWithQuantity = new ArrayList<>();

        for (ProductBase product : products) {
            productsWithQuantity.add(new ProductWithQuantity(product, getQuantityPerShop(shopId, product.getId())));
        }

        return productsWithQuantity;
    }

    @Override
    public boolean removeQuantityFromShopStorages(Integer quantity, Long shopId, Long productId) {
        Set<Storage> storages = shopPersistenceService.findById(shopId).getStorages();
        Integer tempQuantity = quantity;

        if ((getQuantityPerShop(shopId, productId)) < quantity) {
            return false;
        }


        for (Storage storage : storages) {
            ProductWithQuantity productWithQuantity = storage.getProductWithQuantity(productId);

            if (productWithQuantity == null) {
                continue;
            }

            Integer storageQuantity = storage.getProductWithQuantity(productId).getQuantity();

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
    public void addProductQuantity(Integer quantity, Long storageId, Long productId) {
        ProductWithQuantity productWithQuantity = new ProductWithQuantity(productService.getProduct(productId), quantity);
        Storage storage = storagePersistenceService.findById(storageId);

        ProductWithQuantity oldProductWithQuantity = storage.getProductWithQuantity(productId);

        if (oldProductWithQuantity != null) {
            oldProductWithQuantity.setQuantity(oldProductWithQuantity.getQuantity() + quantity);
        } else {
            storage.addQuantity(productWithQuantity);
        }

        storagePersistenceService.update(storage);
    }

    @Override
    public void deleteProductQuantity(Integer quantity, Long storageId, Long productId) {
        Storage storage = storagePersistenceService.findById(storageId);
        if (storage == null) {
            return;
        }

        ProductWithQuantity productWithQuantity = storage.getProductWithQuantity(productId);

        if (productWithQuantity != null) {
            productWithQuantity.setQuantity(Math.max(productWithQuantity.getQuantity() - quantity, 0));
            storagePersistenceService.update(storage);
        }
    }
}
