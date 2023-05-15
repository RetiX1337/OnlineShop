package com.company.core.services;

import com.company.core.models.goods.Item;

public interface ItemService {
    Item createItem(Long productId, Integer quantity);

    Item addItem(Item item);

    void addToItem(Item item, Integer quantity);

    void deleteFromItem(Item item, Integer quantity);
}
