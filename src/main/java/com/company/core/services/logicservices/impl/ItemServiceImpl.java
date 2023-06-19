package com.company.core.services.logicservices.impl;

import com.company.core.models.EntityNotFoundException;
import com.company.core.models.goods.Item;
import com.company.core.services.logicservices.ItemService;
import com.company.core.services.logicservices.ProductFormerService;
import com.company.core.services.logicservices.ProductService;
import com.company.core.services.persistenceservices.PersistenceInterface;

public class ItemServiceImpl implements ItemService {
    private final ProductFormerService productFormerService;
    private final PersistenceInterface<Item> itemPersistenceService;

    public ItemServiceImpl(ProductFormerService productFormerService, PersistenceInterface<Item> itemPersistenceService) {
        this.productFormerService = productFormerService;
        this.itemPersistenceService = itemPersistenceService;
    }

    @Override
    public Item createItem(Long productId, Integer quantity) throws EntityNotFoundException {
        return new Item(productFormerService.getProductWithQuantity(productId), quantity);
    }

    @Override
    public Item addItem(Item item) {
        return itemPersistenceService.save(item);
    }

    @Override
    public void deleteItem(Long id) throws EntityNotFoundException {
        if (itemPersistenceService.isPresent(id)) {
            itemPersistenceService.deleteById(id);
        } else {
            throw new EntityNotFoundException();
        }
    }

    @Override
    public void addToItem(Item item, Integer quantity) {
        item.increaseQuantity(quantity);
    }

    @Override
    public void deleteFromItem(Item item, Integer quantity) {
        item.decreaseQuantity(quantity);
    }

    @Override
    public Item findItem(Long id) throws EntityNotFoundException {
        if (itemPersistenceService.isPresent(id)) {
            return itemPersistenceService.findById(id);
        } else {
            throw new EntityNotFoundException();
        }
    }
}
