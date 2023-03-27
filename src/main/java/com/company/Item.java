package com.company;

import java.util.Objects;

public class Item {
    private String itemNumber;
    private String brand;
    private String name;
    private Type type;
    private float price;

    public Item(String itemNumber, String brand, String name, Type type, float price) {
        this.itemNumber = itemNumber;
        this.brand = brand;
        this.name = name;
        this.type = type;
        this.price = price;
    }

    public float getPrice() {
        return price;
    }

    public String getBrand() {
        return brand;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Item item = (Item) o;
        return itemNumber.equals(item.itemNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(itemNumber);
    }

    @Override
    public String toString() {
        return "Item Number: " + itemNumber +
                " Brand: " + brand +
                " Name: " + name +
                " Type: " + type +
                " Price: " + price;
    }
}
