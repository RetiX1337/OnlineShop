package com.company.core.services.impl;

import com.company.core.models.EntityNotFoundException;
import com.company.core.models.goods.Item;
import com.company.core.services.ItemService;
import com.company.core.services.ProductService;
import com.company.core.services.persistenceservices.ItemPersistenceService;

public class ItemServiceImpl implements ItemService {
    private final ProductService productService;
    private final ItemPersistenceService itemPersistenceService;

    public ItemServiceImpl(ProductService productService, ItemPersistenceService itemPersistenceService) {
        this.productService = productService;
        this.itemPersistenceService = itemPersistenceService;
    }

    @Override
    public Item createItem(Long productId, Integer quantity) throws EntityNotFoundException {
        return new Item(productService.getProduct(productId), quantity);
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