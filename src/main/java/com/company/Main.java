package com.company;

import com.company.configuration.DependencyManager;
import com.company.core.models.user.customer.Customer;
import com.company.presentation.Menu;

import java.util.Scanner;

public class Main {
    public static final Scanner scan = new Scanner(System.in);
    public static Customer customer;

    public static void main(String[] args) {
        DependencyManager dependencyManager = new DependencyManager();

        customer = dependencyManager.testCustomerCreateMethod();

        PreFiller.fillProductList(dependencyManager.getProductController());
        Menu.menu(dependencyManager, customer);
    }
}
