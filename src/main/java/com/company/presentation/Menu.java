package com.company.presentation;

import com.company.configuration.DependencyManager;
import com.company.core.models.user.customer.Customer;

import static com.company.Main.scan;

public class Menu {
    public static void menu(DependencyManager dependencyManager, Customer customer) {
        boolean menu = true;
        while (menu) {
            System.out.println("""
                    Do you want to put some items in the cart?
                    1. Yes
                    2. No""");
            int input = Integer.parseInt(scan.nextLine());
            dependencyManager.getCustomerController().displayProducts();
            switch (input) {
                case 1 -> {
                    dependencyManager.getCustomerController().addToCart(customer);
                    dependencyManager.getCustomerController().deleteFromCart(customer);
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
                    case 1 ->  {
                        innerMenu = false;
                        menu = true;
                    }
                    case 2 -> {
                        dependencyManager.getCustomerController().checkoutCart(customer);
                    }
                    case 3 -> {
                        innerMenu = false;
                        menu = false;
                    }
                    default -> System.out.println("This case doesn't exist");
                }
            }
            dependencyManager.getCustomerController().displayOrders(customer);
        }
    }
}
