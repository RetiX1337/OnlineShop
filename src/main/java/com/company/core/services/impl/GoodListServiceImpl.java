package com.company.core.services.impl;

import com.company.core.Observer;
import com.company.core.ProductListObserver;
import com.company.core.models.goods.Good;
import com.company.core.models.goods.Product;
import com.company.core.services.GoodListService;
import com.company.core.services.persistenceservices.GoodListPersistenceService;

import java.util.Stack;

public class GoodListServiceImpl implements GoodListService, Observer<Product> {
    private final GoodListPersistenceService glps;
    private final ProductListServiceImpl productListServiceImpl;
    private final ProductListObserver productListObserver;
    private static GoodListServiceImpl instance;

    private GoodListServiceImpl(GoodListPersistenceService glps, ProductListServiceImpl productListServiceImpl, ProductListObserver productListObserver) {
        this.glps = glps;
        this.productListServiceImpl = productListServiceImpl;
        this.productListObserver = productListObserver;
        productListObserver.addObserver(this);
    }

    @Override
    public void update(Product product) {
        if (productListServiceImpl.productIsPresent(product)) {
            addGoodListElement(product);
        } else {
            deleteGoodListElement(product);
        }
    }

    @Override
    public Good createGood(Long productId) {
        return new Good(getProduct(productId));
    }

    @Override
    public void addGood(Good good) {
        glps.save(good);
    }

    @Override
    public void deleteGood(Long productId) {
        glps.deleteById(productId);
    }

    @Override
    public void addGoodListElement(Product product) {
        glps.addGoodListElement(product);
    }

    @Override
    public void deleteGoodListElement(Product product) {
        glps.removeGoodListElement(product);
    }

    @Override
    public Stack<Good> getGoods(Long productId, int quantity) {
        Stack<Good> goods = new Stack<>();
        if (quantity <= glps.findAllByProductId(productId).size()) {
            for (int i = 0; i < quantity; i++) {
                goods.add(glps.findByProductId(productId));
            }
        }
        return goods;
    }

    public void addToGoodList(Long productId, int quantity) {
        for (int i = 0; i < quantity; i++) {
            addGood(createGood(productId));
        }
    }

    public int getGoodListSize() {
        return glps.findAllProducts().size();
    }

    public int getGoodListElementSize(Long productId) {
        return glps.findAllByProductId(productId).size();
    }

    public Product getProduct(Long productId) {
        return productListServiceImpl.getProduct(productId);
    }

    public static GoodListServiceImpl getInstance(GoodListPersistenceService glps, ProductListServiceImpl productListServiceImpl, ProductListObserver productListObserver) {
        if (instance == null) {
            instance = new GoodListServiceImpl(glps, productListServiceImpl, productListObserver);
        }
        return instance;
    }
}
