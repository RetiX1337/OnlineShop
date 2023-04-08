package com.company.core.services;

import com.company.core.lists.GoodList;
import com.company.core.lists.ProductList;
import com.company.core.models.goods.Good;
import com.company.core.models.goods.Product;
import com.company.core.models.user.customer.ShoppingCart;

import java.util.Stack;

public class ShoppingCartService {
    private final ShoppingCart shoppingCart;
    private final GoodService goodService;
    private static ShoppingCartService instance;

    private ShoppingCartService(ShoppingCart shoppingCart, GoodService goodService) {
        this.shoppingCart = shoppingCart;
        this.goodService = goodService;
    }

    public void addToCart(int productId, int amount) {
        if (shoppingCart.getGoodsCart().containsKey(goodService.getProduct(productId))) {
            if (availableOnStorage(goodService.getGoods(productId, amount), productId)) {
                goodService.getGoods(productId, amount).forEach(good -> shoppingCart.updateCart(goodService.getProduct(productId), good));
            } else {
                System.out.println("You've picked more than available on the storage");
            }
        } else {
            shoppingCart.addToCart(goodService.getProduct(productId), goodService.getGoods(productId, amount));
        }
    }

    private boolean availableOnStorage(Stack<Good> goods, int productId) {
        Product product = goods.peek().getProduct();
        return goods.size() - (shoppingCart.getCartElement(product).size() + goodService.getGoodListElementSize(productId)) >= 0;
    }

    public static ShoppingCartService getInstance(ShoppingCart shoppingCart, GoodService goodService) {
        if (instance == null) {
            instance = new ShoppingCartService(shoppingCart, goodService);
        }
        return instance;
    }
}
