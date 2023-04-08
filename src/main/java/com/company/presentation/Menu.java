package com.company.presentation;

import com.company.configuration.Config;

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
                    config.getGoodController().printGoodList();
                    config.getCustomerController().addToCart();
                }
                case 2 -> menu = false;
                default -> System.out.println("This case doesn't exist");
            }
            boolean innerMenu = true;
            while (innerMenu) {
                System.out.println("""
                        1. Keep on looking for goods
                        2. Buy the goods from the cart
                        3. Leave without buying""");
                int innerInput = Integer.parseInt(scan.nextLine());
                switch (innerInput) {
                    case 1 -> innerMenu = false;
                    case 2 -> {
                        if (!config.getCustomer().getShoppingCart().isEmpty()) {
                            if (config.getCustomerController().buy()) {
                                System.out.println("checkout");
                                config.getShopController().checkout(config.getCustomer());
                            }
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
            System.out.println(config.getShop().getOrderList());
        }
    }
}
