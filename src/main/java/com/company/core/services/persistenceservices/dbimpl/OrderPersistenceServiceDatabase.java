package com.company.core.services.persistenceservices.dbimpl;

import com.company.JDBCConnectionPool;
import com.company.core.models.EntityNotFoundException;
import com.company.core.models.Shop;
import com.company.core.models.goods.*;
import com.company.core.models.user.customer.Customer;
import com.company.core.services.persistenceservices.PersistenceInterface;

import java.math.BigDecimal;
import java.sql.*;
import java.util.*;

public class OrderPersistenceServiceDatabase implements PersistenceInterface<Order> {
    private final JDBCConnectionPool pool;
    private final PersistenceInterface<Item> itemPersistenceService;
    private final PersistenceInterface<Customer> customerPersistenceService;
    private final String DELETE_SQL = "DELETE FROM online_shop.order WHERE online_shop.order.id = ?";
    private final String UPDATE_SQL = "UPDATE product SET online_shop.order.customer_id = ?, online_shop.order.summary_price = ?, order_status_id = ? WHERE online_shop.order.id = ?";
    private final String ALL_SQL = "SELECT * FROM online_shop.order";
    private final String SAVE_SQL = "INSERT INTO online_shop.order (customer_id, summary_price, order_status_id) VALUES (?, ?, ?)";
    private final String FIND_BY_ID_SQL = "SELECT online_shop.order.id, online_shop.order.customer_id, online_shop.order.summary_price, order_status.order_status FROM online_shop.order INNER JOIN order_status ON online_shop.order.order_status_id = order_status.id WHERE online_shop.order.id = ?";
    private final String FIND_ALL_SQL = "SELECT online_shop.order.id, online_shop.order.customer_id, online_shop.order.summary_price, order_status.order_status FROM online_shop.order INNER JOIN order_status ON online_shop.order.order_status_id = order_status.id";
    private final String GET_ITEMS_BY_ORDER = "SELECT id FROM item WHERE order_id = ?";

    public OrderPersistenceServiceDatabase(JDBCConnectionPool pool, PersistenceInterface<Item> itemPersistenceService, PersistenceInterface<Customer> customerPersistenceService) {
        this.pool = pool;
        this.itemPersistenceService = itemPersistenceService;
        this.customerPersistenceService = customerPersistenceService;
    }

    @Override
    public Order save(Order entity) {
        try {
            Connection con = pool.checkOut();
            PreparedStatement prep = con.prepareStatement(SAVE_SQL, Statement.RETURN_GENERATED_KEYS);
            prep.setLong(1, entity.getCustomer().getId());
            prep.setBigDecimal(2, entity.getSummaryPrice());
            prep.setLong(3, entity.getOrderStatus().ordinal()+1);
            prep.executeUpdate();

            ResultSet rs = prep.getGeneratedKeys();
            rs.next();
            entity.setId(rs.getLong(1));

            entity.getItems().forEach(item -> {
                item.setOrderId(entity.getId());
                itemPersistenceService.save(item);
            });

            pool.checkIn(con);
            return entity;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Order findById(Long id) {
        try {
            Connection con = pool.checkOut();
            PreparedStatement p = con.prepareStatement(FIND_BY_ID_SQL);
            p.setLong(1, id);
            ResultSet rs = p.executeQuery();
            rs.next();
            Order order = mapOrder(rs);
            pool.checkIn(con);
            return order;
        } catch (SQLException e) {
            throw new EntityNotFoundException();
        }
    }

    @Override
    public List<Order> findAll() {
        try {
            List<Order> orderList = new ArrayList<>();
            Connection con = pool.checkOut();
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(FIND_ALL_SQL);
            while (rs.next()) {
                Order order = mapOrder(rs);
                orderList.add(order);
            }
            pool.checkIn(con);
            return orderList;
        } catch (SQLException e) {
            throw new EntityNotFoundException();
        }
    }

    @Override
    public Order update(Order entity) {
        try {
            Connection con = pool.checkOut();
            PreparedStatement prep = con.prepareStatement(UPDATE_SQL);
            prep.setLong(1, entity.getCustomer().getId());
            prep.setBigDecimal(2, entity.getSummaryPrice());
            prep.setLong(3, entity.getOrderStatus().ordinal()+1);
            prep.setLong(4, entity.getId());

            List<Item> oldItems = getItemsByOrder(entity.getId());
            List<Item> newItems = entity.getItems();

            addNewItems(newItems, oldItems);
            deleteOldItems(newItems, oldItems);

            prep.executeUpdate();
            entity.setId(entity.getId());
            pool.checkIn(con);
            return entity;
        } catch (SQLException e) {
            throw new EntityNotFoundException();
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
            throw new EntityNotFoundException();
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

    private void deleteOldItems(List<Item> newItems, List<Item> oldItems) {
        for (Item item : oldItems) {
            if (!newItems.contains(item)) {
                itemPersistenceService.deleteById(item.getId());
            }
        }
    }

    private void addNewItems(List<Item> newItems, List<Item> oldItems) {
        for (Item item : newItems) {
            if (!oldItems.contains(item)) {
                itemPersistenceService.save(item);
            }
        }
    }

    private List<Item> getItemsByOrder(Long orderId) {
        try {
            List<Item> items = new LinkedList<>();
            Connection con = pool.checkOut();
            PreparedStatement p = con.prepareStatement(GET_ITEMS_BY_ORDER);
            p.setLong(1, orderId);
            ResultSet rs = p.executeQuery();
            while (rs.next()) {
                Item item = itemPersistenceService.findById(rs.getLong(1));
                items.add(item);
            }
            pool.checkIn(con);
            return items;
        } catch (SQLException e) {
            throw new EntityNotFoundException();
        }
    }

    public Order mapOrder(ResultSet rs) {
        try {
            Long id = rs.getLong("id");
            Long customerId = rs.getLong("customer_id");
            Customer customer = customerPersistenceService.findById(customerId);
            BigDecimal summaryPrice = rs.getBigDecimal("summary_price");
            OrderStatus orderStatus = OrderStatus.valueOf(rs.getString("order_status"));
            Collection<Item> items = getItemsByOrder(id);
            return new Order(id, items, customer, summaryPrice, orderStatus);
        } catch (SQLException e) {
            throw new EntityNotFoundException();
        }
    }
}
