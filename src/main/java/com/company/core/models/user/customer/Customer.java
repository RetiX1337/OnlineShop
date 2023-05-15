package com.company.core.models.user.customer;

import com.company.core.models.goods.Identifiable;
import com.company.core.models.user.User;

import java.math.BigDecimal;

public class Customer extends User implements Identifiable {
    private Long id;
    private final String username;
    private final String password;
    private BigDecimal wallet;
    private final Cart cart;

    public Customer(String username, String password, Cart cart) {
        super(username, password);
        this.username = username;
        this.password = password;
        this.wallet = new BigDecimal(500);
        this.cart = cart;
    }

    public BigDecimal getWallet() {
        return wallet;
    }

    public void setWallet(BigDecimal wallet) {
        this.wallet = wallet;
    }

    public Cart getShoppingCart() {
        return cart;
    }

    public String getUsername() {
        return username;
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
