package com.company.core.models.user;

import com.company.core.models.goods.Identifiable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class User implements Identifiable {
    private Long id;
    private String username;
    private String passwordHash;
    private String email;
    private Set<UserRole> roles = new HashSet<>();

    public User(String username, String password, String email, Set<UserRole> roles) {
        this.username = username;
        this.email = email;
        this.roles = roles;
    }

    public User() {

    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Set<UserRole> getRoles() {
        return Set.copyOf(roles);
    }

    public void setRoles(Set<UserRole> roles) {
        this.roles = roles;
    }

    public void addRole(UserRole userRole) {
        this.roles.add(userRole);
    }

    public boolean deleteRole(UserRole userRole) {
        return this.roles.remove(userRole);
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id) && Objects.equals(username, user.username) && Objects.equals(passwordHash, user.passwordHash) && Objects.equals(email, user.email) && Objects.equals(roles, user.roles);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username, passwordHash, email, roles);
    }
}
