package com.company.configuration;

import com.company.core.controllers.*;
import com.company.core.lists.CustomerList;
import com.company.core.lists.ItemList;
import com.company.core.lists.OrderList;
import com.company.core.lists.ProductList;
import com.company.core.Shop;
import com.company.core.models.user.customer.Customer;
import com.company.core.models.user.customer.ShoppingCart;
import com.company.core.services.*;
import com.company.core.services.impl.*;
import com.company.core.services.persistenceservices.CustomerPersistenceService;
import com.company.core.services.persistenceservices.ItemPersistenceService;
import com.company.core.services.persistenceservices.OrderPersistenceService;
import com.company.core.services.persistenceservices.ProductPersistenceService;

public class DependencyManager {
    private final ProductController productController;
    private final CustomerController customerController;
    private final CustomerPersistenceService cps;

    public DependencyManager() {
        ProductList productList = new ProductList();
        OrderList orderList = new OrderList();
        CustomerList customerList = new CustomerList();
        ItemList itemList = new ItemList();

        Shop shop = new ShopBuilder()
                .withCustomers(customerList)
                .withOrders(orderList)
                .withProducts(productList)
                .build();

        ProductPersistenceService pps = new ProductPersistenceService(shop.getProductList());
        ProductListService productListService = new ProductServiceImpl(pps);
        this.productController = new ProductController(productListService);

        OrderPersistenceService ops = new OrderPersistenceService(shop.getOrderList());
        OrderService orderService = new OrderServiceImpl(ops);

        ItemPersistenceService ips = new ItemPersistenceService(itemList);
        ItemService itemService = new ItemServiceImpl(productListService, ips);

        ShoppingCartService shoppingCartService = new ShoppingCartServiceImpl(itemService, productListService);

        this.cps = new CustomerPersistenceService(shop.getCustomerList());
        CustomerService customerService = new CustomerServiceImpl(shoppingCartService, cps);

        ShopService shopService = new ShopServiceImpl(shop, orderService, productListService, customerService);

        this.customerController = new CustomerController(customerService, shopService);
    }

    public Customer testCustomerCreateMethod() {
        return cps.save(new Customer("whyretski", "123456", new ShoppingCart()));
    }

    public ProductController getProductController() {
        return productController;
    }

    public CustomerController getCustomerController() {
        return customerController;
    }

}
