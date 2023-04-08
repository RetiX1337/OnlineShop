package com.company.core.models.goods;

import java.util.Objects;

public class Good {
    private final Product product;

    public Good(Product product) {
        this.product = product;
    }

    @Override
    public String toString() {
        return product.getBrand() + " " + product.getName() + ", " + product.getPrice();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Good good = (Good) o;
        return product.equals(good.product);
    }

    @Override
    public int hashCode() {
        return Objects.hash(product);
    }

    public Product getProduct() {
        return product;
    }
}
