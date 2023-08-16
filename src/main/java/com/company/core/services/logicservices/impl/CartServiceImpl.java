package com.company.core.services.logicservices.impl;

import com.company.core.models.goods.Item;
import com.company.core.models.goods.Order;
import com.company.core.models.goods.Product;
import com.company.core.models.user.customer.Cart;
import com.company.core.services.logicservices.ItemService;
import com.company.core.services.logicservices.ProductService;
import com.company.core.services.logicservices.OrderService;
import com.company.core.services.logicservices.StorageService;
import com.company.core.services.logicservices.CartService;

import java.math.BigDecimal;

public class CartServiceImpl implements CartService {
    private final ItemService itemService;
    private final ProductService productService;
    private final OrderService orderService;
    private final StorageService storageService;

    public CartServiceImpl(ItemService itemService, ProductService productService, OrderService orderService, StorageService storageService) {
        this.itemService = itemService;
        this.productService = productService;
        this.orderService = orderService;
        this.storageService = storageService;
    }

    @Override
    public boolean addToCart(Cart cart, Long productId, Integer quantity, Long shopId) {
        if (storageService.getQuantityPerShop(shopId, productId) < quantity) {
            return false;
        }

        Product product = productService.getProduct(productId);

        if (product == null) {
            return false;
        }

        if (containsProduct(cart, product)) {
            if (notMoreThanAvailable(cart, product, quantity, shopId)) {
                addToExistingItem(cart, product, quantity);
            } else {
                return false;
            }
        } else {
            addNewItem(cart, productId, quantity);
        }

        countPrice(cart);
        return true;
    }

    @Override
    public boolean deleteFromCart(Cart cart, Long productId, Integer quantity) {
        Product product = productService.getProduct(productId);

        if (product == null) {
            return false;
        }

        if (!containsProduct(cart, product)) {
            return false;
        }

        Item item = cart.getItem(product);

        if (item.getQuantity().equals(quantity)) {
            cart.deleteItem(item);
        } else {
            item.decreaseQuantity(quantity);
        }

        countPrice(cart);
        return true;
    }

    @Override
    public boolean checkoutCart(Cart cart, Long shopId) {
        if (cart.isEmpty()) {
            return false;
        }

        Order order = orderService.createOrder(cart.getCartItems(), cart.getUser());

        if (orderService.processOrder(order, cart.getUser(), shopId)) {
            cart.clear();
            return true;
        }
        return false;
    }

    private void addToExistingItem(Cart cart, Product product, Integer quantity) {
        cart.getItem(product).increaseQuantity(quantity);
    }

    private void addNewItem(Cart cart, Long productId, Integer quantity) {
        Item item = itemService.createItem(productId, quantity);
        cart.addItem(item);
    }

    private void countPrice(Cart cart) {
        BigDecimal summaryPrice = BigDecimal.valueOf(0);
        for (Item it : cart.getCartItems()) {
            summaryPrice = summaryPrice.add(it.getPrice());
        }
        cart.setSummaryPrice(summaryPrice);
    }

    private boolean notMoreThanAvailable(Cart cart, Product product, Integer quantity, Long shopId) {
        return (storageService.getQuantityPerShop(shopId, product.getId()) - (quantity + cart.getItem(product).getQuantity())) >= 0;
    }

    private boolean containsProduct(Cart cart, Product product) {
        return cart.getCartItems().stream().anyMatch(item -> item.getProduct().equals(product));
    }

}
