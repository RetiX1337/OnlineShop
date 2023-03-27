package com.company;

import java.util.HashMap;

public class ItemList {
    private final HashMap<Integer, Item> itemList = new HashMap<>();

    public void fillItemList() {
        itemList.put(0, new Item("ovs1", "Super Platki", "Ovsianka", Type.FOOD, 4.99f));
        itemList.put(1, new Item("ovs2", "Platki Gorskie", "Ovsianka Dziwna", Type.FOOD, 5.49f));
        itemList.put(2, new Item("pivo1", "Desperados", "Desperados Mochito", Type.ALCOHOL, 6.49f));
        itemList.put(3, new Item("toipap1", "Miekka dupa", "Rumianek", Type.HOUSEHOLD, 10.99f));
        itemList.put(4, new Item("cola1", "Coca-Cola", "Coca-Cola", Type.BEVERAGE, 4.99f));
    }

    public void printItemList() {
        for (int i = 0; i < itemList.size(); i++) {
            System.out.println(i + ". " + itemList.get(i));
        }
    }

    public HashMap<Integer, Item> getItemList() {
        return itemList;
    }
}
