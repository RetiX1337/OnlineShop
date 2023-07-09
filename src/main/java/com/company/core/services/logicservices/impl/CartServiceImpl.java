package com.company.core.services.logicservices.impl;

import com.company.core.models.goods.Item;
import com.company.core.models.goods.Order;
import com.company.core.models.goods.Product;
import com.company.core.models.user.customer.Cart;
import com.company.core.models.user.customer.Customer;
import com.company.core.services.logicservices.*;

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
        if (storageService.getQuantityPerShop(shopId, productId)>0) {
            if (containsProduct(cart, productService.getProduct(productId))) {
                if (notMoreThanAvailable(cart, itemService.createItem(productId, quantity), shopId)) {
                    addToExistingItem(cart, productId, quantity);
                    countPrice(cart);
                    return true;
                } else {
                    return false;
                }
            } else {
                addNewItem(cart, productId, quantity);
                countPrice(cart);
                return true;
            }
        } else {
            return false;
        }
    }

    @Override
    public boolean deleteFromCart(Cart cart, Long productId, Integer quantity) {
        if (containsProduct(cart, productService.getProduct(productId))) {
            Item item = cart.getItem(productService.getProduct(productId));
            if (item.getQuantity().equals(quantity)) {
                cart.deleteItem(item);
            } else {
                item.decreaseQuantity(quantity);
            }
            countPrice(cart);
            return true;
        }
        return false;
    }

    @Override
    public boolean checkoutCart(Customer customer, Long shopId) {
        Order order = orderService.createOrder(customer.getShoppingCart().getProductsFromCart(), customer);
        if (customer.getShoppingCart().isEmpty()) return false;
        if (orderService.processOrder(order, customer, shopId)) {
            customer.getShoppingCart().clear();
            return true;
        }
        return false;
    }

    private void addToExistingItem(Cart cart, Long productId, Integer quantity) {
        cart.getItem(productService.getProduct(productId)).increaseQuantity(quantity);
    }

    private void addNewItem(Cart cart, Long productId, Integer quantity) {
        Item item = itemService.createItem(productId, quantity);
        cart.addItem(item);
    }

    private void countPrice(Cart cart) {
        cart.setSummaryPrice(new BigDecimal(0));
        for (Item it : cart.getProductsFromCart()) {
            cart.setSummaryPrice(cart.getSummaryPrice().add(it.getPrice()));
        }
    }

    private boolean notMoreThanAvailable(Cart cart, Item item, Long shopId) {
        if (item == null) return false;
        Product product = item.getProduct();
        return (storageService.getQuantityPerShop(shopId, product.getId()) - (item.getQuantity() + cart.getItem(product).getQuantity())) >= 0;
    }

    private boolean containsProduct(Cart cart, Product product) {
        return cart.getProductsFromCart().stream().anyMatch(item -> item.getProduct().getId().equals(product.getId()));
    }

}
