package com.company;

import com.company.configuration.DependencyManager;
import com.company.core.models.Shop;
import com.company.core.models.user.customer.Customer;
import com.company.presentation.Menu;

import java.util.Scanner;

public class Main {
    public static final Scanner scan = new Scanner(System.in);
    public static Customer customer;
    public static Shop shop;
    public static DependencyManager dependencyManager;

    public static void main(String[] args) {
        dependencyManager = DependencyManager.getInstance();
        customer = dependencyManager.testCustomerGetMethod();
    //    PreFiller.fillProductList(dependencyManager.getProductController());
        Menu.menu(dependencyManager, customer);
    }

    public static void initDependencyManager() {
        dependencyManager = DependencyManager.getInstance();
    }
}
