package com.company.core.services;

import com.company.core.Shop;
import com.company.core.controllers.GoodController;
import com.company.core.models.goods.Good;
import com.company.core.models.goods.Product;
import com.company.core.models.user.customer.Customer;
import com.company.core.models.user.customer.ShoppingCart;

import java.util.HashMap;
import java.util.Stack;

public class ShopService {
    private final Shop shop;
    private final GoodService goodService;
    private final OrderService orderService;
    private static ShopService instance;

    private ShopService(Shop shop, GoodService goodService, OrderService orderService) {
        this.shop = shop;
        this.goodService = goodService;
        this.orderService = orderService;
    }

    public void checkout(Customer customer) {
        HashMap<Product, Stack<Good>> goods = customer.getShoppingCart().getGoodsCart();
        goods.forEach((key, value)
                -> value.forEach(good
                -> shop.getGoodList().delete(good.getProduct())));
        orderService.createOrder(goods, customer);
        customer.getShoppingCart().getGoodsCart().clear();
    }

    public static ShopService getInstance(Shop shop, GoodService goodService, OrderService orderService) {
        if (instance == null) {
            instance = new ShopService(shop, goodService, orderService);
        }
        return instance;
    }
}
