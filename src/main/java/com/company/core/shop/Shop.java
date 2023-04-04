package com.company.core.shop;

import com.company.core.good.Good;
import com.company.core.good.GoodList;
import com.company.core.order.OrderList;
import com.company.core.user.customer.Customer;

import java.util.ArrayList;

public class Shop {
    private final GoodList goodList;
    private final OrderList orderList;

    public Shop(GoodList goodList, OrderList orderList) {
        this.goodList = goodList;
        this.orderList = orderList;
    }

    public void sell(Customer customer) {
        ArrayList<Good> goods = customer.getShoppingCart().getGoodsCart();
        for (Good good : goods) {
            goodList.delete(good.getProduct());
        }
        orderList.addOrder();
    }

    public GoodList getGoodList() {
        return goodList;
    }
}
