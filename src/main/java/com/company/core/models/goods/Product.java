package com.company.core.models.goods;

import java.math.BigDecimal;
import java.util.Objects;

public class Product implements Identifiable {
    private Long id;
    private String brand;
    private String name;
    private ProductType productType;
    private BigDecimal price;

    public Product(String brand, String name, ProductType productType, BigDecimal price) {
        this.brand = brand;
        this.name = name;
        this.productType = productType;
        this.price = price;
    }
    public Product(Long id, String brand, String name, ProductType productType, BigDecimal price) {
        this.id = id;
        this.brand = brand;
        this.name = name;
        this.productType = productType;
        this.price = price;
    }

    public Product() {}

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ProductType getType() {
        return productType;
    }

    public void setProductType(ProductType productType) {
        this.productType = productType;
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
