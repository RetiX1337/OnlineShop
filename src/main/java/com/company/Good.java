package com.company;

import java.util.Objects;

public class Good {
    private Item item;
    private static int idCounter = 0;
    private int id;

    public Good(Item item) {
        this.item = item;
        this.id = idCounter++;
    }

    @Override
    public String toString() {
        return item.getBrand() + " " + item.getName() + ", " + item.getPrice();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Good good = (Good) o;
        return item.equals(good.item);
    }

    @Override
    public int hashCode() {
        return Objects.hash(item);
    }

    public Item getItem() {
        return item;
    }
}
