package com.company.core.shop;

import com.company.core.good.Good;
import com.company.core.good.GoodList;
import com.company.core.order.OrderList;
import com.company.core.product.Product;
import com.company.core.user.customer.Customer;

import java.util.HashMap;
import java.util.Stack;

public class Shop {
    private final GoodList goodList;
    private final OrderList orderList;

    public Shop(GoodList goodList, OrderList orderList) {
        this.goodList = goodList;
        this.orderList = orderList;
    }

    public void addToOrder(Customer customer, int productId, int amount) {
        customer.getShoppingCart().addToCart(goodList, productId, amount);
    }

    public void checkout(Customer customer) {
        HashMap<Product, Stack<Good>> goods = customer.getShoppingCart().getGoodsCart();
        goods.forEach((key, value)
                -> value.forEach(good
                -> goodList.delete(good.getProduct())));
        orderList.addOrder(goods, customer);
    }
    //This method will be used for admins to create an order without a customer
    /*
    public void checkout() {
    }
    */
    public GoodList getGoodList() {
        return goodList;
    }
}
