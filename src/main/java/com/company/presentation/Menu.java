package com.company.presentation;

import com.company.configuration.DependencyManager;
import com.company.core.models.user.customer.Customer;

import static com.company.Main.scan;

public class Menu {
    public static void menu(DependencyManager dependencyManager, Customer customer) {
        boolean menu = true;
        while (menu) {
            System.out.println("""
                    1. Put some items in cart
                    2. Buy the items from cart
                    3. Delete the items from cart
                    4. Display your orders
                    5. Display products
                    6. Display cart
                    7. Leave""");
            int input = Integer.parseInt(scan.nextLine());
            switch (input) {
                case 1 -> dependencyManager.getTestController().addToCart(customer, 1L);
                case 2 -> dependencyManager.getTestController().checkoutCart(customer, 1L);
                case 3 -> dependencyManager.getTestController().deleteFromCart(customer);
                case 4 -> dependencyManager.getTestController().displayOrders(customer);
                case 5 -> dependencyManager.getTestController().displayProducts(1L);
                case 6 -> dependencyManager.getTestController().displayCart(customer);
                case 7 -> {
                    System.out.println("Bye!");
                    menu = false;
                }
                default -> System.out.println("This case doesn't exist");
            }
        }
    }
}
