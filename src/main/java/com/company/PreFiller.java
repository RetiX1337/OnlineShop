package com.company;

import com.company.core.controllers.CustomerController;
import com.company.core.controllers.ProductController;
import com.company.core.models.goods.Product;
import com.company.core.models.goods.ProductBase;
import com.company.core.models.goods.ProductWithQuantity;
import com.company.core.models.goods.Type;

import java.math.BigDecimal;

public class PreFiller {
    public static void fillProductList(ProductController productController) {
        Product baseProduct = new ProductBase("Super Platki", "Ovsianka", Type.FOOD, new BigDecimal("4.99"));
        Product decoratedProduct = new ProductWithQuantity(baseProduct, 5);


        productController.addProduct("Super Platki", "Ovsianka", Type.FOOD, new BigDecimal("4.99"), 1000);
        productController.addProduct("Platki Gorskie", "Ovsianka Dziwna", Type.FOOD, new BigDecimal("5.49"), 1000);
        productController.addProduct("Desperados", "Desperados Mochito", Type.ALCOHOL, new BigDecimal("6.49"), 1000);
        productController.addProduct("Miekka dupa", "Rumianek", Type.HOUSEHOLD, new BigDecimal("10.99"), 1000);
        productController.addProduct("Coca-Cola", "Coca-Cola", Type.BEVERAGE, new BigDecimal("4.49"), 1000);
    }

    public static void fillCustomerList(CustomerController customerController) {

    }
}
