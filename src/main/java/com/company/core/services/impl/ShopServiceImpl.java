package com.company.core.services.impl;

import com.company.core.Shop;
import com.company.core.models.goods.Good;
import com.company.core.models.goods.Product;
import com.company.core.models.user.customer.Customer;

import java.util.HashMap;
import java.util.Stack;

public class ShopServiceImpl {
    private final Shop shop;
    private final GoodListServiceImpl goodListServiceImpl;
    private final OrderListServiceImpl orderListServiceImpl;
    private static ShopServiceImpl instance;

    private ShopServiceImpl(Shop shop, GoodListServiceImpl goodListServiceImpl, OrderListServiceImpl orderListServiceImpl) {
        this.shop = shop;
        this.goodListServiceImpl = goodListServiceImpl;
        this.orderListServiceImpl = orderListServiceImpl;
    }

    public void checkout(Customer customer) {
        HashMap<Product, Stack<Good>> goods = customer.getShoppingCart().getGoodsCart();
        goods.forEach((key, value)
                -> value.forEach(good
                -> goodListServiceImpl.deleteGood(good.getProduct().getId())));
        orderListServiceImpl.createOrder(goods, customer);
        customer.getShoppingCart().getGoodsCart().clear();
    }

    public static ShopServiceImpl getInstance(Shop shop, GoodListServiceImpl goodListServiceImpl, OrderListServiceImpl orderListServiceImpl) {
        if (instance == null) {
            instance = new ShopServiceImpl(shop, goodListServiceImpl, orderListServiceImpl);
        }
        return instance;
    }
}
