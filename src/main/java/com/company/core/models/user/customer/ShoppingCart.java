package com.company.core.models.user.customer;

import com.company.core.models.goods.Good;
import com.company.core.models.goods.Product;

import java.util.Collection;
import java.util.HashMap;
import java.util.Stack;

public class ShoppingCart {
    private final HashMap<Product, Stack<Good>> shoppingCart = new HashMap<>();

    public HashMap<Product, Stack<Good>> getGoodsCart() {
        return shoppingCart;
    }

    public void updateCart(Product product, Good good) {
        System.out.println("push that");
        shoppingCart.get(product).push(good);
    }

    public void addToCart(Product product, Stack<Good> goods) {
        shoppingCart.put(product, goods);
    }

    public Stack<Good> getCartElement(Product product) {
        return shoppingCart.get(product);
    }

    public boolean isEmpty() {
        return shoppingCart.isEmpty();
    }
}
