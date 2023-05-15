package com.company.core.lists;

import com.company.core.models.user.customer.Customer;

import java.util.HashMap;

public class CustomerList {
    private final HashMap<Long, Customer> customerList = new HashMap<>();

    public HashMap<Long, Customer> getCustomerList() {
        return customerList;
    }
}
