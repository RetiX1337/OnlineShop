package com.company.core.services.persistenceservices.dbimpl;

import com.company.JDBCConnectionPool;
import com.company.core.models.EntityNotFoundException;
import com.company.core.models.EntityNotSavedException;
import com.company.core.models.goods.*;
import com.company.core.services.persistenceservices.PersistenceInterface;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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
        Connection connection = pool.checkOut();
        try {
            pool.startTransaction(connection);
            PreparedStatement preparedStatement = connection.prepareStatement(SAVE_SQL, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setLong(1, entity.getProduct().getId());
            preparedStatement.setInt(2, entity.getQuantity());
            preparedStatement.setBigDecimal(3, entity.getPrice());
            preparedStatement.setLong(4, entity.getOrder().getId());
            preparedStatement.executeUpdate();
            pool.commitTransaction(connection);

            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            resultSet.next();
            entity.setId(resultSet.getLong(1));
            return entity;
        } catch (SQLException e) {
            throw new EntityNotSavedException();
        } finally {
            pool.checkIn(connection);
        }
    }

    @Override
    public Item findById(Long id) {
        Connection connection = pool.checkOut();
        try {
            pool.startTransaction(connection);
            PreparedStatement preparedStatement = connection.prepareStatement(FIND_BY_ID_SQL);
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            pool.commitTransaction(connection);
            resultSet.next();
            Item item = mapItem(resultSet);
            return item;
        } catch (SQLException e) {
            throw new EntityNotFoundException();
        } finally {
            pool.checkIn(connection);
        }
    }

    @Override
    public List<Item> findAll() {
        Connection connection = pool.checkOut();
        try {
            pool.startTransaction(connection);
            List<Item> itemList = new ArrayList<>();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(FIND_ALL_SQL);
            pool.commitTransaction(connection);
            while (resultSet.next()) {
                Item item = mapItem(resultSet);
                itemList.add(item);
            }
            return itemList;
        } catch (SQLException e) {
            throw new EntityNotFoundException();
        } finally {
            pool.checkIn(connection);
        }
    }

    @Override
    public Item update(Item entity) {
        Connection connection = pool.checkOut();
        try {
            pool.startTransaction(connection);
            PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_SQL);
            preparedStatement.setLong(1, entity.getProduct().getId());
            preparedStatement.setInt(2, entity.getQuantity());
            preparedStatement.setBigDecimal(3, entity.getPrice());
            preparedStatement.setLong(4, entity.getOrder().getId());
            preparedStatement.setLong(5, entity.getId());
            preparedStatement.executeUpdate();
            pool.commitTransaction(connection);
            return entity;
        } catch (SQLException e) {
            throw new EntityNotFoundException();
        } finally {
            pool.checkIn(connection);
        }
    }

    @Override
    public void deleteById(Long id) {
        Connection connection = pool.checkOut();
        try {
            pool.startTransaction(connection);
            PreparedStatement preparedStatement = connection.prepareStatement(DELETE_SQL);
            preparedStatement.setLong(1, id);
            preparedStatement.executeUpdate();
            pool.commitTransaction(connection);
        } catch (SQLException e) {
            throw new EntityNotFoundException();
        } finally {
            pool.checkIn(connection);
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

    public Item mapItem(ResultSet resultSet) {
        try {
            Item item = new Item();

            item.setId(resultSet.getLong("id"));
            item.setProduct(productPersistenceService.findById(resultSet.getLong("product_id")));
            item.setQuantity(resultSet.getInt("quantity"));
            item.setPrice(resultSet.getBigDecimal("price"));

            return item;
        } catch (SQLException e) {
            throw new EntityNotFoundException();
        }
    }
}
