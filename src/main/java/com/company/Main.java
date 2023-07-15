package com.company;

import com.company.configuration.DependencyManager;
import com.company.core.models.EntityNotFoundException;
import com.company.core.models.Shop;
import com.company.core.models.user.customer.Customer;

import java.util.Scanner;

public class Main {
    public static final Scanner scan = new Scanner(System.in);
    public static Customer customer;
    public static Shop shop;
    public static DependencyManager dependencyManager;

    public static void main(String[] args) {
        try {
            DependencyManager.getInstance().getCustomerController().findCustomer(3L);
        } catch (EntityNotFoundException e) {
            System.out.println("Not found");
        }
    }

    public static void initDependencyManager() {
        dependencyManager = DependencyManager.getInstance();
    }
}
