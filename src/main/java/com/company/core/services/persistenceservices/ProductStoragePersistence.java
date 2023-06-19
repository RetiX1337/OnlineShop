package com.company.core.services.persistenceservices;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public interface ProductStoragePersistence {
    Integer getQuantity(Long storageId, Long productId);
    void updateQuantity(Integer quantity, Long storageId, Long productId);
    void addQuantity(Integer quantity, Long storageId, Long productId);
    boolean isPresent(Long storageId, Long productId);
}
