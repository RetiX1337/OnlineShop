package com.company.configuration;

import com.company.core.Shop;
import com.company.core.lists.CustomerList;
import com.company.core.lists.OrderList;
import com.company.core.lists.ProductList;

public class ShopBuilder {
    private CustomerList customerList;
    private OrderList orderList;
    private ProductList productList;

    public ShopBuilder withOrders(OrderList orderList) {
        this.orderList = orderList;
        return this;
    }

    public ShopBuilder withProducts(ProductList productList) {
        this.productList = productList;
        return this;
    }

    public ShopBuilder withCustomers(CustomerList customerList) {
        this.customerList = customerList;
        return this;
    }

    public Shop build() {
        return new Shop(orderList, productList, customerList);
    }
}