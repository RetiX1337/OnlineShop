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
    private final String SAVE_SQL = "INSERT INTO item (product_id, quantity, price, order_id) VALUES (?, ?, ?, ?)";
    private final String FIND_BY_ID_SQL = "SELECT item.id, item.product_id, item.quantity, item.price, item.order_id FROM item WHERE item.id = ?";
    private final String FIND_ALL_SQL = "SELECT item.id, item.product_id, item.quantity, item.price, item.order_id FROM item";


    public ItemPersistenceServiceDatabase(JDBCConnectionPool pool, PersistenceInterface<Product> productPersistenceService) {
        this.productPersistenceService = productPersistenceService;
        this.pool = pool;
    }

    @Override
    public Item save(Item entity) {
        try {
            Connection con = pool.checkOut();
            PreparedStatement prep = con.prepareStatement(SAVE_SQL, Statement.RETURN_GENERATED_KEYS);
            prep.setLong(1, entity.getProduct().getId());
            prep.setInt(2, entity.getQuantity());
            prep.setBigDecimal(3, entity.getPrice());
            prep.setLong(4, entity.getOrderId());
            prep.executeUpdate();

            ResultSet rs = prep.getGeneratedKeys();
            rs.next();
            entity.setId(rs.getLong(1));
            pool.checkIn(con);
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
            Item item = mapItem(rs);
            pool.checkIn(con);
            return item;
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
                Item item = mapItem(rs);
                itemList.add(item);
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

    public Item mapItem(ResultSet rs) {
        try {
            Long id = rs.getLong("id");
            Long productId = rs.getLong("product_id");
            Integer quantity = rs.getInt("quantity");
            BigDecimal price = rs.getBigDecimal("price");
            Long orderId = rs.getLong("order_id");

            Product product = productPersistenceService.findById(productId);

            return new Item(id, product, quantity, price, orderId);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
