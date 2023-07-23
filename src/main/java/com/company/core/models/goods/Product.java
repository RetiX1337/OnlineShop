package com.company.core.models.goods;

import javax.persistence.*;

import java.math.BigDecimal;

@MappedSuperclass
public interface Product extends Identifiable {
    BigDecimal getPrice();

    void setPrice(BigDecimal price);

    String getBrand();

    void setBrand(String brand);

    String getName();

    void setName(String name);

    ProductType getType();

    void setProductType(ProductType productType);
}
