package com.company.core.services.persistenceservices.dbimpl;

import com.company.JDBCConnectionPool;
import com.company.core.models.goods.Product;
import com.company.core.models.goods.ProductBase;
import com.company.core.models.goods.ProductType;
import com.company.core.models.user.customer.Customer;
import com.company.core.services.persistenceservices.PersistenceInterface;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CustomerPersistenceServiceDatabase implements PersistenceInterface<Customer> {
    private final JDBCConnectionPool pool;
    private final PersistenceInterface<Customer> customerPersistenceService;
    private final String DELETE_SQL = "DELETE FROM customer WHERE customer.id = ?";
    private final String UPDATE_SQL = "UPDATE customer SET username = ?, email = ?, password = ?, wallet = ? WHERE customer.id = ?";
    private final String ALL_SQL = "SELECT * FROM customer";
    private final String SAVE_SQL = "INSERT INTO customer (id, username, email, password, wallet) VALUES (?, ?, ?, ?, ?)";
    private final String FIND_BY_ID_SQL = "SELECT customer.id, customer.username, customer.email, customer.password, customer.wallet WHERE customer.id = ?";
    private final String FIND_ALL_SQL = "SELECT customer.id, customer.username, customer.email, customer.password, customer.wallet FROM customer";

    private Long idCounter;

    public CustomerPersistenceServiceDatabase(JDBCConnectionPool pool, PersistenceInterface<Customer> customerPersistenceService) {
        this.customerPersistenceService = customerPersistenceService;
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
    public Customer save(Customer entity) {
        entity.setId(idCounter);
        try {
            Connection con = pool.checkOut();
            PreparedStatement prep = con.prepareStatement(SAVE_SQL);
            prep.setLong(1, entity.getId());
            prep.setString(2, entity.getUsername());
            prep.setString(3, entity.getEmail());
            prep.setString(4, entity.getPassword());
            prep.setBigDecimal(5, entity.getWallet());
            prep.executeUpdate();
            pool.checkIn(con);
            idCounter++;
            return entity;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Customer findById(Long id) {
        try {
            Connection con = pool.checkOut();
            PreparedStatement p = con.prepareStatement(FIND_BY_ID_SQL);
            p.setLong(1, id);
            ResultSet rs = p.executeQuery();
            rs.next();
            String username = rs.getString("username");
            String email = rs.getString("email");
            String password = rs.getString("password");
            BigDecimal wallet = rs.getBigDecimal("wallet");
            pool.checkIn(con);
            return new Customer(id, username, email, password, wallet);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Customer> findAll() {
        try {
            List<Customer> customerList = new ArrayList<>();
            Connection con = pool.checkOut();
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(FIND_ALL_SQL);
            while (rs.next()) {
                Long id = rs.getLong("id");
                String username = rs.getString("username");
                String email = rs.getString("email");
                String password = rs.getString("password");
                BigDecimal wallet = rs.getBigDecimal("wallet");
                customerList.add(new Customer(id, username, email, password, wallet));
            }
            pool.checkIn(con);
            return customerList;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Customer update(Customer entity, Long id) {
        try {
            Connection con = pool.checkOut();
            PreparedStatement prep = con.prepareStatement(UPDATE_SQL);
            prep.setString(1, entity.getUsername());
            prep.setString(2, entity.getEmail());
            prep.setString(3, entity.getPassword());
            prep.setBigDecimal(4, entity.getWallet());
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
