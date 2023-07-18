package com.company.core.models;

import com.company.core.models.goods.Product;
import com.company.core.models.goods.ProductType;

import java.math.BigDecimal;

public class ProductDecorator implements Product {
    protected Product decoratedProduct;

    public ProductDecorator(Product decoratedProduct) {
        this.decoratedProduct = decoratedProduct;
    }

    @Override
    public Long getId() {
        return decoratedProduct.getId();
    }

    @Override
    public void setId(Long id) {
        this.decoratedProduct.setId(id);
    }

    @Override
    public BigDecimal getPrice() {
        return decoratedProduct.getPrice();
    }

    @Override
    public String getBrand() {
        return decoratedProduct.getBrand();
    }

    @Override
    public String getName() {
        return decoratedProduct.getName();
    }

    @Override
    public ProductType getType() {
        return decoratedProduct.getType();
    }
}
