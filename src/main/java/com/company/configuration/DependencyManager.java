package com.company.configuration;

import com.company.core.controllers.*;
import com.company.core.lists.CustomerList;
import com.company.core.lists.OrderList;
import com.company.core.lists.ProductList;
import com.company.core.Shop;
import com.company.core.models.user.customer.Customer;
import com.company.core.models.user.customer.ShoppingCart;
import com.company.core.services.*;
import com.company.core.services.impl.*;
import com.company.core.services.persistenceservices.CustomerListPersistenceService;
import com.company.core.services.persistenceservices.OrderListPersistenceService;
import com.company.core.services.persistenceservices.ProductListPersistenceService;

public class DependencyManager {
    private final ProductController productController;
    private final CustomerController customerController;
    private final CustomerListPersistenceService clps;

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
        ProductListService productListService = ProductListServiceImpl.getInstance(plps);
        this.productController = new ProductController(productListService);

        OrderListPersistenceService olps = OrderListPersistenceService.getInstance(shop.getOrderList());
        OrderListService orderListService = OrderListServiceImpl.getInstance(olps);

        ItemService itemService = ItemServiceImpl.getInstance(productListService);

        ShoppingCartService shoppingCartService = ShoppingCartServiceImpl.getInstance(itemService, productListService);

        this.clps = CustomerListPersistenceService.getInstance(shop.getCustomerList());
        CustomerService customerService = CustomerServiceImpl.getInstance(shoppingCartService);

        ShopService shopService = ShopServiceImpl.getInstance(shop, orderListService, productListService, customerService);

        this.customerController = new CustomerController(customerService, shopService);
    }

    public Customer testCustomerCreateMethod() {
        return clps.save(new Customer("whyretski", "123456", new ShoppingCart()));
    }

    public ProductController getProductController() {
        return productController;
    }

    public CustomerController getCustomerController() {
        return customerController;
    }

}
