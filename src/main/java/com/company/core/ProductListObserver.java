package com.company.core;

import com.company.Observer;
import com.company.core.models.goods.Product;

import java.util.ArrayList;
import java.util.List;

public class ProductListObserver {
    private final List<Observer<Product>> observers = new ArrayList<>();

    public void addObserver(Observer<Product> observer) {
        observers.add(observer);
    }

    public void removeObserver(Observer<Product> observer) {
        observers.remove(observer);
    }

    public void notifyObservers(Product product) {
        for (Observer<Product> observer : observers) {
            observer.update(product);
        }
    }
}
