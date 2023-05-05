package com.company.core.services.impl;

import com.company.core.Shop;
import com.company.core.models.goods.Order;
import com.company.core.models.goods.OrderStatus;
import com.company.core.models.goods.Product;
import com.company.core.models.user.customer.Customer;
import com.company.core.services.CustomerService;
import com.company.core.services.OrderService;
import com.company.core.services.ProductListService;
import com.company.core.services.ShopService;

import java.math.BigDecimal;
import java.util.List;

public class ShopServiceImpl implements ShopService {
    private final Shop shop;
    private final OrderService orderService;
    private final ProductListService productListService;
    private final CustomerService customerService;

    public ShopServiceImpl(Shop shop, OrderService orderService, ProductListService productListService, CustomerService customerService) {
        this.shop = shop;
        this.orderService = orderService;
        this.productListService = productListService;
        this.customerService = customerService;
    }

    @Override
    public boolean checkoutCart(Customer customer) {
        Order order = orderService.createOrder(customer.getShoppingCart().getProductsFromCart(), customer);
        order.setOrderStatus(OrderStatus.NEW);
        if (processPayment(customer, order)) {
            order.setOrderStatus(OrderStatus.PAID);
            orderService.addOrder(order);
            customer.getShoppingCart().clear();
            return true;
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
        return orderService.findByCustomer(customer).toString();
    }

    private boolean processPayment(Customer customer, Order order) {
        if (enoughMoney(customer.getWallet(), order.getSummaryPrice())) {
            customer.setWallet(customer.getWallet().subtract(order.getSummaryPrice()));
            return true;
        }
        return false;
    }

    private boolean enoughMoney(BigDecimal wallet, BigDecimal summaryPrice) {
        return summaryPrice.compareTo(wallet) == 0 || summaryPrice.compareTo(wallet) == -1;
    }
}
