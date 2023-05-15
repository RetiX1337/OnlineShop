package com.company.core.services.impl;

import com.company.core.models.goods.Item;
import com.company.core.services.ItemService;
import com.company.core.services.ProductListService;
import com.company.core.services.persistenceservices.ItemPersistenceService;

public class ItemServiceImpl implements ItemService {
    private final ProductListService productListService;
    private final ItemPersistenceService ips;

    public ItemServiceImpl(ProductListService productListService, ItemPersistenceService ips) {
        this.productListService = productListService;
        this.ips = ips;
    }

    @Override
    public Item createItem(Long productId, Integer quantity) {
        return new Item(productListService.getProduct(productId), quantity);
    }

    @Override
    public Item addItem(Item item) {
        return ips.save(item);
    }

    @Override
    public void addToItem(Item item, Integer quantity) {
        item.increaseQuantity(quantity);
    }

    @Override
    public void deleteFromItem(Item item, Integer quantity) {
        item.decreaseQuantity(quantity);
    }

}
