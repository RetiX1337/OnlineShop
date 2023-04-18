package com.company.core.controllers;

import com.company.core.models.goods.Good;
import com.company.core.models.goods.Product;
import com.company.core.services.impl.GoodListServiceImpl;

import java.util.Stack;

import static com.company.Main.scan;

public class GoodController {

    private GoodListServiceImpl goodListServiceImpl;

    public GoodController(GoodListServiceImpl goodListServiceImpl) {
        this.goodListServiceImpl = goodListServiceImpl;
    }

    public void fillGoodList() {
        boolean flag = true;
        while (flag) {
            System.out.println("1. Add a product" +
                    "\n2. Stop adding products");
            switch (getInt()) {
                case 1 -> {
                    int productId, quantity;
                    System.out.println("Enter product's ID you want to add: ");
                    productId = getInt();
                    System.out.println("How many products do you want to add? ");
                    quantity = getInt();
                    goodListServiceImpl.addToGoodList((long) productId, quantity);
                    printResult(quantity);
                }
                case 2 -> flag = false;
                default -> System.out.println("This case doesn't exist");
            }
        }
    }

    private void printResult(int quantity) {
        if (quantity == 1) {
            System.out.println("Product was added successfully!");
        } else {
            System.out.println("Products were added successfully!");
        }
    }

    public void printGoodList() {
        System.out.println("dam");
        System.out.println(goodListServiceImpl.getGoodListSize());
        for (int i = 0; i < goodListServiceImpl.getGoodListSize(); i++) {
            System.out.println(i + ". "
                    + getProduct(i).getBrand() + " "
                    + getProduct(i).getName() + ", "
                    + getProduct(i).getPrice()
                    + " (" + goodListServiceImpl.getGoodListElementSize((long) i) + ")");
        }
    }

    private int getInt() {
        return Integer.parseInt(scan.nextLine());
    }

    private Product getProduct(int productId) {
        return goodListServiceImpl.getProduct((long) productId);
    }
}
