package com.company.core.models;

import com.company.core.models.goods.Identifiable;
import com.company.core.models.goods.Order;
import com.company.core.models.goods.Product;
import com.company.core.models.user.customer.Customer;
import com.company.core.services.persistenceservices.PersistenceInterface;

import java.util.ArrayList;
import java.util.List;

public class Shop implements Identifiable {
    private Long id;
    private String name;
    private String address;
    private final List<Long> storages;

    public Shop(Long id, String name, String address, List<Long> storages) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.storages = storages;
    }
    public Shop(String name, String address) {
        this.name = name;
        this.address = address;
        this.storages = new ArrayList<>();
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

    public List<Long> getStorages() {
        return storages.stream().toList();
    }

    public void addToStorages(Long storageId) {
        storages.add(storageId);
    }

    public void deleteFromStorages(Long storageId) {
        storages.remove(storageId);
    }
}
