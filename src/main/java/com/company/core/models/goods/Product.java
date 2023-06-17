package com.company.core.models.goods;

import java.math.BigDecimal;

public interface Product extends Identifiable {
    BigDecimal getPrice();
    String getBrand();
    String getName();
    ProductType getType();
}
