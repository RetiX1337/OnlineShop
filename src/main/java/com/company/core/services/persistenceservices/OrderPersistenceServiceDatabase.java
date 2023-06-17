package com.company.core.services.persistenceservices;

import com.company.JDBCConnectionPool;
import com.company.core.models.goods.Order;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class OrderPersistenceServiceDatabase implements PersistenceInterface<Order> {

    private final JDBCConnectionPool pool;
    private final String DELETE_SQL = "DELETE FROM order WHERE order.id = ?";
    private final String UPDATE_SQL = "UPDATE product SET order.customer_id = ?, order.summary_price = ?, order_status_id = ? WHERE order.id = ?";
    private final String ALL_SQL = "SELECT * FROM order";
    private final String SAVE_SQL = "INSERT INTO order (id, customer_id, summary_price, order_status_id) VALUES (?, ?, ?, ?)";
    private final String FIND_BY_ID_SQL = "SELECT order.id, order.customer_id, order.summary_price, order_status.order_status FROM order INNER JOIN order_status ON order.order_status_id = order_status.id WHERE order.id = ?";
    private final String FIND_ALL_SQL = "SELECT order.id, order.customer_id, order.summary_price, order_status.order_status FROM order INNER JOIN order_status ON order.order_status_id = order_status.id";
    private static Long idCounter = 1L;

    public OrderPersistenceServiceDatabase(JDBCConnectionPool pool) {
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
    @Override
    public Order save(Order entity) {
        return null;
    }

    @Override
    public Order findById(Long id) {
        return null;
    }

    @Override
    public List<Order> findAll() {
        return null;
    }

    @Override
    public Order update(Order entity, Long id) {
        return null;
    }

    @Override
    public void deleteById(Long id) {

    }

    @Override
    public boolean isPresent(Long id) {
        return false;
    }
}
