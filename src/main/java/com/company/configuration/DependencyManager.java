package com.company.configuration;

import com.company.core.ProductListObserver;
import com.company.core.controllers.*;
import com.company.core.lists.OrderList;
import com.company.core.lists.ProductList;
import com.company.core.Shop;
import com.company.core.models.user.customer.ShoppingCart;
import com.company.core.models.user.customer.Customer;
import com.company.core.services.impl.*;
import com.company.core.services.persistenceservices.OrderListPersistenceService;
import com.company.core.services.persistenceservices.ProductListPersistenceService;

import java.math.BigDecimal;

public class DependencyManager {
    private final Shop shop;
    private final Customer customer;
    private final OrderController orderController;
    private final ItemController itemController;
    private final ProductController productController;
    private final ShoppingCartController shoppingCartController;
    private final CustomerController customerController;
    private final ShopController shopController;

    public DependencyManager() {
        ProductList productList = new ProductList();
        ProductListObserver productListObserver = new ProductListObserver();

        OrderList orderList = new OrderList();

        this.shop = new Shop(orderList, productList);

        ShoppingCart shoppingCart = new ShoppingCart();

        this.customer = new Customer("whyretski", "qwerty123", new BigDecimal(150), shoppingCart, shop);

        ProductListPersistenceService plps = ProductListPersistenceService.getInstance(productList);
        ProductListServiceImpl productListServiceImpl = ProductListServiceImpl.getInstance(plps, productListObserver);
        this.productController = new ProductController(productListServiceImpl);

        OrderListPersistenceService olps = OrderListPersistenceService.getInstance(orderList);
        OrderListServiceImpl orderListServiceImpl = OrderListServiceImpl.getInstance(olps);
        this.orderController = new OrderController(orderListServiceImpl);

        ItemServiceImpl itemService = ItemServiceImpl.getInstance(productListServiceImpl);
        this.itemController = new ItemController(itemService);

        ShopServiceImpl shopServiceImpl = ShopServiceImpl.getInstance(shop, orderListServiceImpl);
        this.shopController = new ShopController(shopServiceImpl);

        ShoppingCartServiceImpl shoppingCartServiceImpl = ShoppingCartServiceImpl.getInstance(shoppingCart, itemService);
        this.shoppingCartController = new ShoppingCartController(shoppingCartServiceImpl);

        CustomerServiceImpl customerServiceImpl = CustomerServiceImpl.getInstance(customer);
        this.customerController = new CustomerController(customerServiceImpl, shoppingCartController);
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

    public ItemController getItemController() {
        return itemController;
    }

    public Customer getCustomer() {
        return customer;
    }

    public Shop getShop() {
        return shop;
    }
}
