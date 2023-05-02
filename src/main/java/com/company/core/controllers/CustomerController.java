package com.company.core.controllers;

import com.company.core.models.user.customer.Customer;
import com.company.core.services.impl.CustomerServiceImpl;
import com.company.core.services.impl.ShopServiceImpl;

import java.math.BigDecimal;

import static com.company.Main.scan;

public class CustomerController {
    private final CustomerServiceImpl customerServiceImpl;
    private final ShopServiceImpl shopService;

    public CustomerController(CustomerServiceImpl customerServiceImpl, ShopServiceImpl shopService) {
        this.customerServiceImpl = customerServiceImpl;
        this.shopService = shopService;
    }

    /*
    public void register(String name, String password) {
        customerServiceImpl.addCustomer(customerServiceImpl.createCustomer(name, password));
    }

    public boolean login(String name, String password) {

    }
     */

    public void displayProducts() {
        System.out.println(shopService.showProducts());
    }

    public void addToCart(Customer customer) {
        int productId;
        int amount;
        displayProducts();
        System.out.println("Choose the product: ");
        productId = getInt();
        System.out.println("Enter the amount: ");
        amount = getInt();
        if (customerServiceImpl.addToCart(customer, (long) productId, amount)) {
            System.out.println("Added successfully!");
        } else {
            System.out.println("You've picked more than available on the storage!");
        }
    }

    public void checkoutCart(Customer customer) {
        if (shopService.checkout(customer)) {
            System.out.println("You have bought successfully!");
            System.out.println("Money left: " + customer.getWallet());
        } else {
            System.out.println("You don't have enough money to do that or your cart is empty");
        }
    }

    private int getInt() {
        return Integer.parseInt(scan.nextLine());
    }
}
