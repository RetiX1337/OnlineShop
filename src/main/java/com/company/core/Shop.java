package com.company.core;

import com.company.core.services.persistenceservices.CustomerPersistenceService;
import com.company.core.services.persistenceservices.OrderPersistenceService;
import com.company.core.services.persistenceservices.ProductPersistenceService;

public class Shop {
    private final OrderPersistenceService orderPersistenceService;
    private final ProductPersistenceService productPersistenceService;
    private final CustomerPersistenceService customerPersistenceService;

    public Shop(OrderPersistenceService orderPersistenceService, ProductPersistenceService productPersistenceService, CustomerPersistenceService customerPersistenceService) {
        this.orderPersistenceService = orderPersistenceService;
        this.productPersistenceService = productPersistenceService;
        this.customerPersistenceService = customerPersistenceService;
    }
}
