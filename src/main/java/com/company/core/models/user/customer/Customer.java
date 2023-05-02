package com.company.core.models.user.customer;

import com.company.core.Shop;
import com.company.core.models.goods.Identifiable;
import com.company.core.models.user.User;

import java.math.BigDecimal;

public class Customer extends User implements Identifiable {
    private Long id;
    private BigDecimal wallet;
    private final ShoppingCart shoppingCart;

    public Customer(String username, String password, ShoppingCart shoppingCart) {
        super(username, password);
        this.wallet = new BigDecimal(500);
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

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }
}
