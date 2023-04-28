package com.company.presentation;

import com.company.configuration.DependencyManager;

import static com.company.Main.scan;

public class Menu {
    public static void menu(DependencyManager dependencyManager) {
        boolean menu = true;
        while (menu) {
            System.out.println("""
                    Do you want to put some items in the cart?
                    1. Yes
                    2. No""");
            int input = Integer.parseInt(scan.nextLine());
            switch (input) {
                case 1 -> {
                    dependencyManager.getProductController().printProductList();
                    dependencyManager.getCustomerController().addToCart();
                }
                case 2 -> menu = false;
                default -> System.out.println("This case doesn't exist");
            }
            boolean innerMenu = true;
            while (innerMenu) {
                System.out.println("""
                        1. Keep on looking for items
                        2. Buy the items from the cart
                        3. Leave without buying""");
                int innerInput = Integer.parseInt(scan.nextLine());
                switch (innerInput) {
                    case 1 -> innerMenu = false;
                    case 2 -> {
                        dependencyManager.getShopController().checkout(dependencyManager.getCustomer());
                    }
                    case 3 -> {
                        innerMenu = false;
                        menu = false;
                    }
                    default -> System.out.println("This case doesn't exist");
                }
            }
            System.out.println(dependencyManager.getShop().getOrderList());
        }
    }
}
