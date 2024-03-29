package com.company.core.models;

import com.company.core.models.goods.Identifiable;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Shop implements Identifiable {
    private Long id;
    private String name;
    private String address;
    private Set<Storage> storages;

    public Shop(Long id, String name, String address, Set<Storage> storages) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.storages = storages;
    }
    public Shop(String name, String address) {
        this.name = name;
        this.address = address;
        this.storages = new HashSet<>();
    }

    public Shop() {}

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

    public Set<Storage> getStorages() {
        return new HashSet<>(storages);
    }

    public void setStorages(Set<Storage> storages) {
        this.storages = storages;
    }

    public void addStorage(Storage storage) {
        storages.add(storage);
    }

    public void deleteStorage(Storage storage) {
        storages.remove(storage);
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
