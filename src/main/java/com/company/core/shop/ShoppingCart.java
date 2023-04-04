package com.company.core.shop;

import com.company.core.good.Good;

import java.util.ArrayList;
import java.util.Stack;

public class ShoppingCart {
    private final Shop shop;
    private final ArrayList<Good> shoppingCart = new ArrayList<>();

    public ShoppingCart(Shop shop) {
        this.shop = shop;
    }

    public void printCart() {
        System.out.println(shoppingCart);
    }

    public ArrayList<Good> getGoodsCart() {
        return shoppingCart;
    }

    public void putInCart(int productId, int amount) {
        Stack<Good> goods = shop.getGoodList().getGoods(productId, amount);
        for (int i = 0; i < goods.size(); i++) {
            shoppingCart.add(goods.elementAt(i));
        }
    }

    public boolean isEmpty() {
        return shoppingCart.isEmpty();
    }
}
