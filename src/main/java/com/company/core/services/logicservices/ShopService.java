package com.company.core.services.logicservices;

import com.company.core.models.EntityNotFoundException;
import com.company.core.models.Shop;
import com.company.core.models.goods.Product;
import com.company.core.models.user.customer.Customer;

import java.util.List;

public interface ShopService {
    boolean checkoutCart(Customer customer);
    String getProductsString();
    String getCustomerOrdersString(Customer customer);
    Shop createShop(String name, String address);
    void addShop(Shop shop);
    Shop getShop(Long id) throws EntityNotFoundException;
    List<Shop> getAllShops();
}
