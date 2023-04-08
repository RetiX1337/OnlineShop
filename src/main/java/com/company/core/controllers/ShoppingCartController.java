package com.company.core.controllers;

import com.company.core.services.ShoppingCartService;

import static com.company.Main.scan;

public class ShoppingCartController {
    private final ShoppingCartService shoppingCartService;

    public ShoppingCartController(ShoppingCartService shoppingCartService) {
        this.shoppingCartService = shoppingCartService;
    }

    public void addToCart() {
        int productId;
        int amount;
        System.out.println("Choose the product: ");
        productId = getInt();
        System.out.println("Enter the amount: ");
        amount = getInt();
        shoppingCartService.addToCart(productId, amount);
    }

    private int getInt() {
        return Integer.parseInt(scan.nextLine());
    }
}
