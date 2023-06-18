package com.company.core.services.persistenceservices;

import com.company.JDBCConnectionPool;
import com.company.core.models.user.customer.Customer;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;

public class ProductStoragePersistenceService {
    private final JDBCConnectionPool pool;
    private Long idCounter;
    private Properties sqlProps;

    {
        sqlProps = new Properties();
        try {
            sqlProps.load(new FileInputStream("src\\main\\java\\com\\company\\core\\sql_queries\\product.properties"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    private final String GET_QUANTITY_SQL = sqlProps.getProperty("GET_QUANTITY_SQL");
    private final String UPDATE_QUANTITY_SQL = sqlProps.getProperty("UPDATE_QUANTITY_SQL");
    private final String ALL_SQL = sqlProps.getProperty("ALL_SQL");
    public ProductStoragePersistenceService(JDBCConnectionPool pool) {
        this.pool = pool;
        initCounter();
    }

    private void initCounter() {
        try {
            Connection con = pool.checkOut();
            ResultSet rs = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY).executeQuery(ALL_SQL);
            rs.last();
            idCounter = Long.valueOf(rs.getInt("id")) + 1;
            pool.checkIn(con);
            System.out.println(idCounter);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public Integer getQuantity(Long storageId, Long productId) {
        try {
            Connection con = pool.checkOut();
            PreparedStatement prep = con.prepareStatement(GET_QUANTITY_SQL);
            prep.setLong(1, storageId);
            prep.setLong(2, productId);
            ResultSet rs = prep.executeQuery();
            rs.next();
            Integer quantity = rs.getInt(1);
            pool.checkIn(con);
            return quantity;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean updateQuantity(Integer quantity, Long storageId, Long productId) {
        try {
            Connection con = pool.checkOut();
            PreparedStatement prep = con.prepareStatement(UPDATE_QUANTITY_SQL);
            prep.setInt(1, quantity);
            prep.setLong(2, storageId);
            prep.setLong(3, productId);
            prep.executeUpdate();
            pool.checkIn(con);
            return true;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


}
