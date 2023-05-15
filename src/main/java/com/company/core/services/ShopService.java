package com.company.core.services;

import com.company.core.models.user.customer.Customer;

public interface ShopService {
    boolean checkoutCart(Customer customer);
    String getProductsString();
    String getCustomerOrdersString(Customer customer);
}
