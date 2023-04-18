package com.company.core.controllers;

import com.company.core.services.impl.ShoppingCartServiceImpl;

import static com.company.Main.scan;

public class ShoppingCartController {
    private final ShoppingCartServiceImpl shoppingCartServiceImpl;

    public ShoppingCartController(ShoppingCartServiceImpl shoppingCartServiceImpl) {
        this.shoppingCartServiceImpl = shoppingCartServiceImpl;
    }

    public void addToCart() {
        int productId;
        int amount;
        System.out.println("Choose the product: ");
        productId = getInt();
        System.out.println("Enter the amount: ");
        amount = getInt();
        shoppingCartServiceImpl.addToCart((long) productId, amount);
    }

    private int getInt() {
        return Integer.parseInt(scan.nextLine());
    }
}
