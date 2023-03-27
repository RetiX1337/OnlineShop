package com.company.user.customer;

import com.company.Good;
import com.company.Shop;
import com.company.user.User;

import java.util.ArrayList;

public class Customer extends User {
    private float wallet;
    private ShoppingCart shoppingCart;
    private Shop shop;

    public Customer(String username, String password, float wallet, ShoppingCart shoppingCart, Shop shop) {
        super(username, password);
        this.wallet = wallet;
        this.shoppingCart = shoppingCart;
        this.shop = shop;
    }

    public void setWallet(float wallet) {
        this.wallet = wallet;
    }

    public ShoppingCart getShoppingCart() {
        return shoppingCart;
    }

    public void buy() {
        if (!shoppingCart.isEmpty()) {
            float summaryPrice = 0;
            ArrayList<Good> goods = shoppingCart.getGoodsCart();
            for (int i = 0; i < goods.size(); i++) {
                summaryPrice += goods.get(i).getItem().getPrice();
            }
            System.out.println(summaryPrice);
            System.out.println(wallet);
            if(summaryPrice<=wallet) {
                wallet-=summaryPrice;
                System.out.println("Money left: " + wallet);
                shop.sell(this);
            } else {
                System.out.println("You don't have enough money to do that");
            }
        }
    }
}
