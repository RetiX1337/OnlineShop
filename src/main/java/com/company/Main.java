package com.company;

import com.company.configuration.DependencyManager;
import com.company.presentation.Menu;

import java.util.Scanner;

public class Main {
    public static final Scanner scan = new Scanner(System.in);

    public static void main(String[] args) {
        DependencyManager dependencyManager = new DependencyManager();
        PreFiller.fillProductList(dependencyManager.getProductController());
        dependencyManager.getProductController().printProductList();
        dependencyManager.getGoodController().fillGoodList();
        Menu.menu(dependencyManager);
    }
}
