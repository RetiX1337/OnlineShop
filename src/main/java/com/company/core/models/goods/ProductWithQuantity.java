package com.company.core.models.goods;

public class ProductWithQuantity {
    private Integer quantity;
    private final Product product;

    public ProductWithQuantity(Product product, Integer quantity) {
        this.product = product;
        this.quantity = quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public Product getProduct() {
        return product;
    }

    @Override
    public String toString() {
        return product +
                " Quantity: " + quantity;
    }
}
