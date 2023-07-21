package com.company.core.models.goods;

import java.math.BigDecimal;
import java.util.Objects;

public class Item implements Identifiable {
    private Long id;
    private Order order;
    private Product product;
    private Integer quantity;
    private BigDecimal price;

    public Item(Product product, Integer quantity) {
        this.price = BigDecimal.valueOf(0);
        this.product = product;
        this.quantity = quantity;
        countPrice();
    }

    public Item(Long id, Product product, Integer quantity, BigDecimal price, Order order) {
        this.id = id;
        this.product = product;
        this.quantity = quantity;
        this.price = price;
        this.order = order;
    }

    public Item() {
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public void increaseQuantity(Integer quantity) {
        this.quantity += quantity;
        countPrice();
    }

    public void decreaseQuantity(Integer quantity) {
        this.quantity -= quantity;
        countPrice();
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
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
    public String toString() {
        return product.getBrand() + " " + product.getName() + ", Quantity: " + quantity + ", Price for one: " + product.getPrice() + ", Summary price: " + price;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Item item = (Item) o;
        return (item.product.equals(product) && item.quantity.equals(quantity) && item.price.equals(price));
    }

    @Override
    public int hashCode() {
        return Objects.hash(product, quantity, price);
    }

    private void countPrice() {
        price = product.getPrice().multiply(BigDecimal.valueOf(quantity));
    }
}
