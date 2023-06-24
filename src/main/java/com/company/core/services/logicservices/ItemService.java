package com.company.core.services.logicservices;

import com.company.core.models.EntityNotFoundException;
import com.company.core.models.goods.Item;

public interface ItemService {
    Item createItem(Long productId, Integer quantity) throws EntityNotFoundException;
    Item addItem(Item item);
    void deleteItem(Long id) throws EntityNotFoundException;
    Item findItem(Long id) throws EntityNotFoundException;
}
