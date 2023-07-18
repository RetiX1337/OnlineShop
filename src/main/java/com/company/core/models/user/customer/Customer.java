package com.company.core.models.user.customer;

import com.company.core.models.goods.Identifiable;
import com.company.core.models.user.User;

import java.math.BigDecimal;

public class Customer extends User implements Identifiable {
    private Long id;
    private final String username;
    private final String password;
    private final String email;
    private BigDecimal wallet;
    private final Cart cart;

    public Customer(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.wallet = BigDecimal.valueOf(500);
        this.cart = new Cart();
    }

    public Customer(Long id, String username, String password, String email, BigDecimal wallet) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
        this.cart = new Cart();
        this.wallet = wallet;
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

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
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
