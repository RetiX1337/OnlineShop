package com.company;

import com.company.configuration.Config;
import com.company.presentation.Menu;

import java.util.Scanner;

public class Main {
    public static final Scanner scan = new Scanner(System.in);

    public static void main(String[] args) {
        Config config = new Config();
        Menu.menu(config);
    }
}
