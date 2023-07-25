package com.company.configuration;

import com.company.core.pools.HibernateSessionPool;
import com.company.core.pools.JDBCConnectionPool;
import com.company.core.controllers.*;
import com.company.core.models.Shop;
import com.company.core.models.Storage;
import com.company.core.models.goods.Item;
import com.company.core.models.goods.Order;
import com.company.core.models.goods.ProductBase;
import com.company.core.models.user.customer.Customer;
import com.company.core.services.logicservices.impl.*;
import com.company.core.services.logicservices.*;
import com.company.core.services.persistenceservices.*;
import com.company.core.services.persistenceservices.dbimpl.*;
import com.company.core.services.persistenceservices.hibernateimpl.*;
import org.springframework.beans.factory.annotation.Autowired;

public class DependencyManager {
    private final CustomerController customerController;
    private final ShopController shopController;
    private final ProductController productController;
    private final TestController testController;
    private final CartController cartController;
    private final OrderController orderController;
    private final StorageController storageController;
    private static DependencyManager instance;
    private static int strategy = -1;

    private DependencyManager() {
        JDBCConnectionPool pool = new JDBCConnectionPool("com.mysql.jdbc.Driver", "jdbc:mysql://localhost:3306/online_shop", "root", "matretsk82004");
        HibernateSessionPool hibernateSessionPool = new HibernateSessionPool();

        PersistenceInterface<ProductBase> productPersistenceService;
        PersistenceInterface<Item> itemPersistenceService;
        PersistenceInterface<Storage> storagePersistenceService;
        PersistenceInterface<Shop> shopPersistenceService;
        PersistenceInterface<Customer> customerPersistenceService;
        PersistenceInterface<Order> orderPersistenceService;

        if (strategy == 1) {
            productPersistenceService = new ProductPersistenceServiceHibernate(hibernateSessionPool);
            itemPersistenceService = new ItemPersistenceServiceHibernate(hibernateSessionPool);
            storagePersistenceService = new StoragePersistenceServiceHibernate(hibernateSessionPool);
            shopPersistenceService = new ShopPersistenceServiceHibernate(hibernateSessionPool);
            customerPersistenceService = new CustomerPersistenceServiceHibernate(hibernateSessionPool);
            orderPersistenceService = new OrderPersistenceServiceHibernate(hibernateSessionPool);
        } else {
            productPersistenceService = new ProductPersistenceServiceDatabase(pool);
            itemPersistenceService = new ItemPersistenceServiceDatabase(pool, productPersistenceService);
            storagePersistenceService = new StoragePersistenceServiceDatabase(pool, productPersistenceService);
            shopPersistenceService = new ShopPersistenceServiceDatabase(pool, storagePersistenceService);
            customerPersistenceService = new CustomerPersistenceServiceDatabase(pool);
            orderPersistenceService = new OrderPersistenceServiceDatabase(pool, itemPersistenceService, customerPersistenceService);
        }

        ProductService productService = new ProductServiceImpl(productPersistenceService);

        ItemService itemService = new ItemServiceImpl(itemPersistenceService, productService);

        StorageService storageService = new StorageServiceImpl(storagePersistenceService, shopPersistenceService, productService);

        OrderService orderService = new OrderServiceImpl(orderPersistenceService, itemService, storageService);

        CartService cartService = new CartServiceImpl(itemService, productService, orderService, storageService);


        CustomerService customerService = new CustomerServiceImpl(customerPersistenceService);

        ShopService shopService = new ShopServiceImpl(shopPersistenceService, storagePersistenceService);

        this.cartController = new CartController(cartService, productService);

        this.productController = new ProductController(productService, storageService);

        this.customerController = new CustomerController(customerService);

        this.shopController = new ShopController(shopService);

        this.orderController = new OrderController(orderService);

        this.storageController = new StorageController(storageService);

        this.testController = new TestController(cartService, orderService, productService, customerService, storageService, shopService);
    }


    public ProductController getProductController() {
        return productController;
    }

    public TestController getTestController() {
        return testController;
    }

    public CartController getCartController() {
        return cartController;
    }

    public CustomerController getCustomerController() {
        return customerController;
    }

    public ShopController getShopController() {
        return shopController;
    }

    public StorageController getStorageController() {
        return storageController;
    }

    public OrderController getOrderController() {
        return orderController;
    }

    public static DependencyManager getInstance() {
        if (instance == null) {
            instance = new DependencyManager();
        }
        return instance;
    }

    public static void setStrategy(int strategy) {
        DependencyManager.strategy = strategy;
    }
}
