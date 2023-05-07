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
                case 1 -> dependencyManager.getCustomerController().addToCart(customer);
                case 2 -> dependencyManager.getCustomerController().checkoutCart(customer);
                case 3 -> dependencyManager.getCustomerController().deleteFromCart(customer);
                case 4 -> dependencyManager.getCustomerController().displayOrders(customer);
                case 5 -> dependencyManager.getCustomerController().displayProducts();
                case 6 -> dependencyManager.getCustomerController().displayCart(customer);
                case 7 -> {
                    System.out.println("Bye!");
                    menu = false;
                }
                default -> System.out.println("This case doesn't exist");
            }
        }
    }
}
