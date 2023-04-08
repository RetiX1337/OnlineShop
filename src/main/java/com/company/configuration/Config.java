package com.company.configuration;

import com.company.core.controllers.*;
import com.company.core.lists.GoodList;
import com.company.core.lists.OrderList;
import com.company.core.lists.ProductList;
import com.company.core.Shop;
import com.company.core.models.user.customer.ShoppingCart;
import com.company.core.models.user.customer.Customer;
import com.company.core.services.*;

import java.math.BigDecimal;

public class Config {
    private final Shop shop;
    private final Customer customer;
    private final GoodController goodController;
    private final OrderController orderController;
    private final ProductController productController;
    private final ShoppingCartController shoppingCartController;
    private final CustomerController customerController;
    private final ShopController shopController;

    public Config() {
        ProductList productList = new ProductList();
        ProductService productService = ProductService.getInstance(productList);
        this.productController = new ProductController(productService);

        GoodList goodList = new GoodList(productList);
        GoodService goodService = GoodService.getInstance(goodList, productService);
        this.goodController = new GoodController(goodService);
        goodController.fillGoodList();

        OrderList orderList = new OrderList();
        OrderService orderService = OrderService.getInstance(orderList);
        this.orderController = new OrderController(orderService);

        this.shop = new Shop(goodList, orderList);
        ShopService shopService = ShopService.getInstance(shop, goodService, orderService);
        this.shopController = new ShopController(shopService);

        ShoppingCart shoppingCart = new ShoppingCart();
        ShoppingCartService shoppingCartService = ShoppingCartService.getInstance(shoppingCart, goodService);
        this.shoppingCartController = new ShoppingCartController(shoppingCartService);

        this.customer = new Customer("whyretski", "qwerty123", new BigDecimal(150), shoppingCart, shop);
        CustomerService customerService = CustomerService.getInstance(customer);
        this.customerController = new CustomerController(customerService, shoppingCartController);
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
