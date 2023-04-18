package com.company.core.services.persistenceservices;

import com.company.core.lists.GoodList;
import com.company.core.models.goods.Good;
import com.company.core.models.goods.Product;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class GoodListPersistenceService implements PersistenceInterface<Good> {
    private final GoodList goodList;
    private static GoodListPersistenceService instance;
    private static Long idCounter = 0L;

    private GoodListPersistenceService(GoodList goodList) {
        this.goodList = goodList;
    }

    private HashMap<Long, GoodList.GoodListElement> getList() {
        return goodList.getGoodList();
    }

    @Override
    public Good save(Good entity) {
        getList().get(entity.getProduct().getId()).push(entity);
        entity.setId(idCounter);
        idCounter++;
        return entity;
    }

    @Override
    public Good findById(Long id) {
        return findAll().stream().filter(good -> good.getId().equals(id)).findFirst().orElse(null);
    }

    @Override
    public List<Good> findAll() {
        return getList()
                .values()
                .stream()
                .flatMap(goodListElement -> goodListElement.getGoodElement().stream())
                .collect(Collectors.toList());
    }

    public void addGoodListElement(Product product) {
        getList().put(product.getId(), new GoodList.GoodListElement());
    }

    public void removeGoodListElement(Product product) {
        getList().remove(product.getId());
    }

    public List<Long> findAllProducts() {
        return getList().keySet().stream().toList();
    }

    public List<Good> findAllByProductId(Long productId) {
        return getList().get(productId).getGoodElement().stream().toList();
    }

    @Override
    public Good update(Good entity) {
        return null;
    }

    @Override
    public void deleteById(Long id) {
        getList().get(id).pop();
    }

    @Override
    public void delete(Good entity) {
        getList().get(entity.getProduct().getId()).pop();
    }

    public Good findByProductId(Long productId) {
        return getList().get(productId).peek();
    }

    public static GoodListPersistenceService getInstance(GoodList goodList) {
        if (instance == null) {
            instance = new GoodListPersistenceService(goodList);
        }
        return instance;
    }
}
