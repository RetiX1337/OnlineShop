package com.company.core.services.logicservices;

import com.company.core.models.user.customer.Customer;

public interface ShopService {
    boolean checkoutCart(Customer customer);
    String getProductsString();
    String getCustomerOrdersString(Customer customer);
}
