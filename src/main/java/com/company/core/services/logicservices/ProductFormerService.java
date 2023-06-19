package com.company.core.services.logicservices;

import com.company.core.models.EntityNotFoundException;
import com.company.core.models.goods.ProductWithQuantity;

public interface ProductFormerService {
    ProductWithQuantity getProductWithQuantity(Long productId) throws EntityNotFoundException;
}
