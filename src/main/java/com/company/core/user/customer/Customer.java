package com.company.core.user.customer;

import com.company.core.good.Good;
import com.company.core.product.Product;
import com.company.core.shop.Shop;
import com.company.core.user.User;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Stack;

public class Customer extends User {
    private BigDecimal wallet;
    private final ShoppingCart shoppingCart;
    private final Shop shop;

    public Customer(String username, String password, BigDecimal wallet, ShoppingCart shoppingCart, Shop shop) {
        super(username, password);
        this.wallet = wallet;
        this.shoppingCart = shoppingCart;
        this.shop = shop;
    }

    public void setWallet(BigDecimal wallet) {
        this.wallet = wallet;
    }

    public ShoppingCart getShoppingCart() {
        return shoppingCart;
    }

    public void buy() {
        if (!shoppingCart.isEmpty()) {
            BigDecimal summaryPrice = new BigDecimal(0);
            HashMap<Product, Stack<Good>> goods = shoppingCart.getGoodsCart();
            for (Stack<Good> goodStack : goods.values()) {
                for (Good good : goodStack) {
                    summaryPrice = summaryPrice.add(good.getProduct().getPrice());
                }
            }
            System.out.println(summaryPrice);
            System.out.println(wallet);
            if (summaryPrice.compareTo(wallet) == 0 || summaryPrice.compareTo(wallet) == -1) {
                wallet = wallet.subtract(summaryPrice);
                System.out.println("Money left: " + wallet);
                shop.checkout(this);
                shoppingCart.getGoodsCart().clear();
            } else {
                System.out.println("You don't have enough money to do that");
            }
        }
    }
}
