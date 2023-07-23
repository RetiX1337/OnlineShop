package com.company;

import com.company.configuration.DependencyManager;
import com.company.core.exceptions.EntityNotFoundException;
import com.company.core.models.Shop;
import com.company.core.models.Storage;
import com.company.core.models.user.customer.Customer;

import java.util.Scanner;

public class Main {
    public static final Scanner scan = new Scanner(System.in);
    public static DependencyManager dependencyManager;

    public static void main(String[] args) {
        try {
            DependencyManager.setStrategy(1);
            DependencyManager.getInstance().getShopController().addStorage(1L, 4L);
            DependencyManager.getInstance().getShopController().findShop(1L).getStorages().forEach(storage -> System.out.println(storage.getName()));
        } catch (EntityNotFoundException e) {
            System.out.println("Not found");
        }
    }

    public static void initDependencyManager() {
        dependencyManager = DependencyManager.getInstance();
    }

}
