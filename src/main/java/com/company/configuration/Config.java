package com.company.configuration;

import com.company.core.good.GoodList;
import com.company.core.order.OrderList;
import com.company.core.product.ProductList;
import com.company.core.shop.Shop;
import com.company.core.shop.ShoppingCart;
import com.company.core.user.customer.Customer;

public class Config {
    private final Shop shop;
    private final Customer customer;

    public Config(Customer customer) {
        ProductList productList = new ProductList();
        productList.fillProductList();
        GoodList goodList = new GoodList(productList);
        goodList.fillGoodList();
        OrderList orderList = new OrderList();
        this.shop = new Shop(goodList, orderList);
        this.customer = customer;
        ShoppingCart shoppingCart = new ShoppingCart(shop);
    }

    public Customer getCustomer() {
        return customer;
    }

    public Shop getShop() {
        return shop;
    }
}
