package com.company;

import com.company.user.customer.Customer;

import java.util.ArrayList;
import java.util.Stack;

import static com.company.Main.scan;

public class Shop {
    private final ItemList itemList;
    private final GoodList goodList;

    public Shop(ItemList itemList, GoodList goodList) {
        this.itemList = itemList;
        this.goodList = goodList;
    }

    public void shop(Customer customer) {
        int itemId;
        int amount;
        goodList.printGoodList();
        System.out.println("Choose the product: ");
        itemId = Integer.parseInt(scan.nextLine());
        System.out.println("Enter the amount: ");
        amount = Integer.parseInt(scan.nextLine());
        Stack<Good> goods = goodList.getGoods(itemId, amount);
        for (int i = 0; i < goods.size(); i++) {
            customer.getShoppingCart().putInCart(goods.elementAt(i));
        }
    }

    public void sell(Customer customer) {
        ArrayList<Good> goods = customer.getShoppingCart().getGoodsCart();
        for (int i = 0; i < goods.size(); i++) {
            goodList.delete(goods.get(i).getItem());
        }
    }
}
