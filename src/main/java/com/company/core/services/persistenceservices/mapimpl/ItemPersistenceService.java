package com.company.core.services.persistenceservices.mapimpl;

import com.company.core.models.goods.Item;
import com.company.core.services.persistenceservices.PersistenceInterface;

import java.util.HashMap;
import java.util.List;

public class ItemPersistenceService implements PersistenceInterface<Item> {
    private final HashMap<Long, Item> items;
    private static Long idCounter = 0L;

    public ItemPersistenceService() {
        items = new HashMap<>();
    }

    @Override
    public Item save(Item entity) {
        items.put(idCounter, entity);
        entity.setId(idCounter);
        idCounter++;
        return entity;
    }

    @Override
    public Item findById(Long id) {
        return items.get(id);
    }

    @Override
    public List<Item> findAll() {
        return items.values().stream().toList();
    }

    @Override
    public Item update(Item entity) {
        items.put(entity.getId(), entity);
        return entity;
    }

    @Override
    public void deleteById(Long id) {
        items.remove(id);
    }


    @Override
    public boolean isPresent(Long id){
        return items.containsKey(id);
    }
}
