package com.company.core.models.goods;

import java.math.BigDecimal;
import java.util.Objects;

public class ProductBase implements Product {
    private Long id;
    private final String brand;
    private final String name;
    private final ProductType productType;
    private final BigDecimal price;

    public ProductBase(String brand, String name, ProductType productType, BigDecimal price) {
        this.brand = brand;
        this.name = name;
        this.productType = productType;
        this.price = price;
    }
    public ProductBase(Long id, String brand, String name, ProductType productType, BigDecimal price) {
        this.id = id;
        this.brand = brand;
        this.name = name;
        this.productType = productType;
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
    public ProductType getType() {
        return productType;
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
                " Type: " + productType +
                " Price: " + price;
    }


}
