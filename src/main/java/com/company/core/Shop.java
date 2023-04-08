package com.company.core;

import com.company.core.models.goods.Good;
import com.company.core.lists.GoodList;
import com.company.core.lists.OrderList;
import com.company.core.models.goods.Product;
import com.company.core.models.user.customer.Customer;

import java.util.HashMap;
import java.util.Stack;

public class Shop {
    private final GoodList goodList;
    private final OrderList orderList;

    public Shop(GoodList goodList, OrderList orderList) {
        this.goodList = goodList;
        this.orderList = orderList;
    }
    //This method will be used for admins to create an order without a customer
    /*
    public void checkout() {
    }
    */

    public OrderList getOrderList() {
        return orderList;
    }

    public GoodList getGoodList() {
        return goodList;
    }
}
