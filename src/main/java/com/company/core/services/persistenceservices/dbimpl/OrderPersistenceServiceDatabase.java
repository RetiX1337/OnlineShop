package com.company.core.services.persistenceservices.dbimpl;

import com.company.JDBCConnectionPool;
import com.company.core.exceptions.EntityNotFoundException;
import com.company.core.exceptions.EntityNotSavedException;
import com.company.core.models.goods.*;
import com.company.core.models.user.User;
import com.company.core.services.persistenceservices.PersistenceInterface;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

public class OrderPersistenceServiceDatabase implements PersistenceInterface<Order> {
    private final JDBCConnectionPool pool;
    private final PersistenceInterface<Item> itemPersistenceService;
    private final PersistenceInterface<User> userPersistenceService;
    private final String DELETE_SQL = "DELETE FROM orders WHERE orders.id = ?";
    private final String UPDATE_SQL = "UPDATE orders SET orders.user_id = ?, orders.summary_price = ?, orders.order_status = ? WHERE orders.id = ?";
    private final String ALL_SQL = "SELECT * FROM orders";
    private final String SAVE_SQL = "INSERT INTO orders (user_id, summary_price, order_status) VALUES (?, ?, ?)";
    private final String FIND_BY_ID_SQL = "SELECT orders.id, orders.user_id, orders.summary_price, orders.order_status FROM orders WHERE orders.id = ?";
    private final String FIND_ALL_SQL = "SELECT orders.id, orders.user_id, orders.summary_price, orders.order_status FROM orders";
    private final String GET_ITEMS_BY_ORDER = "SELECT id FROM item WHERE order_id = ?";

    public OrderPersistenceServiceDatabase(JDBCConnectionPool pool, PersistenceInterface<Item> itemPersistenceService, PersistenceInterface<User> userPersistenceService) {
        this.pool = pool;
        this.itemPersistenceService = itemPersistenceService;
        this.userPersistenceService = userPersistenceService;
    }

    @Override
    public Order save(Order entity) {
        Connection connection = pool.checkOut();
        try {
            pool.startTransaction(connection);
            PreparedStatement preparedStatement = connection.prepareStatement(SAVE_SQL, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setLong(1, entity.getUser().getId());
            preparedStatement.setBigDecimal(2, entity.getSummaryPrice());
            preparedStatement.setString(3, entity.getOrderStatus().name());
            preparedStatement.executeUpdate();
            pool.commitTransaction(connection);

            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            resultSet.next();
            entity.setId(resultSet.getLong(1));

            entity.getItems().forEach(item -> {
                item.setOrder(entity);
                itemPersistenceService.save(item);
            });

            return entity;
        } catch (SQLException e) {
            throw new EntityNotSavedException();
        } finally {
            pool.checkIn(connection);
        }
    }

    @Override
    public Order findById(Long id) {
        Connection connection = pool.checkOut();
        try {
            pool.startTransaction(connection);
            PreparedStatement preparedStatement = connection.prepareStatement(FIND_BY_ID_SQL);
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            pool.commitTransaction(connection);
            resultSet.next();
            Order order = mapOrder(resultSet);
            return order;
        } catch (SQLException e) {
            throw new EntityNotFoundException();
        } finally {
            pool.checkIn(connection);
        }
    }

    @Override
    public List<Order> findAll() {
        Connection connection = pool.checkOut();
        try {
            pool.startTransaction(connection);
            List<Order> orderList = new ArrayList<>();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(FIND_ALL_SQL);
            pool.commitTransaction(connection);
            while (resultSet.next()) {
                Order order = mapOrder(resultSet);
                orderList.add(order);
            }
            return orderList;
        } catch (SQLException e) {
            throw new EntityNotFoundException();
        } finally {
            pool.checkIn(connection);
        }
    }

    @Override
    public Order update(Order entity) {
        Connection connection = pool.checkOut();
        try {
            pool.startTransaction(connection);
            PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_SQL);
            preparedStatement.setLong(1, entity.getUser().getId());
            preparedStatement.setBigDecimal(2, entity.getSummaryPrice());
            preparedStatement.setLong(3, entity.getOrderStatus().ordinal()+1);
            preparedStatement.setLong(4, entity.getId());

            Set<Item> oldItems = getItemsByOrder(entity.getId());
            Set<Item> newItems = entity.getItems();

            addNewItems(newItems, oldItems);
            deleteOldItems(newItems, oldItems);

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

    private void deleteOldItems(Set<Item> newItems, Set<Item> oldItems) {
        for (Item item : oldItems) {
            if (!newItems.contains(item)) {
                itemPersistenceService.deleteById(item.getId());
            }
        }
    }

    private void addNewItems(Set<Item> newItems, Set<Item> oldItems) {
        for (Item item : newItems) {
            if (!oldItems.contains(item)) {
                itemPersistenceService.save(item);
            }
        }
    }

    private Set<Item> getItemsByOrder(Long orderId) {
        Connection connection = pool.checkOut();
        try {
            pool.startTransaction(connection);
            Set<Item> items = new HashSet<>();
            PreparedStatement preparedStatement = connection.prepareStatement(GET_ITEMS_BY_ORDER);
            preparedStatement.setLong(1, orderId);
            ResultSet resultSet = preparedStatement.executeQuery();
            pool.commitTransaction(connection);
            while (resultSet.next()) {
                Item item = itemPersistenceService.findById(resultSet.getLong(1));
                items.add(item);
            }
            return items;
        } catch (SQLException e) {
            throw new EntityNotFoundException();
        } finally {
            pool.checkIn(connection);
        }
    }

    public Order mapOrder(ResultSet resultSet) {
        try {
            Order order = new Order();

            order.setId(resultSet.getLong("id"));
            order.setUser(userPersistenceService.findById(resultSet.getLong("user_id")));
            order.setSummaryPrice(resultSet.getBigDecimal("summary_price"));
            order.setOrderStatus(OrderStatus.valueOf(resultSet.getString("order_status")));
            Set<Item> items = getItemsByOrder(order.getId());
            items.forEach(item -> item.setOrder(order));
            order.setItems(items);

            return order;
        } catch (SQLException e) {
            throw new EntityNotFoundException();
        }
    }
}
