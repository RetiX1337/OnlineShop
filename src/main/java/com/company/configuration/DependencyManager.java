package com.company.configuration;

import com.company.core.controllers.*;
import com.company.core.Shop;
import com.company.core.models.user.customer.Customer;
import com.company.core.models.user.customer.Cart;
import com.company.core.services.*;
import com.company.core.services.impl.*;
import com.company.core.services.persistenceservices.CustomerPersistenceService;
import com.company.core.services.persistenceservices.ItemPersistenceService;
import com.company.core.services.persistenceservices.OrderPersistenceService;
import com.company.core.services.persistenceservices.ProductPersistenceService;

public class DependencyManager {
    private final ProductController productController;
    private final CustomerController customerController;
    private final CustomerPersistenceService customerPersistenceService;

    public DependencyManager() {
        ProductPersistenceService productPersistenceService = new ProductPersistenceService();
        OrderPersistenceService orderPersistenceService = new OrderPersistenceService();
        ItemPersistenceService itemPersistenceService = new ItemPersistenceService();
        customerPersistenceService = new CustomerPersistenceService();

        Shop shop = new ShopBuilder()
                .withCustomers(customerPersistenceService)
                .withOrders(orderPersistenceService)
                .withProducts(productPersistenceService)
                .build();

        ProductService productService = new ProductServiceImpl(productPersistenceService);
        this.productController = new ProductController(productService);

        OrderService orderService = new OrderServiceImpl(orderPersistenceService);

        ItemService itemService = new ItemServiceImpl(productService, itemPersistenceService);

        CartService cartService = new CartServiceImpl(itemService, productService);

        CustomerService customerService = new CustomerServiceImpl(customerPersistenceService);

        ShopService shopService = new ShopServiceImpl(shop, orderService, productService, customerService);

        this.customerController = new CustomerController(customerService, shopService);
    }

    public Customer testCustomerCreateMethod() {
        return customerPersistenceService.save(new Customer("whyretski", "123456", new Cart()));
    }

    public ProductController getProductController() {
        return productController;
    }

    public CustomerController getCustomerController() {
        return customerController;
    }

}
