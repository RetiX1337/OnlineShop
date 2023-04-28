package com.company.core;

import com.company.core.lists.OrderList;
import com.company.core.lists.ProductList;

public class Shop {
    private final OrderList orderList;
    private final ProductList productList;

    public Shop(OrderList orderList, ProductList productList) {
        this.orderList = orderList;
        this.productList = productList;
    }
    //This method will be used for admins to create an order without a customer
    /*
    public void checkout() {
    }
    */

    public OrderList getOrderList() {
        return orderList;
    }

    public ProductList getProductList() {
        return productList;
    }
}
