package com.company.core.services.logicservices;

import com.company.core.models.goods.Item;
import org.springframework.stereotype.Service;

@Service
public interface ItemService {
    Item createItem(Long productId, Integer quantity);
    Item addItem(Item item);
    void deleteItem(Long id);
    Item findItem(Long id);
}
