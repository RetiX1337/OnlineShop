package com.company.core.services.impl;

import com.company.Observer;
import com.company.core.ProductListObserver;
import com.company.core.models.goods.Good;
import com.company.core.models.goods.Product;
import com.company.core.services.persistenceservices.GoodListPersistenceService;

import java.util.Stack;

public class GoodListServiceImpl implements Observer<Product> {
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
            removeGoodListElement(product);
        }
    }

    public Good createGood(Long productId) {
        return new Good(getProduct(productId));
    }

    public void addGood(Good good) {
        glps.save(good);
    }

    public void deleteGood(Long productId) {
        glps.deleteById(productId);
        System.out.println("Deleted probably");
    }

    public void addToGoodList(Long productId, int quantity) {
        for (int i = 0; i < quantity; i++) {
            addGood(createGood(productId));
        }
    }

    private void addGoodListElement(Product product) {
        glps.addGoodListElement(product);
    }

    private void removeGoodListElement(Product product) {
        glps.removeGoodListElement(product);
    }

    public Product getProduct(Long productId) {
        return productListServiceImpl.getProduct(productId);
    }

    public Stack<Good> getGoods(Long productId, int quantity) {
        Stack<Good> goods = new Stack<>();
        for (int i = 0; i < quantity; i++) {
            goods.add(glps.findByProductId(productId));
        }
        return goods;
    }

    public int getGoodListSize() {
        return glps.findAllProducts().size();
    }

    public int getGoodListElementSize(Long productId) {
        return glps.findAllByProductId(productId).size();
    }

    public static GoodListServiceImpl getInstance(GoodListPersistenceService glps, ProductListServiceImpl productListServiceImpl, ProductListObserver productListObserver) {
        if (instance == null) {
            instance = new GoodListServiceImpl(glps, productListServiceImpl, productListObserver);
        }
        return instance;
    }
}
