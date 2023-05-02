package com.company.core.services.impl;

import com.company.core.models.goods.Item;
import com.company.core.models.goods.Product;
import com.company.core.services.ItemService;
import com.company.core.services.ProductListService;

public class ItemServiceImpl implements ItemService {
    private static ItemService instance;
    private final ProductListService productListService;

    private ItemServiceImpl(ProductListService productListService) {
        this.productListService = productListService;
    }

    @Override
    public Item createItem(Long productId, Integer quantity) {
        return new Item(productListService.getProduct(productId), quantity);
    }

    @Override
    public void addToItem(Item item) {
        item.update(item.getQuantity());
    }

    public static ItemService getInstance(ProductListService productListService) {
        if (instance == null) {
            instance = new ItemServiceImpl(productListService);
        }
        return instance;
    }
}
