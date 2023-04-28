package com.company.core.models.goods;

import java.math.BigDecimal;
import java.util.Objects;

public class Product implements Identifiable {
    private Long id;
    private final String brand;
    private final String name;
    private final Type type;
    private final BigDecimal price;
    private Integer quantity;

    public Product(String brand, String name, Type type, BigDecimal price, Integer quantity) {
        this.brand = brand;
        this.name = name;
        this.type = type;
        this.price = price;
        this.quantity = quantity;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public void updateQuantity(Integer quantity) {
        this.quantity += quantity;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public String getBrand() {
        return brand;
    }

    public String getName() {
        return name;
    }

    public Type getType() {
        return type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return id.equals(product.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Product ID: " + id +
                " Brand: " + brand +
                " Name: " + name +
                " Type: " + type +
                " Price: " + price +
                " Quantity: " + quantity;
    }
}
