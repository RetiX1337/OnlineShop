package com.company.core.services.persistenceservices.dbimpl;

import com.company.JDBCConnectionPool;
import com.company.core.models.EntityNotFoundException;
import com.company.core.models.goods.*;
import com.company.core.services.persistenceservices.PersistenceInterface;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

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
        Connection con = pool.checkOut();
        try {
            pool.startTransaction(con);
            PreparedStatement prep = con.prepareStatement(SAVE_SQL, Statement.RETURN_GENERATED_KEYS);
            prep.setLong(1, entity.getProduct().getId());
            prep.setInt(2, entity.getQuantity());
            prep.setBigDecimal(3, entity.getPrice());
            prep.setLong(4, entity.getOrderId());
            prep.executeUpdate();
            pool.commitTransaction(con);

            ResultSet rs = prep.getGeneratedKeys();
            rs.next();
            entity.setId(rs.getLong(1));
            return entity;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            pool.checkIn(con);
        }
    }

    @Override
    public Item findById(Long id) {
        Connection con = pool.checkOut();
        try {
            pool.startTransaction(con);
            PreparedStatement p = con.prepareStatement(FIND_BY_ID_SQL);
            p.setLong(1, id);
            ResultSet rs = p.executeQuery();
            pool.commitTransaction(con);
            rs.next();
            Item item = mapItem(rs);
            return item;
        } catch (SQLException e) {
            throw new EntityNotFoundException();
        } finally {
            pool.checkIn(con);
        }
    }

    @Override
    public List<Item> findAll() {
        Connection con = pool.checkOut();
        try {
            pool.startTransaction(con);
            List<Item> itemList = new ArrayList<>();
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(FIND_ALL_SQL);
            pool.commitTransaction(con);
            while (rs.next()) {
                Item item = mapItem(rs);
                itemList.add(item);
            }
            return itemList;
        } catch (SQLException e) {
            throw new EntityNotFoundException();
        } finally {
            pool.checkIn(con);
        }
    }

    @Override
    public Item update(Item entity) {
        Connection con = pool.checkOut();
        try {
            pool.startTransaction(con);
            PreparedStatement prep = con.prepareStatement(UPDATE_SQL);
            prep.setLong(1, entity.getProduct().getId());
            prep.setInt(2, entity.getQuantity());
            prep.setBigDecimal(3, entity.getPrice());
            prep.setLong(4, entity.getOrderId());
            prep.setLong(5, entity.getId());
            prep.executeUpdate();
            pool.commitTransaction(con);
            return entity;
        } catch (SQLException e) {
            throw new EntityNotFoundException();
        } finally {
            pool.checkIn(con);
        }
    }

    @Override
    public void deleteById(Long id) {
        Connection con = pool.checkOut();
        try {
            pool.startTransaction(con);
            PreparedStatement prep = con.prepareStatement(DELETE_SQL);
            prep.setLong(1, id);
            prep.executeUpdate();
            pool.commitTransaction(con);
        } catch (SQLException e) {
            throw new EntityNotFoundException();
        } finally {
            pool.checkIn(con);
        }
    }

    @Override
    public boolean isPresent(Long id) {
        try {
            findById(id);
            return true;
        } catch (EntityNotFoundException e) {
            return false;
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
            throw new EntityNotFoundException();
        }
    }
}
