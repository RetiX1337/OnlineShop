package com.company.core;

import com.company.core.lists.CustomerList;
import com.company.core.lists.OrderList;
import com.company.core.lists.ProductList;

public class Shop {
    private final OrderList orderList;
    private final ProductList productList;
    private final CustomerList customerList;

    public Shop(OrderList orderList, ProductList productList, CustomerList customerList) {
        this.orderList = orderList;
        this.productList = productList;
        this.customerList = customerList;
    }

    public OrderList getOrderList() {
        return orderList;
    }

    public ProductList getProductList() {
        return productList;
    }

    public CustomerList getCustomerList() {
        return customerList;
    }
}
