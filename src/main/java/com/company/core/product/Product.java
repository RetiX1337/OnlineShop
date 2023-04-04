package com.company.core.product;

import java.math.BigDecimal;
import java.util.Objects;

public class Product {
    private final String productId;
    private final String brand;
    private final String name;
    private final Type type;
    private final BigDecimal price;

    public Product(String productId, String brand, String name, Type type, BigDecimal price) {
        this.productId = productId;
        this.brand = brand;
        this.name = name;
        this.type = type;
        this.price = price;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return productId.equals(product.productId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(productId);
    }

    @Override
    public String toString() {
        return "Product ID: " + productId +
                " Brand: " + brand +
                " Name: " + name +
                " Type: " + type +
                " Price: " + price;
    }
}
