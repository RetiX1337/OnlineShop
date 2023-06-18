package com.company.core.models;

import com.company.core.models.goods.Order;
import com.company.core.models.goods.Product;
import com.company.core.models.user.customer.Customer;
import com.company.core.services.persistenceservices.PersistenceInterface;

public class Shop {
    private Storage storage;
    private final PersistenceInterface<Order> orderPersistenceService;
    private final PersistenceInterface<Product> productPersistenceService;
    private final PersistenceInterface<Customer> customerPersistenceService;

    public Shop(PersistenceInterface<Order> orderPersistenceService, PersistenceInterface<Product> productPersistenceService, PersistenceInterface<Customer> customerPersistenceService, Storage storage) {
        this.orderPersistenceService = orderPersistenceService;
        this.productPersistenceService = productPersistenceService;
        this.customerPersistenceService = customerPersistenceService;
        this.storage = storage;
    }
}
