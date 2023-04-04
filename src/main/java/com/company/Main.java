package com.company;

import com.company.configuration.Config;
import com.company.core.shop.ShoppingCart;
import com.company.presentation.Menu;
import com.company.core.user.customer.Customer;

import java.math.BigDecimal;
import java.util.Scanner;

public class Main {
    public static final Scanner scan = new Scanner(System.in);

    public static void main(String[] args) {
        ShoppingCart shoppingCart = new ShoppingCart();
        Customer customer1 = new Customer("whyretski", "qwerty123", new BigDecimal(150), shoppingCart);
        Config config = new Config(customer1);
        Menu.menu(config);
    }
}
