package com.company.core.services;

import com.company.core.models.goods.Good;
import com.company.core.models.goods.Product;

import java.util.Stack;

public interface GoodListService {
    void createGood(Long productId);
    void addToGoodList(Long productId, int quantity);
    Product getProduct(Long productId);
    Stack<Good> getGoods(Long productId, int quantity);
    int getGoodListSize();
    int getGoodListElementSize(Long productId);
}
