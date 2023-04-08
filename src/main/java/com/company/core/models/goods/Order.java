package com.company.core.models.goods;

import com.company.core.models.user.customer.Customer;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.HashMap;
import java.util.Stack;

public class Order {
    private final HashMap<Product, OrderElement> order = new HashMap<>();
    private final Customer customer;
    private final int ID;
    private static int idCounter = 0;

    public Order(HashMap<Product, Stack<Good>> goods, Customer customer) {
        ID = idCounter++;
        this.customer = customer;
        goods.forEach((key, value) -> addToOrder(value));
    }

    public Order(HashMap<Product, Stack<Good>> goods) {
        ID = idCounter++;
        this.customer = null;
        goods.forEach((key, value) -> addToOrder(value));
    }

    public void addToOrder(Stack<Good> goods) {
        if (order.containsKey(goods.peek().getProduct())) {
            for (Good good : goods) {
                updateOrder(good);
            }
        } else {
            createOrderElement(goods);
        }
    }

    public void updateOrder(Good good) {
        order.get(good.getProduct()).push(good);
    }

    private void createOrderElement(Stack<Good> goods) {
        order.put(goods.peek().getProduct(), new OrderElement(goods));
    }

    public Customer getCustomer() {
        return customer;
    }

    public Collection<OrderElement> getOrderElements() {
        return order.values();
    }

    public class OrderElement {
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

        public BigDecimal getPrice() {
            BigDecimal summaryPrice = new BigDecimal(0);
            for (Good good : orderElement) {
                summaryPrice = summaryPrice.add(good.getProduct().getPrice());
            }
            return summaryPrice;
        }

        @Override
        public String toString() {
            return "\nProduct: " +
                    getProduct().getName() +
                    " Amount: " +
                    size() +
                    " Summary price: " +
                    getPrice();
        }
    }
}
