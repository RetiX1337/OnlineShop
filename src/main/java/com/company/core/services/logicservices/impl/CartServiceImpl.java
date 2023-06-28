package com.company.core.services.logicservices.impl;

import com.company.core.models.EntityNotFoundException;
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
    public boolean addToCart(Cart cart, Long productId, Integer quantity, Long shopId) throws EntityNotFoundException {
        if (containsProduct(cart, productService.getProduct(productId))) {
            if (!moreThanAvailable(cart, itemService.createItem(productId, quantity), shopId)) {
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
        if (orderService.processOrder(order, customer, shopId)) {
            customer.getShoppingCart().clear();
            return true;
        }
        return false;
    }

    private void addToCartItem(Cart cart, Long productId, Integer quantity) throws EntityNotFoundException {
        cart.getItem(productService.getProduct(productId)).increaseQuantity(quantity);
    }

    private void countPrice(Cart cart) {
        cart.setSummaryPrice(new BigDecimal(0));
        for (Item it : cart.getProductsFromCart()) {
            cart.setSummaryPrice(cart.getSummaryPrice().add(it.getPrice()));
        }
    }

    private boolean moreThanAvailable(Cart cart, Item item, Long shopId) {
        if (item == null) return false;
        Product product = item.getProduct();
        return item.getQuantity() - (cart.getItem(product).getQuantity() + storageService.getQuantityPerShop(shopId, product.getId())) >= 0;
    }

    private boolean containsProduct(Cart cart, Product product) {
        return cart.getProductsFromCart().stream().anyMatch(item -> item.getProduct().getId().equals(product.getId()));
    }

    private void addOrUpdate(Cart cart, Long productId, Integer quantity) throws EntityNotFoundException {
        if (containsProduct(cart, productService.getProduct(productId))) {
            addToCartItem(cart, productId, quantity);
        } else {
            Item item = itemService.createItem(productId, quantity);
            cart.addItem(item);
        }
        countPrice(cart);
    }
}
