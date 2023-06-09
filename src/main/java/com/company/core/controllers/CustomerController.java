package com.company.core.controllers;

import com.company.core.models.user.customer.Customer;
import com.company.core.services.CustomerService;
import com.company.core.services.ShopService;
import com.company.core.services.impl.CustomerServiceImpl;
import com.company.core.services.impl.ShopServiceImpl;

import static com.company.Main.scan;

public class CustomerController {
    //TODO CONTROLLERS WILL BE REMADE
    private final CustomerService customerService;
    private final ShopService shopService;

    public CustomerController(CustomerService customerService, ShopService shopService) {
        this.customerService = customerService;
        this.shopService = shopService;
    }

    public void displayProducts() {
        System.out.println(shopService.getProductsString());
    }

    public void displayOrders(Customer customer) {
        System.out.println(shopService.getCustomerOrdersString(customer));
    }

    public void displayCart(Customer customer) {
        System.out.println(customer.getShoppingCart());
    }

    public void addToCart(Customer customer) {/*
        int productId;
        int amount;
        displayProducts();
        System.out.println("Choose the product ID: ");
        productId = getInt();
        System.out.println("Enter the amount: ");
        amount = getInt();
        if (customerService.addToCart(customer, (long) productId, amount)) {
            System.out.println("Added successfully!");
        } else {
            System.out.println("You've picked more than available on the storage!");
        }
        */
    }

    public void deleteFromCart(Customer customer) {
        /*
        int productId;
        int amount;
        displayCart(customer);
        System.out.println("Choose the product: ");
        productId = getInt();
        System.out.println("Enter the amount: ");
        amount = getInt();
        if (customerService.deleteFromCart(customer, (long) productId, amount)){
            System.out.println("Deleted successfully");
        } else {
            System.out.println("This product doesn't exist");
        }

         */
    }

    public void checkoutCart(Customer customer) {
        displayCart(customer);
        if (shopService.checkoutCart(customer)) {
            System.out.println("Order successfully created.");
            System.out.println("Money left: " + customer.getWallet());
        } else {
            System.out.println("You don't have enough money to do that or your cart is empty");
        }
    }

    private int getInt() {
        return Integer.parseInt(scan.nextLine());
    }
}
