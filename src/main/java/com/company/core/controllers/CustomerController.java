package com.company.core.controllers;

import com.company.core.models.EntityNotFoundException;
import com.company.core.models.user.customer.Customer;
import com.company.core.services.logicservices.CartService;
import com.company.core.services.logicservices.CustomerService;
import com.company.core.services.logicservices.ShopService;

import static com.company.Main.scan;

public class CustomerController {
    //TODO CONTROLLERS WILL BE REWORKED
    private final CustomerService customerService;
    private final ShopService shopService;
    private final CartService cartService;

    public CustomerController(CustomerService customerService, ShopService shopService, CartService cartService) {
        this.customerService = customerService;
        this.shopService = shopService;
        this.cartService = cartService;
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

    public void addToCart(Customer customer, Long shopId) {
        int productId;
        int quantity;
        displayProducts();
        System.out.println("Choose the product ID: ");
        productId = getInt();
        System.out.println("Enter the amount: ");
        quantity = getInt();
        try {
            if (cartService.addToCart(customer.getShoppingCart(), (long) productId, quantity, shopId)) {
                System.out.println("Added successfully!");
            } else {
                System.out.println("You've picked more than available on the storage!");
            }
        } catch (EntityNotFoundException e) {
            throw new RuntimeException(e);
        }

    }

    public void deleteFromCart(Customer customer) {

        int productId;
        int amount;
        displayCart(customer);
        System.out.println("Choose the product: ");
        productId = getInt();
        System.out.println("Enter the amount: ");
        amount = getInt();
        try {
            if (cartService.deleteFromCart(customer.getShoppingCart(), (long) productId, amount)){
                System.out.println("Deleted successfully");
            } else {
                System.out.println("This product doesn't exist");
            }
        } catch (EntityNotFoundException e) {
            throw new RuntimeException(e);
        }


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
