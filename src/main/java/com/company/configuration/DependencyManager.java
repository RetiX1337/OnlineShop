package com.company.configuration;

import com.company.JDBCConnectionPool;
import com.company.core.controllers.*;
import com.company.core.models.Shop;
import com.company.core.models.Storage;
import com.company.core.models.goods.Item;
import com.company.core.models.goods.Order;
import com.company.core.models.goods.Product;
import com.company.core.models.user.customer.Customer;
import com.company.core.models.user.customer.Cart;
import com.company.core.services.logicservices.impl.*;
import com.company.core.services.logicservices.*;
import com.company.core.services.persistenceservices.*;
import com.company.core.services.persistenceservices.dbimpl.*;
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
        ProductStoragePersistence productStoragePersistence = new ProductStoragePersistenceServiceDatabase(pool);
        ShopStoragePersistence shopStoragePersistence = new ShopStoragePersistenceServiceDatabase(pool);
        PersistenceInterface<Order> orderPersistenceService = new OrderPersistenceService();
        PersistenceInterface<Item> itemPersistenceService = new ItemPersistenceService();
        PersistenceInterface<Shop> shopPersistenceService = new ShopPersistenceServiceDatabase(pool);
        PersistenceInterface<Storage> storagePersistenceService = new StoragePersistenceServiceDatabase(pool);
        this.customerPersistenceService = new CustomerPersistenceService();

        Shop shop = shopPersistenceService.findById(1L);

        ShopStorageService shopStorageService = new ShopStorageServiceImpl(shopStoragePersistence);
        ProductService productService = new ProductServiceImpl(productPersistenceService);
        ProductStorageService productStorageService = new ProductStorageServiceImpl(productStoragePersistence, shopStorageService);
        ProductManagerService productManagerService = new ProductManagerServiceImpl(productService, productStorageService);

        this.productController = new ProductController(productService);

        OrderService orderService = new OrderServiceImpl(orderPersistenceService);

        ItemService itemService = new ItemServiceImpl(productManagerService, itemPersistenceService);

        CartService cartService = new CartServiceImpl(itemService, productService, orderService);

        CustomerService customerService = new CustomerServiceImpl(customerPersistenceService);

        ShopService shopService = new ShopServiceImpl(shopPersistenceService);

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
