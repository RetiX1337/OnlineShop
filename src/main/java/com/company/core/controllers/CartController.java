package com.company.core.controllers;

import com.company.core.models.EntityNotFoundException;
import com.company.core.models.goods.Item;
import com.company.core.models.goods.Product;
import com.company.core.models.user.customer.Customer;
import com.company.core.services.logicservices.CartService;
import com.company.core.services.logicservices.ProductService;

import java.util.Collection;
import java.util.List;

public class CartController {
    private final CartService cartService;
    private final ProductService productService;

    public CartController(CartService cartService, ProductService productService) {
        this.cartService = cartService;
        this.productService = productService;
    }

    public void addToCart(Customer customer, Long shopId, Long productId, Integer quantity) {
        try {
            if (cartService.addToCart(customer.getShoppingCart(), productId, quantity, shopId)) {
                System.out.println("Added successfully!");
            } else {
                System.out.println("You've picked more than available on the storage!");
            }
        } catch (EntityNotFoundException e) {
            System.out.println("This customer doesn't exist");
        }
    }

    public boolean addToCart(Customer customer, Long shopId, Long productId) {
        try {
            if (cartService.addToCart(customer.getShoppingCart(), productId, 1, shopId)) {
                return true;
            } else {
                return false;
            }
        } catch (EntityNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void deleteFromCart(Customer customer, Long productId, Integer quantity) {
        try {
            if (cartService.deleteFromCart(customer.getShoppingCart(), productId, quantity)) {
                System.out.println("Deleted successfully");
            } else {
                System.out.println("This product doesn't exist");
            }
        } catch (EntityNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean deleteFromCart(Customer customer, Long productId) {
        try {
            if (cartService.deleteFromCart(customer.getShoppingCart(), productId, 1)) {
                return true;
            } else {
                return false;
            }
        } catch (EntityNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean checkoutCart(Customer customer, Long shopId) {
        if (cartService.checkoutCart(customer, shopId)) {
            return true;
        } else {
            return false;
        }
    }

    public void deleteItem(Customer customer, Item item) {
        customer.getShoppingCart().deleteItem(item);
    }

    public Collection<Item> displayCart(Customer customer) {
        return List.copyOf(customer.getShoppingCart().getProductsFromCart());
    }

    public Integer getProductQuantity(Customer customer, Long productId) {
        try {
            Product product = productService.getProduct(productId);
            Item item = customer.getShoppingCart().getItem(product);
            if (item!=null) {
                return customer.getShoppingCart().getItem(productService.getProduct(productId)).getQuantity();
            } else {
                return 0;
            }
        } catch (EntityNotFoundException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
