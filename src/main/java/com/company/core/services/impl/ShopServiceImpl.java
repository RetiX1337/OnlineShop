package com.company.core.services.impl;

import com.company.core.Shop;
import com.company.core.models.goods.Item;
import com.company.core.models.goods.Product;
import com.company.core.models.user.customer.Customer;
import com.company.core.services.CustomerService;
import com.company.core.services.OrderListService;
import com.company.core.services.ProductListService;
import com.company.core.services.ShopService;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;

public class ShopServiceImpl implements ShopService {
    private final Shop shop;
    private final OrderListService orderListService;
    private final ProductListService productListService;
    private final CustomerService customerService;
    private static ShopService instance;

    private ShopServiceImpl(Shop shop, OrderListService orderListService, ProductListService productListService, CustomerService customerService) {
        this.shop = shop;
        this.orderListService = orderListService;
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
        return orderListService.findByCustomer(customer).toString();
    }

    private void createOrder(Customer customer) {
        orderListService.addOrder(orderListService.createOrder(customer.getShoppingCart().getProductsFromCart(), customer));
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

    public static ShopService getInstance(Shop shop, OrderListService orderListService, ProductListService productListService, CustomerService customerService) {
        if (instance == null) {
            instance = new ShopServiceImpl(shop, orderListService, productListService, customerService);
        }
        return instance;
    }
}
