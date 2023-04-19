package com.company.core.services;

import com.company.core.models.goods.Good;
import com.company.core.models.goods.Product;

import java.util.Stack;

public interface GoodListService {
    Good createGood(Long productId);
    void addGood(Good good);
    void deleteGood(Long productId);
    void addGoodListElement(Product product);
    void deleteGoodListElement(Product product);
    Stack<Good> getGoods(Long productId, int quantity);
}
