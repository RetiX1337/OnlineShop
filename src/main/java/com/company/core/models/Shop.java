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

    public Shop(Long id, String name, String address) {
        this.id = id;
        this.name = name;
        this.address = address;
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
