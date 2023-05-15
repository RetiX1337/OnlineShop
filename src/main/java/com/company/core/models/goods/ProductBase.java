package com.company.core.models.goods;

import java.math.BigDecimal;
import java.util.Objects;

public class ProductBase implements Product, Identifiable {
    private Long id;
    private final String brand;
    private final String name;
    private final Type type;
    private final BigDecimal price;

    public ProductBase(String brand, String name, Type type, BigDecimal price) {
        this.brand = brand;
        this.name = name;
        this.type = type;
        this.price = price;
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
    public BigDecimal getPrice() {
        return price;
    }

    @Override
    public String getBrand() {
        return brand;
    }

    @Override
    public String getName() {
        return name;
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
                " Price: " + price;
    }
}
