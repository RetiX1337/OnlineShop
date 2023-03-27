package com.company.user.customer;

import com.company.Good;
import com.company.Shop;

import java.util.ArrayList;

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

    public void putInCart(Good good) {
        shoppingCart.add(good);
    }

    public boolean isEmpty() {
        return shoppingCart.isEmpty();
    }
}
