package com.company;

import java.util.Objects;

public class Item {
    private final String itemNumber;
    private final String brand;
    private final String name;
    private final Type type;
    private final float price;

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
