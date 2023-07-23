package com.company.core.controllers;

import com.company.core.exceptions.EntityNotFoundException;
import com.company.core.models.Shop;
import com.company.core.models.goods.Product;
import com.company.core.models.goods.ProductBase;
import com.company.core.models.user.customer.Customer;
import com.company.core.services.logicservices.*;

import java.util.List;

import static com.company.Main.scan;

public class TestController {
    private final CartService cartService;
    private final OrderService orderService;
    private final ProductService productService;
    private final CustomerService customerService;
    private final StorageService storageService;
    private final ShopService shopService;

    /*
                case 1 -> dependencyManager.getCustomerController().addToCart(customer);
                case 2 -> dependencyManager.getCustomerController().checkoutCart(customer);
                case 3 -> dependencyManager.getCustomerController().deleteFromCart(customer);
                case 4 -> dependencyManager.getCustomerController().displayOrders(customer);
                case 5 -> dependencyManager.getCustomerController().displayProducts();
                case 6 -> dependencyManager.getCustomerController().displayCart(customer);
     */

    public TestController(CartService cartService,
                          OrderService orderService,
                          ProductService productService,
                          CustomerService customerService,
                          StorageService storageService,
                          ShopService shopService) {
        this.cartService = cartService;
        this.orderService = orderService;
        this.productService = productService;
        this.storageService = storageService;
        this.customerService = customerService;
        this.shopService = shopService;
    }

    public String displayProducts(Long shopId) {
        List<ProductBase> products = productService.getAllProducts();

        String result = "";
        for (Product p : products) {
            Integer quantity = storageService.getQuantityPerShop(shopId, p.getId());
            result = result.concat(p + ", Quantity: " + quantity + "\n");
        }
        System.out.println(result);
        return result;
    }

    public void displayOrders(Customer customer) {
        System.out.println(orderService.findByCustomer(customer.getId()));
    }

    public void displayCart(Customer customer) {
        System.out.println(customer.getShoppingCart());
    }

    public void addToCart(Customer customer, Long shopId) {
        int productId;
        int quantity;
        displayProducts(shopId);
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
            System.out.println("This customer doesn't exist");
        }

    }

    public void deleteFromCart(Customer customer) {

        int productId;
        int quantity;
        displayCart(customer);
        System.out.println("Choose the product: ");
        productId = getInt();
        System.out.println("Enter the amount: ");
        quantity = getInt();
        try {
            if (cartService.deleteFromCart(customer.getShoppingCart(), (long) productId, quantity)) {
                System.out.println("Deleted successfully");
            } else {
                System.out.println("This product doesn't exist");
            }
        } catch (EntityNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public Customer findCustomer(Long customerId) {
        try {
            return customerService.findCustomer(customerId);
        } catch (EntityNotFoundException e) {
            System.out.println("This customer doesn't exist");
        }
        return null;
    }

    public Shop findShop(Long shopId) {
        try {
            return shopService.getShop(shopId);
        } catch (EntityNotFoundException e) {
            System.out.println("This shop doesn't exist");
        }
        return null;
    }

    public void checkoutCart(Customer customer, Long shopId) {
        displayCart(customer);
        if (cartService.checkoutCart(customer, shopId)) {
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
