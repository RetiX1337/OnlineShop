package com.company.core.services.impl;

import com.company.core.models.goods.Good;
import com.company.core.models.goods.Product;
import com.company.core.models.user.customer.ShoppingCart;

import java.util.Stack;

public class ShoppingCartServiceImpl {
    private final ShoppingCart shoppingCart;
    private final GoodListServiceImpl goodListServiceImpl;
    private static ShoppingCartServiceImpl instance;

    private ShoppingCartServiceImpl(ShoppingCart shoppingCart, GoodListServiceImpl goodListServiceImpl) {
        this.shoppingCart = shoppingCart;
        this.goodListServiceImpl = goodListServiceImpl;
    }

    public void addToCart(Long productId, int quantity) {
        if (shoppingCart.getGoodsCart().containsKey(goodListServiceImpl.getProduct(productId))) {
            if (availableOnStorage(goodListServiceImpl.getGoods(productId, quantity), productId)) {
                goodListServiceImpl.getGoods(productId, quantity).forEach(good -> shoppingCart.updateCart(goodListServiceImpl.getProduct(productId), good));
            } else {
                System.out.println("You've picked more than available on the storage");
            }
        } else {
            shoppingCart.addToCart(goodListServiceImpl.getProduct(productId), goodListServiceImpl.getGoods(productId, quantity));
        }
    }

    private boolean availableOnStorage(Stack<Good> goods, Long productId) {
        if(goods==null) return false;
        Product product = goods.peek().getProduct();
        return goods.size() - (shoppingCart.getCartElement(product).size() + goodListServiceImpl.getGoodListElementSize(productId)) >= 0;
    }

    public static ShoppingCartServiceImpl getInstance(ShoppingCart shoppingCart, GoodListServiceImpl goodListServiceImpl) {
        if (instance == null) {
            instance = new ShoppingCartServiceImpl(shoppingCart, goodListServiceImpl);
        }
        return instance;
    }
}
