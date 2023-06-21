package com.company.core.services.logicservices.impl;

import com.company.core.models.EntityNotFoundException;
import com.company.core.models.Shop;
import com.company.core.models.goods.Order;
import com.company.core.models.user.customer.Customer;
import com.company.core.services.logicservices.ShopService;
import com.company.core.services.persistenceservices.PersistenceInterface;

import java.math.BigDecimal;
import java.util.List;

public class ShopServiceImpl implements ShopService {
    private final PersistenceInterface<Shop> shopPersistenceService;

    public ShopServiceImpl(PersistenceInterface<Shop> shopPersistenceService) {
        this.shopPersistenceService = shopPersistenceService;
    }

    //TODO MOVE THIS METHOD TO ANOTHER SERVICE (WILL BE CREATED)

    @Override
    public boolean checkoutCart(Customer customer) {/*
        Order order = orderService.createOrder(customer.getShoppingCart().getProductsFromCart(), customer);
        order.setOrderStatus(OrderStatus.NEW);
        if (processPayment(customer, order)) {
            order.setOrderStatus(OrderStatus.PAID);
            orderService.addOrder(order);
            customer.getShoppingCart().clear();
            return true;
        }*/
        return false;
    }

    //TODO MOVE THIS METHOD TO PRODUCT SERVICE
    @Override
    public String getProductsString() {/*
        List<Product> products = productService.getAllProducts();
        String result = "";
        for (Product p : products) {
            result = result.concat(p + "\n");
        }*/
        return null;
    }

    //TODO MOVE THIS METHOD TO CUSTOMER SERVICE
    @Override
    public String getCustomerOrdersString(Customer customer) {
        return /*orderService.findByCustomer(customer).toString();*/ null;
    }

    @Override
    public Shop createShop(String name, String address) {
        return new Shop(name, address);
    }

    @Override
    public void addShop(Shop shop) {
        shopPersistenceService.save(shop);
    }

    @Override
    public Shop getShop(Long id) throws EntityNotFoundException {
        if (shopPersistenceService.isPresent(id)) {
            return shopPersistenceService.findById(id);
        } else {
            throw new EntityNotFoundException();
        }
    }

    @Override
    public List<Shop> getAllShops() {
        return shopPersistenceService.findAll();
    }


}
