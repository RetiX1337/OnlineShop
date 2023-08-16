package com.company.core.models;

import com.google.common.hash.Hashing;

import java.nio.charset.StandardCharsets;

public class UserAuthenticationDTO {
    private String username;
    private final String passwordHash;
    private final String email;

    //Registration
    public UserAuthenticationDTO(String username, String password, String email) {
        this.username = username;
        this.passwordHash = hashPassword(password);
        this.email = email;
    }

    //Login
    public UserAuthenticationDTO(String email, String password) {
        this.email = email;
        this.passwordHash = hashPassword(password);
    }

    private String hashPassword(String password) {
        return Hashing.sha256()
                .hashString(password, StandardCharsets.UTF_8)
                .toString();
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }
}
