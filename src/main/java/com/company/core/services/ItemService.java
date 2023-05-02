package com.company.core.services;

import com.company.core.models.goods.Item;

public interface ItemService {
    Item createItem(Long productId, Integer quantity);
    void addToItem(Item item);
}
