package com.company;

import com.company.user.customer.Customer;
import com.company.user.customer.ShoppingCart;

import java.util.Scanner;

public class Main {
    public static final Scanner scan = new Scanner(System.in);

    public static void main(String[] args) {
        ItemList itemList = new ItemList();
        itemList.fillItemList();
        GoodList goodList = new GoodList(itemList);
        goodList.fillGoodList();
        Shop shop = new Shop(itemList, goodList);
        ShoppingCart shoppingCart = new ShoppingCart(shop);
        Customer customer1 = new Customer("whyretski", "qwerty123", 150.0f, shoppingCart, shop);
        Config config = new Config(shop, customer1);
        Menu.menu(config);
    }
}
