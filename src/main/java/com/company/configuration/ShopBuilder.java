package com.company.configuration;

import com.company.core.Shop;
import com.company.core.services.persistenceservices.CustomerPersistenceService;
import com.company.core.services.persistenceservices.OrderPersistenceService;
import com.company.core.services.persistenceservices.ProductPersistenceService;

public class ShopBuilder {
    private CustomerPersistenceService customerPersistenceService;
    private OrderPersistenceService orderPersistenceService;
    private ProductPersistenceService productPersistenceService;

    public ShopBuilder withOrders(OrderPersistenceService orderPersistenceService) {
        this.orderPersistenceService = orderPersistenceService;
        return this;
    }

    public ShopBuilder withProducts(ProductPersistenceService productPersistenceService) {
        this.productPersistenceService = productPersistenceService;
        return this;
    }

    public ShopBuilder withCustomers(CustomerPersistenceService customerPersistenceService) {
        this.customerPersistenceService = customerPersistenceService;
        return this;
    }

    public Shop build() {
        return new Shop(orderPersistenceService, productPersistenceService, customerPersistenceService);
    }
}