package com.company.core.models.user.customer;

import com.company.core.models.goods.Identifiable;
import com.company.core.models.user.User;
import javax.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "customer")
public class Customer extends User implements Identifiable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "username")
    private String username;
    @Column(name = "password")
    private String password;
    @Column(name = "email")
    private String email;
    @Column(name = "wallet")
    private BigDecimal wallet;
    @Transient
    private Cart cart = new Cart();

    public Customer(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.wallet = BigDecimal.valueOf(500);
    }

    public Customer(Long id, String username, String password, String email, BigDecimal wallet) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
        this.wallet = wallet;
    }

    public Customer() {
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

    public void setCart(Cart cart) {
        this.cart = cart;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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
