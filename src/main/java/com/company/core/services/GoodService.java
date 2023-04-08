package com.company.core.services;

import com.company.core.lists.GoodList;
import com.company.core.models.goods.Good;
import com.company.core.models.goods.Product;

import java.util.Stack;

public class GoodService {

    private final GoodList goodList;
    private final ProductService productService;
    private static GoodService instance;

    private GoodService(GoodList goodList, ProductService productService) {
        this.goodList = goodList;
        this.productService = productService;
    }

    public void createGood(int productId) {
        goodList.save(new Good(getProduct(productId)));
    }

    public void fillGoodList(int productId, int quantity) {
        for (int i = 0; i < quantity; i++) {
            createGood(productId);
        }
    }

    public Product getProduct(int productId) {
        return productService.getProduct(productId);
    }

    public Stack<Good> getGoods(int productId, int amount) {
        Stack<Good> goods = new Stack<>();
        for (int i = 0; i < amount; i++) {
            goods.add(goodList.getGood(productId));
        }
        return goods;
    }

    public Good getGood(int productId) {
        return goodList.getGood(productId);
    }

    public int getGoodListSize() {
        return goodList.size();
    }

    public int getGoodListElementSize(int productId) {
        return goodList.elementSize(getProduct(productId));
    }

    public static GoodService getInstance(GoodList goodList, ProductService productService) {
        if (instance == null) {
            instance = new GoodService(goodList, productService);
        }
        return instance;
    }
}
