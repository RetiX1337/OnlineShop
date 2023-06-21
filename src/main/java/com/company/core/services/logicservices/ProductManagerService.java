package com.company.core.services.logicservices;

import com.company.core.models.EntityNotFoundException;
import com.company.core.models.goods.ProductWithQuantity;

public interface ProductManagerService {
    ProductWithQuantity getProductWithQuantity(Long shopId, Long productId) throws EntityNotFoundException;
    boolean removeFromStorage(Integer quantity, Long shopId, Long productId);
}
