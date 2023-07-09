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
        return cartService.addToCart(customer.getShoppingCart(), productId, 1, shopId);
    }

    public void deleteFromCart(Customer customer, Long productId, Integer quantity) {
        if (cartService.deleteFromCart(customer.getShoppingCart(), productId, quantity)) {
            System.out.println("Deleted successfully");
        } else {
            System.out.println("This product doesn't exist");
        }
    }

    public boolean deleteFromCart(Customer customer, Long productId) {
        return cartService.deleteFromCart(customer.getShoppingCart(), productId, 1);
    }

    public boolean checkoutCart(Customer customer, Long shopId) {
        return cartService.checkoutCart(customer, shopId);
    }

    public void deleteItem(Customer customer, Item item) {
        customer.getShoppingCart().deleteItem(item);
    }

    public Collection<Item> displayCart(Customer customer) {
        return List.copyOf(customer.getShoppingCart().getProductsFromCart());
    }

    public Integer getProductQuantity(Customer customer, Long productId) {
        Product product = productService.getProduct(productId);
        Item item = customer.getShoppingCart().getItem(product);
        if (item != null) {
            return customer.getShoppingCart().getItem(productService.getProduct(productId)).getQuantity();
        } else {
            return 0;
        }
    }
}
