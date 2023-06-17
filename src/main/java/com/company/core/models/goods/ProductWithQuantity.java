package com.company.core.models.goods;

import com.company.core.models.ProductDecorator;

import java.math.BigDecimal;

public class ProductWithQuantity extends ProductDecorator {
    private Integer quantity;

    public ProductWithQuantity(Product decoratedProduct, Integer quantity) {
        super(decoratedProduct);
        this.quantity = quantity;
    }

    @Override
    public String getName() {
        return decoratedProduct.getName();
    }

    @Override
    public BigDecimal getPrice() {
        return decoratedProduct.getPrice();
    }

    @Override
    public String getBrand() {
        return super.getBrand();
    }

    @Override
    public Long getId() {
        return super.getId();
    }

    @Override
    public void setId(Long id) {
        super.setId(id);
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Integer getQuantity() {
        return quantity;
    }

    @Override
    public String toString() {
        return decoratedProduct +
                " Quantity: " + quantity;
    }
}
