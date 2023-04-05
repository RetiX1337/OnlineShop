package com.company.core.user.customer;

import com.company.core.good.Good;
import com.company.core.good.GoodList;
import com.company.core.product.Product;

import java.util.HashMap;
import java.util.Stack;

public class ShoppingCart {
    private final HashMap<Product, Stack<Good>> shoppingCart = new HashMap<>();

    public void printCart() {
        System.out.println(shoppingCart);
    }

    public HashMap<Product, Stack<Good>> getGoodsCart() {
        return shoppingCart;
    }

    public void addToCart(GoodList goodList, int productId, int amount) {
        for (int i = 0; i < goodList; i++) {
            if (availableOnStorage(goods)) {
            //    if (shoppingCart.containsKey(shop.getGoodList().getProduct(productId))) {
                    shoppingCart.put(shop.getGoodList().getProduct(productId), goods);
                    shoppingCart.put()
            //    }
            } else {
                System.out.println("You've picked more than available on the storage");
            }
        }
    }

    private boolean availableOnStorage(Stack<Good> goods) {
        return shoppingCart.containsKey(goods.peek().getProduct()) & shoppingCart.get(goods.peek().getProduct()).size() - goods.size() <= 0;
    }

    public boolean isEmpty() {
        return shoppingCart.isEmpty();
    }
}
