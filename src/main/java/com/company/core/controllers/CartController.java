package com.company.core.controllers;

import com.company.core.models.goods.Item;
import com.company.core.models.goods.Product;
import com.company.core.models.user.User;
import com.company.core.models.user.customer.Cart;
import com.company.core.services.logicservices.CartService;
import com.company.core.services.logicservices.ProductService;

public class CartController {
    private final CartService cartService;
    private final ProductService productService;
    private final int PRODUCT_QUANTITY_CONSTANT = 1;

    public CartController(CartService cartService, ProductService productService) {
        this.cartService = cartService;
        this.productService = productService;
    }

    public Cart createUserCart(User user) {
        Cart cart = new Cart();
        cart.setUser(user);
        return cart;
    }

    public boolean addConstQuantityToCart(Cart cart, Long shopId, Long productId) {
        return cartService.addToCart(cart, productId, PRODUCT_QUANTITY_CONSTANT, shopId);
    }

    public boolean deleteConstQuantityFromCart(Cart cart, Long productId) {
        return cartService.deleteFromCart(cart, productId, PRODUCT_QUANTITY_CONSTANT);
    }

    public boolean checkoutCart(Cart cart, Long shopId) {
        return cartService.checkoutCart(cart, shopId);
    }


    public Integer getProductQuantity(Cart cart, Long productId) {
        Product product = productService.getProduct(productId);

        if (product == null) {
            return 0;
        }

        Item item = cart.getItem(product);
        if (item != null) {
            return item.getQuantity();
        } else {
            return 0;
        }
    }
}
