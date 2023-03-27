package com.company;

import static com.company.Main.scan;

public class Menu {
    public static void menu(Config config) {
        boolean menu = true;
        while (menu) {
            System.out.println("""
                    Do you want to put some goods in the cart?
                    1. Yes
                    2. No""");
            int input = Integer.parseInt(scan.nextLine());
            switch (input) {
                case 1 -> {
                    int itemId;
                    int amount;
                    config.getShop().getGoodList().printGoodList();
                    System.out.println("Choose the product: ");
                    itemId = Integer.parseInt(scan.nextLine());
                    System.out.println("Enter the amount: ");
                    amount = Integer.parseInt(scan.nextLine());
                    config.getShop().putInCart(config.getCustomer(), itemId, amount);
                }
                case 2 -> menu = false;
                default -> System.out.println("This case doesn't exist");
            }
            boolean innerMenu = true;
            while(innerMenu) {
                System.out.println("""
                        1. Keep on looking for goods
                        2. Buy the goods from the cart
                        3. Leave without buying""");
                int innerInput = Integer.parseInt(scan.nextLine());
                switch (innerInput) {
                    case 1 -> innerMenu = false;
                    case 2 -> {
                        if(!config.getCustomer().getShoppingCart().isEmpty()) {
                            config.getCustomer().buy();
                        } else {
                            System.out.println("Your cart is empty");
                        }
                    }
                    case 3 -> {
                        innerMenu = false;
                        menu = false;
                    }
                    default -> System.out.println("This case doesn't exist");
                }
            }
        }
    }
}
