package com.company.core.models.user;

import com.company.core.models.user.customer.Cart;

import java.math.BigDecimal;

public abstract class User {
    private String username;
    private String password;
    private String email;

    public User(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
    }

    public User() {
    }
}
