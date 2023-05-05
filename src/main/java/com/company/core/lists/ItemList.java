package com.company.core.lists;

import com.company.core.models.goods.Item;

import java.util.HashMap;

public class ItemList {
    private final HashMap<Long, Item> itemList = new HashMap<>();

    public HashMap<Long, Item> getItemList() {
        return itemList;
    }
}
