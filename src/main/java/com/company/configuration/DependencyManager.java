package com.company.configuration;

import com.company.core.ProductListObserver;
import com.company.core.controllers.*;
import com.company.core.lists.CustomerList;
import com.company.core.lists.OrderList;
import com.company.core.lists.ProductList;
import com.company.core.Shop;
import com.company.core.services.impl.*;
import com.company.core.services.persistenceservices.CustomerListPersistenceService;
import com.company.core.services.persistenceservices.OrderListPersistenceService;
import com.company.core.services.persistenceservices.ProductListPersistenceService;

public class DependencyManager {
    private final ProductController productController;
    private final CustomerController customerController;
    private final ShopController shopController;

    public DependencyManager() {
        ProductList productList = new ProductList();
        OrderList orderList = new OrderList();
        CustomerList customerList = new CustomerList();

        Shop shop = new ShopBuilder()
                .withCustomers(customerList)
                .withOrders(orderList)
                .withProducts(productList)
                .build();

        ProductListPersistenceService plps = ProductListPersistenceService.getInstance(shop.getProductList());
        ProductListServiceImpl productListServiceImpl = ProductListServiceImpl.getInstance(plps);
        this.productController = new ProductController(productListServiceImpl);

        OrderListPersistenceService olps = OrderListPersistenceService.getInstance(shop.getOrderList());
        OrderListServiceImpl orderListServiceImpl = OrderListServiceImpl.getInstance(olps);

        ItemServiceImpl itemService = ItemServiceImpl.getInstance(productListServiceImpl);

        ShoppingCartServiceImpl shoppingCartServiceImpl = ShoppingCartServiceImpl.getInstance(itemService, productListServiceImpl);

        CustomerListPersistenceService clps = CustomerListPersistenceService.getInstance(shop.getCustomerList());
        CustomerServiceImpl customerServiceImpl = CustomerServiceImpl.getInstance(clps, shoppingCartServiceImpl);
        this.customerController = new CustomerController(customerServiceImpl);


        ShopServiceImpl shopServiceImpl = ShopServiceImpl.getInstance(shop, orderListServiceImpl, productListServiceImpl, customerServiceImpl);
        this.shopController = new ShopController(shopServiceImpl);
    }

    public ProductController getProductController() {
        return productController;
    }

    public CustomerController getCustomerController() {
        return customerController;
    }

    public ShopController getShopController() {
        return shopController;
    }
}
