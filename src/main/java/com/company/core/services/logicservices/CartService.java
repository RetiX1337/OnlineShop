package com.company.core.services.logicservices;

import com.company.core.models.EntityNotFoundException;
import com.company.core.models.user.customer.Cart;

public interface CartService {
    boolean addToCart(Cart cart, Long productId, Integer quantity) throws EntityNotFoundException;

    boolean deleteFromCart(Cart cart, Long productId, Integer quantity) throws EntityNotFoundException;
}
