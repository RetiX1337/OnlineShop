package com.company.core.models;

import com.company.core.models.goods.Identifiable;

public class Storage implements Identifiable {
    private Long id;
    private String name;
    private String address;

    public Storage(String name, String address) {
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
