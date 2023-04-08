package com.company.core.models.user.customer;

import com.company.core.models.goods.Good;
import com.company.core.models.goods.Product;
import com.company.core.Shop;
import com.company.core.models.user.User;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Stack;

public class Customer extends User {
    private BigDecimal wallet;
    private final ShoppingCart shoppingCart;

    public Customer(String username, String password, BigDecimal wallet, ShoppingCart shoppingCart, Shop shop) {
        super(username, password);
        this.wallet = wallet;
        this.shoppingCart = shoppingCart;
    }

    public BigDecimal getWallet() {
        return wallet;
    }

    public void setWallet(BigDecimal wallet) {
        this.wallet = wallet;
    }

    public ShoppingCart getShoppingCart() {
        return shoppingCart;
    }
}
