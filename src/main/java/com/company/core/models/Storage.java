package com.company.core.models;

import com.company.core.models.goods.Identifiable;

import java.util.ArrayList;
import java.util.List;

public class Storage implements Identifiable {
    private Long id;
    private String name;
    private String address;
    private final List<Long> shops;

    public Storage(Long id, String name, String address, List<Long> shops) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.shops = shops;
    }

    public Storage(String name, String address) {
        this.name = name;
        this.address = address;
        this.shops = new ArrayList<>();
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
        return shops.stream().toList();
    }

    public void addToShops(Long shopId) {
        shops.add(shopId);
    }

    public void deleteFromShops(Long shopId) {
        shops.remove(shopId);
    }
}
