package com.company.configuration;

import com.company.core.models.Shop;
import com.company.core.models.Storage;
import com.company.core.models.goods.Order;
import com.company.core.models.goods.Product;
import com.company.core.models.user.customer.Customer;
import com.company.core.services.persistenceservices.PersistenceInterface;

public class ShopBuilder {
    private PersistenceInterface<Customer> customerPersistenceService;
    private PersistenceInterface<Order> orderPersistenceService;
    private PersistenceInterface<Product> productPersistenceService;
    private Storage storage;

    public ShopBuilder withOrders(PersistenceInterface<Order> orderPersistenceService) {
        this.orderPersistenceService = orderPersistenceService;
        return this;
    }

    public ShopBuilder withProducts(PersistenceInterface<Product> productPersistenceService) {
        this.productPersistenceService = productPersistenceService;
        return this;
    }

    public ShopBuilder withCustomers(PersistenceInterface<Customer> customerPersistenceService) {
        this.customerPersistenceService = customerPersistenceService;
        return this;
    }

    public ShopBuilder withStorage(Storage storage) {
        this.storage = storage;
        return this;
    }

    public Shop build() {
        return new Shop(orderPersistenceService, productPersistenceService, customerPersistenceService, storage);
    }
}