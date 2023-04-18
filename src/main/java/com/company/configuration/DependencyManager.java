package com.company.configuration;

import com.company.core.ProductListObserver;
import com.company.core.controllers.*;
import com.company.core.lists.GoodList;
import com.company.core.lists.OrderList;
import com.company.core.lists.ProductList;
import com.company.core.Shop;
import com.company.core.models.user.customer.ShoppingCart;
import com.company.core.models.user.customer.Customer;
import com.company.core.services.impl.*;
import com.company.core.services.persistenceservices.GoodListPersistenceService;
import com.company.core.services.persistenceservices.OrderListPersistenceService;
import com.company.core.services.persistenceservices.ProductListPersistenceService;

import java.math.BigDecimal;

public class DependencyManager {
    private final Shop shop;
    private final Customer customer;
    private final GoodController goodController;
    private final OrderController orderController;
    private final ProductController productController;
    private final ShoppingCartController shoppingCartController;
    private final CustomerController customerController;
    private final ShopController shopController;

    public DependencyManager() {
        ProductList productList = new ProductList();
        ProductListObserver productListObserver = new ProductListObserver();

        GoodList goodList = new GoodList(productList);

        OrderList orderList = new OrderList();

        this.shop = new Shop(goodList, orderList);

        ShoppingCart shoppingCart = new ShoppingCart();

        this.customer = new Customer("whyretski", "qwerty123", new BigDecimal(150), shoppingCart, shop);

        ProductListPersistenceService plps = ProductListPersistenceService.getInstance(productList);
        ProductListServiceImpl productListServiceImpl = ProductListServiceImpl.getInstance(plps, productListObserver);
        this.productController = new ProductController(productListServiceImpl);

        GoodListPersistenceService glps = GoodListPersistenceService.getInstance(goodList);
        GoodListServiceImpl goodListServiceImpl = GoodListServiceImpl.getInstance(glps, productListServiceImpl, productListObserver);
        this.goodController = new GoodController(goodListServiceImpl);

        OrderListPersistenceService olps = OrderListPersistenceService.getInstance(orderList);
        OrderListServiceImpl orderListServiceImpl = OrderListServiceImpl.getInstance(olps);
        this.orderController = new OrderController(orderListServiceImpl);

        ShopServiceImpl shopServiceImpl = ShopServiceImpl.getInstance(shop, goodListServiceImpl, orderListServiceImpl);
        this.shopController = new ShopController(shopServiceImpl);

        ShoppingCartServiceImpl shoppingCartServiceImpl = ShoppingCartServiceImpl.getInstance(shoppingCart, goodListServiceImpl);
        this.shoppingCartController = new ShoppingCartController(shoppingCartServiceImpl);

        CustomerServiceImpl customerServiceImpl = CustomerServiceImpl.getInstance(customer);
        this.customerController = new CustomerController(customerServiceImpl, shoppingCartController);
    }

    public GoodController getGoodController() {
        return goodController;
    }

    public OrderController getOrderController() {
        return orderController;
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

    public Customer getCustomer() {
        return customer;
    }

    public Shop getShop() {
        return shop;
    }
}
