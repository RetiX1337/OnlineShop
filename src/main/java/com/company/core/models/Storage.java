package com.company.core.models;

import com.company.core.models.goods.Identifiable;
import com.company.core.models.goods.ProductWithQuantity;

import java.util.*;

public class Storage implements Identifiable {
    private Long id;
    private String name;
    private String address;
    private Set<ProductWithQuantity> productQuantities;
    public Storage(Long id, String name, String address, Set<ProductWithQuantity> productQuantities) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.productQuantities = productQuantities;
    }

    public Storage(String name, String address) {
        this.name = name;
        this.address = address;
        this.productQuantities = new HashSet<>();
    }

    public Storage() {}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Set<ProductWithQuantity> getProductQuantities() {
        return productQuantities;
    }

    public void setProductQuantities(Set<ProductWithQuantity> productQuantities) {
        this.productQuantities = productQuantities;
    }

    public void updateQuantity(Long productId, Integer quantity) {
        productQuantities.stream()
                .filter(product -> product.getProduct().getId().equals(productId))
                .findFirst()
                .ifPresent(productWithQuantity -> productWithQuantity.setQuantity(quantity));
    }

    public void addQuantity(ProductWithQuantity productWithQuantity) {
        productQuantities.add(productWithQuantity);
    }

    public ProductWithQuantity getProductWithQuantity(Long productId) {
        return productQuantities.stream()
                .filter(product -> product.getProduct().getId().equals(productId))
                .findFirst()
                .orElse(null);
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }
}
