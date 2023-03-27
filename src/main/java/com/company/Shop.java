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

    public void putInCart(Customer customer, int itemId, int amount) {
        Stack<Good> goods = goodList.getGoods(itemId, amount);
        for (int i = 0; i < goods.size(); i++) {
            customer.getShoppingCart().putInCart(goods.elementAt(i));
        }
    }

    public void sell(Customer customer) {
        ArrayList<Good> goods = customer.getShoppingCart().getGoodsCart();
        for (Good good : goods) {
            goodList.delete(good.getItem());
        }
    }

    public GoodList getGoodList() {
        return goodList;
    }
}
