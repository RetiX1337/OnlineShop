package com.company.core.models.goods;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.Properties;

public class Item implements Identifiable {
    private Long id;
    private Long orderId;
    private final ProductWithQuantity product;
    private Integer quantity;
    private BigDecimal price = BigDecimal.valueOf(0);

    public Item(ProductWithQuantity product, Integer quantity) {
        this.product = product;
        this.quantity = quantity;
        countPrice();
    }

    public Item(Long id, ProductWithQuantity product, Integer quantity, BigDecimal price, Long orderId) {
        this.id = id;
        this.product = product;
        this.quantity = quantity;
        this.price = price;
        this.orderId = orderId;
    }

    public void increaseQuantity(Integer quantity) {
        this.quantity += quantity;
        countPrice();
    }

    public void decreaseQuantity(Integer quantity) {
        this.quantity -= quantity;
        countPrice();
    }

    private void countPrice() {
        price = product.getPrice().multiply(BigDecimal.valueOf(quantity));
    }

    public BigDecimal getPrice() {
        return price;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    @Override
    public String toString() {
        return product.getBrand() + " " + product.getName() + ", Quantity: " + quantity + ", Price for one: " + product.getPrice() + ", Summary price: " + price;
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

    public ProductWithQuantity getProduct() {
        return product;
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
