package com.company.core.models.goods;

import java.math.BigDecimal;
import java.util.Objects;

public class Item {
    private final Product product;
    private Integer quantity;
    private BigDecimal price = new BigDecimal(0);

    public Item(Product product, Integer quantity) {
        this.product = product;
        this.quantity = quantity;
        countPrice();
    }

    public void update(Integer quantity) {
        this.quantity = quantity;
        countPrice();
    }

    private void countPrice() {
        System.out.println(quantity);
        System.out.println(product.getPrice());
        price = product.getPrice().multiply(BigDecimal.valueOf(quantity));
        System.out.println(price);
    }

    public BigDecimal getPrice() {
        return price;
    }

    public Integer getQuantity() {
        return quantity;
    }

    @Override
    public String toString() {
        return product.getBrand() + " " + product.getName() + ", " + product.getPrice();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Item item = (Item) o;
        return product.equals(item.product);
    }

    @Override
    public int hashCode() {
        return Objects.hash(product);
    }

    public Product getProduct() {
        return product;
    }
}
