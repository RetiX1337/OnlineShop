package com.company.core.models.goods;

import com.company.core.models.ProductDecorator;

public class ProductWithQuantity extends ProductDecorator {
    private Integer quantity;

    public ProductWithQuantity(Product decoratedProduct, Integer quantity) {
        super(decoratedProduct);
        this.quantity = quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public Product getProduct() {
        return decoratedProduct;
    }

    @Override
    public String toString() {
        return decoratedProduct +
                " Quantity: " + quantity;
    }
}
