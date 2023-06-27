package com.company.core.models.user;

public abstract class User {
    final String username;
    final String password;
    final String email;

    protected User(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
    }

    protected User() {
        this.username = "default";
        this.password = null;
        this.email = null;
    }
}
