package com.company.core.models;

import com.company.core.models.goods.Product;
import com.company.core.models.goods.ProductBase;
import com.company.core.models.goods.ProductType;

import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import java.math.BigDecimal;
@MappedSuperclass
public class ProductDecorator implements Product {
    @ManyToOne
    @JoinColumn(name = "product_id")
    protected ProductBase decoratedProduct;

    public ProductDecorator(ProductBase decoratedProduct) {
        this.decoratedProduct = decoratedProduct;
    }

    public ProductDecorator() {}

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

    public Product getProduct() {
        return decoratedProduct;
    }

    public void setProduct(ProductBase decoratedProduct) {
        this.decoratedProduct = decoratedProduct;
    }
}
