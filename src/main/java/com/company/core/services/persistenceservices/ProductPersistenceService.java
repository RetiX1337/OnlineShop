package com.company.core.services.persistenceservices;

import com.company.core.models.EntityNotFoundException;
import com.company.core.models.goods.Product;

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
    public void delete(Product product) {
        products.remove(product.getId());
    }

    @Override
    public boolean isPresent(Long id){
        return products.containsKey(id);
    }
}