package com.company.core.controllers;

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


    public boolean addToCart(Customer customer, Long shopId, Long productId) {
        return cartService.addToCart(customer.getShoppingCart(), productId, 1, shopId);
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
        return List.copyOf(customer.getShoppingCart().getCartItems());
    }

    public Integer getProductQuantity(Customer customer, Long productId) {
        Product product = productService.getProduct(productId);

        if (product == null) {
            return 0;
        }

        Item item = customer.getShoppingCart().getItem(product);
        if (item != null) {
            return item.getQuantity();
        } else {
            return 0;
        }
    }
}
