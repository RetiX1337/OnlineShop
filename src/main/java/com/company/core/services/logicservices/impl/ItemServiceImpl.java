package com.company.core.services.logicservices.impl;

import com.company.core.exceptions.EntityNotFoundException;
import com.company.core.models.goods.Item;
import com.company.core.services.logicservices.ItemService;
import com.company.core.services.logicservices.ProductService;
import com.company.core.services.persistenceservices.PersistenceInterface;
import org.springframework.stereotype.Service;

@Service
public class ItemServiceImpl implements ItemService {
    private final PersistenceInterface<Item> itemPersistenceService;
    private final ProductService productService;

    public ItemServiceImpl(PersistenceInterface<Item> itemPersistenceService, ProductService productService) {
        this.itemPersistenceService = itemPersistenceService;
        this.productService = productService;
    }

    @Override
    public Item createItem(Long productId, Integer quantity) {
        return new Item(productService.getProduct(productId), quantity);
    }

    @Override
    public Item addItem(Item item) {
        return itemPersistenceService.save(item);
    }

    @Override
    public void deleteItem(Long id) throws EntityNotFoundException {
        itemPersistenceService.deleteById(id);
    }

    @Override
    public Item findItem(Long id) {
        return itemPersistenceService.findById(id);
    }
}
