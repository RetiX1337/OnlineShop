package com.company.core.services.persistenceservices;

import com.company.core.lists.OrderList;
import com.company.core.lists.ProductList;
import com.company.core.models.EntityNotFoundException;
import com.company.core.models.goods.Product;

import java.util.HashMap;
import java.util.List;

public class ProductListPersistenceService implements PersistenceInterface<Product> {
    private static ProductListPersistenceService instance;
    private final ProductList productList;
    private static Long idCounter = 0L;

    private ProductListPersistenceService(ProductList productList) {
        this.productList = productList;
    }

    private HashMap<Long, Product> getList() {
        return productList.getProductList();
    }

    @Override
    public Product save(Product entity) {
        getList().put(idCounter, entity);
        entity.setId(idCounter);
        idCounter++;
        return entity;
    }

    @Override
    public Product findById(Long id) {
        return getList().get(id);
    }

    @Override
    public List<Product> findAll() {
        return getList().values().stream().toList();
    }

    @Override
    public Product update(Product entity) throws EntityNotFoundException {
        if(getList().containsKey(entity.getId())) {
            getList().put(entity.getId(), entity);
        } else {
            throw new EntityNotFoundException();
        }
        return null;
    }

    @Override
    public void deleteById(Long id) {
        getList().remove(id);
    }

    @Override
    public void delete(Product product) {
        getList().remove(product.getId());
    }

    public boolean productIsPresent(Product product) {
        return getList().containsKey(product.getId());
    }

    public static ProductListPersistenceService getInstance(ProductList productList) {
        if (instance == null) {
            instance = new ProductListPersistenceService(productList);
        }
        return instance;
    }
}
