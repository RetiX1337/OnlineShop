package com.company.core.services.impl;

import com.company.core.models.EntityNotFoundException;
import com.company.core.models.goods.Item;
import com.company.core.models.goods.Product;
import com.company.core.models.user.customer.Cart;
import com.company.core.services.ItemService;
import com.company.core.services.ProductService;
import com.company.core.services.CartService;

import java.math.BigDecimal;

public class CartServiceImpl implements CartService {
    private final ItemService itemService;
    private final ProductService productService;

    public CartServiceImpl(ItemService itemService, ProductService productService) {
        this.itemService = itemService;
        this.productService = productService;
    }

    @Override
    public boolean addToCart(Cart cart, Long productId, Integer quantity) throws EntityNotFoundException {
        if (containsProduct(cart, productService.getProduct(productId))) {
            if (true) {
//          if (notMoreThanAvailable(shoppingCart, itemService.createItem(productId, quantity))) {
                addOrUpdate(cart, productId, quantity);
                return true;
            } else {
                return false;
            }
        } else {
            addOrUpdate(cart, productId, quantity);
            return true;
        }
    }

    @Override
    public boolean deleteFromCart(Cart cart, Long productId, Integer quantity) throws EntityNotFoundException {
        if (containsProduct(cart, productService.getProduct(productId))) {
            itemService.deleteFromItem(cart.getItem(productService.getProduct(productId)), quantity);
            countPrice(cart);
            return true;
        }
        return false;
    }

    private void addToCartItem(Cart cart, Long productId, Integer quantity) throws EntityNotFoundException {
        itemService.addToItem(cart.getItem(productService.getProduct(productId)), quantity);
    }

    private void countPrice(Cart cart) {
        cart.setSummaryPrice(new BigDecimal(0));
        for (Item it : cart.getProductsFromCart()) {
            cart.setSummaryPrice(cart.getSummaryPrice().add(it.getPrice()));
        }
    }

    /*
    private boolean notMoreThanAvailable(ShoppingCart shoppingCart, Item item) {
        if (item == null) return false;
        Product product = item.getProduct();
        return item.getQuantity() - (shoppingCart.getItem(product).getQuantity() + item.getProduct().getQuantity()) >= 0;
    }
     */

    private boolean containsProduct(Cart cart, Product product) {
        return cart.getProductsFromCart().stream().anyMatch(item -> item.getProduct().getId().equals(product.getId()));
    }

    private void addOrUpdate(Cart cart, Long productId, Integer quantity) throws EntityNotFoundException {
        if (containsProduct(cart, productService.getProduct(productId))) {
            addToCartItem(cart, productId, quantity);
        } else {
            Item item = itemService.createItem(productId, quantity);
            cart.addItem(item);
            itemService.addItem(item);
        }
        countPrice(cart);
    }
}
