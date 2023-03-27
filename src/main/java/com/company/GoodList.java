package com.company;

import java.util.*;

import static com.company.Main.scan;

public class GoodList {
    private final ItemList itemList;
    private final HashMap<Item, Stack<Good>> goodList = new HashMap<>();

    public GoodList(ItemList itemList) {
        this.itemList = itemList;
        for (int i = 0; i < itemList.getItemList().size(); i++) {
            goodList.put(itemList.getItemList().get(i), new Stack<>());
        }
    }

    public void fillGoodList() {
        boolean flag = true;
        System.out.println("1. Add a product" +
                "\n2. Stop adding products");
        while (flag) {
            switch (Integer.parseInt(scan.nextLine())) {
                case 1 -> {
                    int itemId, quantity;
                    itemList.printItemList();
                    System.out.println("Enter product's ID you want to add: ");
                    itemId = Integer.parseInt(scan.nextLine());
                    System.out.println("How many products do you want to add? ");
                    quantity = Integer.parseInt(scan.nextLine());
                    for (int i = 0; i < quantity; i++) {
                        createGood(itemId);
                    }
                    if (quantity == 1) {
                        System.out.println("Product was added successfully!");
                    } else {
                        System.out.println("Products were added successfully!");
                    }
                }
                case 2 -> flag = false;
                default -> System.out.println("This case doesn't exist");
            }
        }
    }

    public void printGoodList() {
        for (int i = 0; i < goodList.size(); i++) {
                System.out.println(i + ". "
                        + getItem(i).getBrand() + " "
                        + getItem(i).getName() + ", "
                        + getItem(i).getPrice()
                        + " (" + goodList.get(getItem(i)).size() + ")");
        }
    }

    public int size() {
        return goodList.size();
    }

    private void createGood(int itemId) {
        goodList.get(getItem(itemId)).push(new Good(getItem(itemId)));
    }

    public Stack<Good> getGoods(int itemId, int amount) {
        Stack<Good> goods = new Stack<>();
        for (int i = 0; i < amount; i++) {
            goods.add(goodList.get(getItem(itemId)).get(i));
        }
        return goods;
    }

    public Good getGood(int itemId) {
        if(!goodList.get(getItem(itemId)).isEmpty()) {
            return goodList.get(getItem(itemId)).peek();
        } else {
            return null;
        }
    }

    public Item getItem(int itemId) {
        return itemList.getItemList().get(itemId);
    }

    public void delete(Item item) {
        goodList.get(item).pop();
    }
}
