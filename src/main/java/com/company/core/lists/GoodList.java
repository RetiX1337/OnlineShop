package com.company.core.lists;

import com.company.core.models.goods.Good;
import com.company.core.models.goods.Product;

import java.util.*;

public class GoodList {
    private final ProductList productList;
    private final HashMap<Long, GoodListElement> goodList = new HashMap<>();

    public GoodList(ProductList productList) {
        this.productList = productList;
    }

    public HashMap<Long, GoodListElement> getGoodList() {
        return goodList;
    }

    public Product getProduct(Long productId) {
        return productList.getProductList().get(productId);
    }

    public static class GoodListElement {
        private final Stack<Good> goodElement = new Stack<>();

        public Good push(Good good) {
            return goodElement.push(good);
        }

        public Good peek() {
            return goodElement.peek();
        }

        public Good pop() {
            return goodElement.pop();
        }

        public Stack<Good> getGoodElement() {
            return goodElement;
        }
    }
}
