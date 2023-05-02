package com.company.core.services.impl;

import com.company.core.Shop;
import com.company.core.models.goods.Item;
import com.company.core.models.goods.Product;
import com.company.core.models.user.customer.Customer;
import com.company.core.services.ShopService;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;

public class ShopServiceImpl implements ShopService {
    private final Shop shop;
    private final OrderListServiceImpl orderListServiceImpl;
    private final ProductListServiceImpl productListService;
    private final CustomerServiceImpl customerService;
    private static ShopServiceImpl instance;

    private ShopServiceImpl(Shop shop, OrderListServiceImpl orderListServiceImpl, ProductListServiceImpl productListService, CustomerServiceImpl customerService) {
        this.shop = shop;
        this.orderListServiceImpl = orderListServiceImpl;
        this.productListService = productListService;
        this.customerService = customerService;
    }

    @Override
    public boolean checkout(Customer customer) {
        if (!customer.getShoppingCart().isEmpty()) {
            BigDecimal summaryPrice = customer.getShoppingCart().getSummaryPrice();
            if (enoughMoney(customer, summaryPrice)) {
                processPayment(customer, summaryPrice);
                removeProductsFromProductList(customer.getShoppingCart().getProductsFromCart());
                createOrder(customer);
                customer.getShoppingCart().clear();
                return true;
            } else {
                return false;
            }
        }
        return false;
    }

    @Override
    public String getProductsString() {
        List<Product> products = productListService.getAllProducts();
        String result = "";
        for (Product p : products) {
            result = result.concat(p + "\n");
        }
        return result;
    }

    @Override
    public String getCustomerOrdersString(Customer customer) {
        return orderListServiceImpl.findByCustomer(customer).toString();
    }

    private void createOrder(Customer customer) {
        orderListServiceImpl.addOrder(orderListServiceImpl.createOrder(customer.getShoppingCart().getProductsFromCart(), customer));
    }

    private void removeProductsFromProductList(Collection<Item> items) {
        items.forEach((item) -> item.getProduct().setQuantity(item.getProduct().getQuantity() - item.getQuantity()));
    }

    private void processPayment(Customer customer, BigDecimal summaryPrice) {
        customer.setWallet(customer.getWallet().subtract(summaryPrice));
    }

    private boolean enoughMoney(Customer customer, BigDecimal summaryPrice) {
        return summaryPrice.compareTo(customer.getWallet()) == 0 || summaryPrice.compareTo(customer.getWallet()) == -1;
    }

    public static ShopServiceImpl getInstance(Shop shop, OrderListServiceImpl orderListServiceImpl, ProductListServiceImpl productListService, CustomerServiceImpl customerService) {
        if (instance == null) {
            instance = new ShopServiceImpl(shop, orderListServiceImpl, productListService, customerService);
        }
        return instance;
    }
}
