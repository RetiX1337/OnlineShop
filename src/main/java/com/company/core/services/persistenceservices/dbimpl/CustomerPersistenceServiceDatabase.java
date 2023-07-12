package com.company.core.services.persistenceservices.dbimpl;

import com.company.JDBCConnectionPool;
import com.company.core.models.EntityNotFoundException;
import com.company.core.models.user.customer.Customer;
import com.company.core.services.persistenceservices.PersistenceInterface;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CustomerPersistenceServiceDatabase implements PersistenceInterface<Customer> {
    private final JDBCConnectionPool pool;
    private final String DELETE_SQL = "DELETE FROM customer WHERE customer.id = ?";
    private final String UPDATE_SQL = "UPDATE customer SET username = ?, email = ?, password = ?, wallet = ? WHERE customer.id = ?";
    private final String ALL_SQL = "SELECT * FROM customer";
    private final String SAVE_SQL = "INSERT INTO customer (username, email, password, wallet) VALUES (?, ?, ?, ?)";
    private final String FIND_BY_ID_SQL = "SELECT customer.id, customer.username, customer.email, customer.password, customer.wallet FROM customer WHERE customer.id = ?";
    private final String FIND_ALL_SQL = "SELECT customer.id, customer.username, customer.email, customer.password, customer.wallet FROM customer";

    public CustomerPersistenceServiceDatabase(JDBCConnectionPool pool) {
        this.pool = pool;
    }

    @Override
    public Customer save(Customer entity) {
        try {
            Connection con = pool.checkOut();
            PreparedStatement prep = con.prepareStatement(SAVE_SQL, Statement.RETURN_GENERATED_KEYS);
            prep.setString(1, entity.getUsername());
            prep.setString(2, entity.getEmail());
            prep.setString(3, entity.getPassword());
            prep.setBigDecimal(4, entity.getWallet());
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
    public Customer findById(Long id) {
        try {
            Connection con = pool.checkOut();
            PreparedStatement p = con.prepareStatement(FIND_BY_ID_SQL);
            p.setLong(1, id);
            ResultSet rs = p.executeQuery();
            rs.next();
            Customer customer = mapCustomer(rs);
            pool.checkIn(con);
            return customer;
        } catch (SQLException e) {
            throw new EntityNotFoundException();
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
                Customer customer = mapCustomer(rs);
                customerList.add(customer);
            }
            pool.checkIn(con);
            return customerList;
        } catch (SQLException e) {
            throw new EntityNotFoundException();
        }
    }

    @Override
    public Customer update(Customer entity) {
        try {
            Connection con = pool.checkOut();
            PreparedStatement prep = con.prepareStatement(UPDATE_SQL);
            prep.setString(1, entity.getUsername());
            prep.setString(2, entity.getEmail());
            prep.setString(3, entity.getPassword());
            prep.setBigDecimal(4, entity.getWallet());
            prep.setLong(5, entity.getId());
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

    private Customer mapCustomer(ResultSet rs) {
        try {
            Long id = rs.getLong("id");
            String username = rs.getString("username");
            String email = rs.getString("email");
            String password = rs.getString("password");
            BigDecimal wallet = rs.getBigDecimal("wallet");
            return new Customer(id, username, email, password, wallet);
        } catch (SQLException e) {
            throw new EntityNotFoundException();
        }
    }
}
