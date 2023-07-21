package com.company.core.models.goods;

import java.math.BigDecimal;

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
