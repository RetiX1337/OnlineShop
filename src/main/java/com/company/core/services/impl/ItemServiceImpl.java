package com.company.core.services.impl;

import com.company.core.models.goods.Item;
import com.company.core.models.goods.Product;

public class ItemServiceImpl {
    private static ItemServiceImpl instance;
    private final ProductListServiceImpl productListService;

    private ItemServiceImpl(ProductListServiceImpl productListService) {
        this.productListService = productListService;
    }

    public Item createItem(Long productId, Integer quantity) {
        return new Item(productListService.getProduct(productId), quantity);
    }

    public void addToItem(Item item) {
        item.update(item.getQuantity());
    }

    public void deleteFromItem(Item item, Integer quantity) {
        item.update(quantity * -1);
    }

    public static ItemServiceImpl getInstance(ProductListServiceImpl productListService) {
        if (instance == null) {
            instance = new ItemServiceImpl(productListService);
        }
        return instance;
    }
}
