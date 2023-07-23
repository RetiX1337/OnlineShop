package com.company.core.models.goods;

import com.company.core.models.ProductDecorator;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class ProductWithQuantity extends ProductDecorator {
    @Column(name = "quantity")
    private Integer quantity;

    public ProductWithQuantity(ProductBase decoratedProduct, Integer quantity) {
        super(decoratedProduct);
        this.quantity = quantity;
    }

    public ProductWithQuantity() {}

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
