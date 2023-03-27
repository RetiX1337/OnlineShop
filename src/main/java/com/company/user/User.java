package com.company.user;

public abstract class User {
    final String username;
    final String password;

    protected User(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
