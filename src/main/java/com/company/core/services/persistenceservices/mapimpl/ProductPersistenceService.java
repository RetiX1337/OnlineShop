package com.company.core.services.persistenceservices.mapimpl;

import com.company.core.models.goods.Product;
import com.company.core.services.persistenceservices.PersistenceInterface;

import java.util.HashMap;
import java.util.List;

public class ProductPersistenceService implements PersistenceInterface<Product> {
    private final HashMap<Long, Product> products;
    private static Long idCounter = 0L;

    public ProductPersistenceService() {
        products = new HashMap<>();
    }

    @Override
    public Product save(Product entity) {
        products.put(idCounter, entity);
        entity.setId(idCounter);
        idCounter++;
        return entity;
    }

    @Override
    public Product findById(Long id) {
        return products.get(id);
    }

    @Override
    public List<Product> findAll() {
        return products.values().stream().toList();
    }

    @Override
    public Product update(Product entity, Long id) {
        products.put(id, entity);
        return entity;
    }

    @Override
    public void deleteById(Long id) {
        products.remove(id);
    }


    @Override
    public boolean isPresent(Long id){
        return products.containsKey(id);
    }
}
