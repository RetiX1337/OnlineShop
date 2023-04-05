package com.company.core.order;

import com.company.core.product.Product;
import com.company.core.good.Good;
import com.company.core.user.customer.Customer;

import java.util.HashMap;
import java.util.Stack;

public class Order {
    private final HashMap<Product, OrderElement> order = new HashMap<>();
    private final Customer customer;

    public Order(HashMap<Product, Stack<Good>> goods, Customer customer) {
        this.customer = customer;
        goods.forEach((key, value) -> putInOrder(value));
    }

    public Order(HashMap<Product, Stack<Good>> goods) {
        this.customer = null;
        goods.forEach((key, value) -> putInOrder(value));
    }

    public void putInOrder(Stack<Good> goods) {
        if (availableOnStorage(goods)) {
            if (order.containsKey(goods.peek().getProduct())) {
                for (Good good : goods) {
                    updateOrder(good);
                }
            } else {
                createOrderElement(goods);
            }
        } else {
            System.out.println("You've picked more than available on the storage");
        }
    }

    public void updateOrder(Good good) {
        order.get(good.getProduct()).push(good);
    }

    private void createOrderElement(Stack<Good> goods) {
        order.put(goods.peek().getProduct(), new OrderElement(goods));
    }

    private boolean availableOnStorage(Stack<Good> goods) {
        return order.containsKey(goods.peek().getProduct()) & order.get(goods.peek().getProduct()).size() - goods.size() <= 0;
    }

    public Customer getCustomer() {
        return customer;
    }

    private class OrderElement {
        private final Stack<Good> orderElement;

        public OrderElement(Stack<Good> goods) {
            this.orderElement = goods;
        }

        public void push(Good good) {
            orderElement.push(good);
        }

        public int size() {
            return orderElement.size();
        }

        public Product getProduct() {
            return orderElement.peek().getProduct();
        }
    }
}
