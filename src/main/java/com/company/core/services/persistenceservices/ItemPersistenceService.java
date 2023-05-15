package com.company.core.services.persistenceservices;

import com.company.core.lists.ItemList;
import com.company.core.models.EntityNotFoundException;
import com.company.core.models.goods.Item;

import java.util.HashMap;
import java.util.List;

public class ItemPersistenceService implements PersistenceInterface<Item> {
    private final ItemList itemList;
    private static Long idCounter = 0L;

    public ItemPersistenceService(ItemList itemList) {
        this.itemList = itemList;
    }

    private HashMap<Long, Item> getList() {
        return itemList.getItemList();
    }

    @Override
    public Item save(Item entity) {
        getList().put(idCounter, entity);
        entity.setId(idCounter);
        idCounter++;
        return entity;
    }

    @Override
    public Item findById(Long id) {
        return getList().get(id);
    }

    @Override
    public List<Item> findAll() {
        return getList().values().stream().toList();
    }

    @Override
    public Item update(Item entity) throws EntityNotFoundException {
        if (getList().containsKey(entity.getId())) {
            getList().put(entity.getId(), entity);
        } else {
            throw new EntityNotFoundException();
        }
        return entity;
    }

    @Override
    public void deleteById(Long id) {
        getList().remove(id);
    }

    @Override
    public void delete(Item entity) {
        getList().remove(entity.getId());
    }
}
