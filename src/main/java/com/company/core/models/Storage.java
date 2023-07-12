package com.company.core.models;

import com.company.core.models.goods.Identifiable;
import com.company.core.models.goods.Product;
import com.company.core.models.goods.ProductWithQuantity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Storage implements Identifiable {
    private Long id;
    private String name;
    private String address;
    private final List<Long> shops;
    private final HashMap<Long, ProductWithQuantity> productQuantities;
    public Storage(Long id, String name, String address, List<Long> shops, HashMap<Long, ProductWithQuantity> productQuantities) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.shops = shops;
        this.productQuantities = productQuantities;
    }

    public Storage(String name, String address) {
        this.name = name;
        this.address = address;
        this.shops = new ArrayList<>();
        this.productQuantities = new HashMap<>();
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public List<Long> getShops() {
        return new ArrayList<>(shops);
    }

    public void addShop(Long shopId) {
        shops.add(shopId);
    }

    public void deleteShop(Long shopId) {
        shops.remove(shopId);
    }

    public HashMap<Long, ProductWithQuantity> getProductQuantities() {
        return productQuantities;
    }

    public void updateQuantity(Long productId, Integer quantity) {
        productQuantities.get(productId).setQuantity(quantity);
    }
}
