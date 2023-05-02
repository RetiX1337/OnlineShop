package com.company;

import com.company.configuration.DependencyManager;
import com.company.core.models.user.customer.Customer;
import com.company.core.models.user.customer.ShoppingCart;
import com.company.presentation.Menu;

import java.util.Scanner;

public class Main {
    public static final Scanner scan = new Scanner(System.in);
    public static Customer customer;

    public static void main(String[] args) {
        DependencyManager dependencyManager = new DependencyManager();

        customer = new Customer("whyretski", "123456", new ShoppingCart());

        PreFiller.fillProductList(dependencyManager.getProductController());
        dependencyManager.getProductController().printProductList();
        Menu.menu(dependencyManager, customer);
    }
}
