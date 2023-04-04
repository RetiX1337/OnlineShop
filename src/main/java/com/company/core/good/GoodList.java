package com.company.core.good;

import com.company.core.product.Product;
import com.company.core.product.ProductList;

import java.util.*;

import static com.company.Main.scan;

public class GoodList {
    private final ProductList productList;
    private final HashMap<Product, Stack<Good>> goodList = new HashMap<>();

    public GoodList(ProductList productList) {
        this.productList = productList;
        for (int i = 0; i < productList.getProductList().size(); i++) {
            goodList.put(productList.getProductList().get(i), new Stack<>());
        }
    }

    public void fillGoodList() {
        boolean flag = true;
        while (flag) {
            System.out.println("1. Add a product" +
                    "\n2. Stop adding products");
            switch (Integer.parseInt(scan.nextLine())) {
                case 1 -> {
                    int productId, quantity;
                    productList.printProductList();
                    System.out.println("Enter product's ID you want to add: ");
                    productId = Integer.parseInt(scan.nextLine());
                    System.out.println("How many products do you want to add? ");
                    quantity = Integer.parseInt(scan.nextLine());
                    for (int i = 0; i < quantity; i++) {
                        createGood(productId);
                    }
                    printResult(quantity);
                }
                case 2 -> flag = false;
                default -> System.out.println("This case doesn't exist");
            }
        }
    }

    private void printResult(int quantity) {
        if (quantity == 1) {
            System.out.println("Product was added successfully!");
        } else {
            System.out.println("Products were added successfully!");
        }
    }

    public void printGoodList() {
        for (int i = 0; i < goodList.size(); i++) {
            System.out.println(i + ". "
                    + getProduct(i).getBrand() + " "
                    + getProduct(i).getName() + ", "
                    + getProduct(i).getPrice()
                    + " (" + goodList.get(getProduct(i)).size() + ")");
        }
    }

    private void createGood(int productId) {
        goodList.get(getProduct(productId)).push(new Good(getProduct(productId)));
    }

    public Stack<Good> getGoods(int productId, int amount) {
        Stack<Good> goods = new Stack<>();
        for (int i = 0; i < amount; i++) {
            goods.add(goodList.get(getProduct(productId)).get(i));
        }
        return goods;
    }

    public Product getProduct(int productId) {
        return productList.getProductList().get(productId);
    }

    public void delete(Product product) {
        goodList.get(product).pop();
    }
}
