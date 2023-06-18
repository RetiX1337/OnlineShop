package com.company.configuration;

import com.company.JDBCConnectionPool;
import com.company.core.controllers.*;
import com.company.core.models.Shop;
import com.company.core.models.goods.Item;
import com.company.core.models.goods.Order;
import com.company.core.models.goods.Product;
import com.company.core.models.user.customer.Customer;
import com.company.core.models.user.customer.Cart;
import com.company.core.services.logicservices.impl.*;
import com.company.core.services.logicservices.*;
import com.company.core.services.persistenceservices.*;
import com.company.core.services.persistenceservices.dbimpl.ProductPersistenceServiceDatabase;
import com.company.core.services.persistenceservices.mapimpl.CustomerPersistenceService;
import com.company.core.services.persistenceservices.mapimpl.ItemPersistenceService;
import com.company.core.services.persistenceservices.mapimpl.OrderPersistenceService;

public class DependencyManager {
    private final ProductController productController;
    private final CustomerController customerController;
    private final PersistenceInterface<Customer> customerPersistenceService;

    public DependencyManager() {
        JDBCConnectionPool pool = new JDBCConnectionPool("com.mysql.jdbc.Driver", "jdbc:mysql://localhost:3306/online_shop", "root", "matretsk82004");

        PersistenceInterface<Product> productPersistenceService = new ProductPersistenceServiceDatabase(pool);
        PersistenceInterface<Order> orderPersistenceService = new OrderPersistenceService();
        PersistenceInterface<Item> itemPersistenceService = new ItemPersistenceService();
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

        this.customerController = new CustomerController(customerService, shopService, cartService);
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
