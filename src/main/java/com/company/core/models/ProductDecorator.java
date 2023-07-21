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
        decoratedProduct.setId(id);
    }

    @Override
    public BigDecimal getPrice() {
        return decoratedProduct.getPrice();
    }

    @Override
    public void setPrice(BigDecimal price) {
        decoratedProduct.setPrice(price);
    }

    @Override
    public String getBrand() {
        return decoratedProduct.getBrand();
    }

    @Override
    public void setBrand(String brand) {
        decoratedProduct.setBrand(brand);
    }

    @Override
    public String getName() {
        return decoratedProduct.getName();
    }

    @Override
    public void setName(String name) {
        decoratedProduct.setName(name);
    }

    @Override
    public ProductType getType() {
        return decoratedProduct.getType();
    }

    @Override
    public void setProductType(ProductType productType) {
        decoratedProduct.setProductType(productType);
    }
}
