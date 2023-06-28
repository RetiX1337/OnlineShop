package com.company.core.services.persistenceservices.dbimpl;

import com.company.JDBCConnectionPool;
import com.company.core.models.goods.*;
import com.company.core.services.persistenceservices.PersistenceInterface;

import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class ItemPersistenceServiceDatabase implements PersistenceInterface<Item> {
    private final JDBCConnectionPool pool;
    private final PersistenceInterface<Product> productPersistenceService;
    private final String DELETE_SQL = "DELETE FROM item WHERE item.id = ?";
    private final String UPDATE_SQL = "UPDATE item SET item.product_id = ?, item.quantity = ?, item.price = ?, item.order_id = ? WHERE item.id = ?";
    private final String ALL_SQL = "SELECT * FROM item";
    private final String SAVE_SQL = "INSERT INTO item (id, product_id, quantity, price, order_id) VALUES (?, ?, ?, ?, ?)";
    private final String FIND_BY_ID_SQL = "SELECT item.id, item.product_id, item.quantity, item.price, item.order_id FROM item WHERE item.id = ?";
    private final String FIND_ALL_SQL = "SELECT item.id, item.product_id, item.quantity, item.price, item.order_id FROM item";

    private Long idCounter;

    public ItemPersistenceServiceDatabase(JDBCConnectionPool pool, PersistenceInterface<Product> productPersistenceService) {
        this.productPersistenceService = productPersistenceService;
        this.pool = pool;
        initCounter();
    }

    private void initCounter() {
        try {
            Connection con = pool.checkOut();
            ResultSet rs = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY).executeQuery(ALL_SQL);
            if (rs.isBeforeFirst()) {
                rs.last();
                idCounter = Long.valueOf(rs.getInt("id")) + 1;
                pool.checkIn(con);
            } else {
                idCounter = 1L;
            }
            pool.checkIn(con);
            System.out.println(idCounter);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    public Item save(Item entity) {
        entity.setId(idCounter);
        try {
            Connection con = pool.checkOut();
            PreparedStatement prep = con.prepareStatement(SAVE_SQL);
            prep.setLong(1, entity.getId());
            prep.setLong(2, entity.getProduct().getId());
            prep.setInt(3, entity.getQuantity());
            prep.setBigDecimal(4, entity.getPrice());
            prep.setLong(5, entity.getOrderId());
            prep.executeUpdate();
            pool.checkIn(con);
            idCounter++;
            return entity;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Item findById(Long id) {
        try {
            Connection con = pool.checkOut();
            PreparedStatement p = con.prepareStatement(FIND_BY_ID_SQL);
            p.setLong(1, id);
            ResultSet rs = p.executeQuery();
            rs.next();
            Long productId = rs.getLong("product_id");
            Integer quantity = rs.getInt("quantity");
            BigDecimal price = rs.getBigDecimal("price");
            Long orderId = rs.getLong("order_id");

            Product product = productPersistenceService.findById(productId);

            pool.checkIn(con);
            return new Item(id, product, quantity, price, orderId);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Item> findAll() {
        try {
            List<Item> itemList = new ArrayList<>();
            Connection con = pool.checkOut();
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(FIND_ALL_SQL);
            while (rs.next()) {
                Long id = rs.getLong("id");
                Long productId = rs.getLong("product_id");
                Integer quantity = rs.getInt("quantity");
                BigDecimal price = rs.getBigDecimal("price");
                Long orderId = rs.getLong("order_id");

                Product product = productPersistenceService.findById(productId);
                itemList.add(new Item(id, product, quantity, price, orderId));
            }
            pool.checkIn(con);
            return itemList;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Item update(Item entity, Long id) {
        try {
            Connection con = pool.checkOut();
            PreparedStatement prep = con.prepareStatement(UPDATE_SQL);
            prep.setLong(1, entity.getProduct().getId());
            prep.setInt(2, entity.getQuantity());
            prep.setBigDecimal(3, entity.getPrice());
            prep.setLong(4, entity.getOrderId());
            prep.setLong(5, id);
            prep.executeUpdate();
            entity.setId(id);
            pool.checkIn(con);
            return entity;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteById(Long id) {
        try {
            Connection con = pool.checkOut();
            PreparedStatement prep = con.prepareStatement(DELETE_SQL);
            prep.setLong(1, id);
            prep.executeUpdate();
            pool.checkIn(con);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean isPresent(Long id) {
        try {
            Connection con = pool.checkOut();
            PreparedStatement prep = con.prepareStatement(FIND_BY_ID_SQL);
            prep.setLong(1, id);
            ResultSet rs = prep.executeQuery();
            pool.checkIn(con);
            return rs.isBeforeFirst();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
