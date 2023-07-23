package com.company.core.models.goods;

import jakarta.persistence.*;
import org.hibernate.annotations.Any;

import java.math.BigDecimal;
import java.util.Objects;

@Entity
@Table(name = "item")
public class Item implements Identifiable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;
    @ManyToOne
    @JoinColumn(name = "product_id")
    private ProductBase product;
    @Column(name = "quantity")
    private Integer quantity;
    @Column(name = "price")
    private BigDecimal price;

    public Item(ProductBase product, Integer quantity) {
        this.price = BigDecimal.valueOf(0);
        this.product = product;
        this.quantity = quantity;
        countPrice();
    }

    public Item(Long id, ProductBase product, Integer quantity, BigDecimal price, Order order) {
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

    public ProductBase getProduct() {
        return product;
    }

    public void setProduct(ProductBase product) {
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
