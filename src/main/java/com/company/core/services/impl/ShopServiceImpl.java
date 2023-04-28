package com.company.core.services.impl;

import com.company.core.Shop;
import com.company.core.models.user.customer.Customer;

import java.math.BigDecimal;

public class ShopServiceImpl {
    private final Shop shop;
    private final OrderListServiceImpl orderListServiceImpl;
    private static ShopServiceImpl instance;

    private ShopServiceImpl(Shop shop, OrderListServiceImpl orderListServiceImpl) {
        this.shop = shop;
        this.orderListServiceImpl = orderListServiceImpl;
    }

    public boolean checkout(Customer customer) {
        if (!customer.getShoppingCart().isEmpty()) {
            BigDecimal summaryPrice = customer.getShoppingCart().getSummaryPrice();
            if (summaryPrice.compareTo(customer.getWallet()) == 0 || summaryPrice.compareTo(customer.getWallet()) == -1) {
                customer.getShoppingCart().getShoppingCart().forEach((product, item) ->
                    item.getProduct().setQuantity(product.getQuantity()-item.getQuantity()));
                orderListServiceImpl.addOrder(orderListServiceImpl.createOrder(customer.getShoppingCart().getShoppingCart(), customer));
                customer.getShoppingCart().clear();
                customer.setWallet(customer.getWallet().subtract(summaryPrice));
                return true;
            } else {
                return false;
            }
        }
        return false;
    }

    public static ShopServiceImpl getInstance(Shop shop, OrderListServiceImpl orderListServiceImpl) {
        if (instance == null) {
            instance = new ShopServiceImpl(shop, orderListServiceImpl);
        }
        return instance;
    }
}
