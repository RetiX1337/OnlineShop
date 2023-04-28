package com.company.core.models.user.customer;

import com.company.core.Shop;
import com.company.core.models.user.User;

import java.math.BigDecimal;

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
