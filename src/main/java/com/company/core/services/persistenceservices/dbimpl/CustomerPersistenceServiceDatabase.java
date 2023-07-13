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
        Connection con = pool.checkOut();
        try {
            pool.startTransaction(con);
            PreparedStatement prep = con.prepareStatement(SAVE_SQL, Statement.RETURN_GENERATED_KEYS);
            prep.setString(1, entity.getUsername());
            prep.setString(2, entity.getEmail());
            prep.setString(3, entity.getPassword());
            prep.setBigDecimal(4, entity.getWallet());
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
    public Customer findById(Long id) {
        Connection con = pool.checkOut();
        try {
            pool.startTransaction(con);
            PreparedStatement p = con.prepareStatement(FIND_BY_ID_SQL);
            p.setLong(1, id);
            ResultSet rs = p.executeQuery();
            pool.commitTransaction(con);
            rs.next();
            Customer customer = mapCustomer(rs);
            return customer;
        } catch (SQLException e) {
            throw new EntityNotFoundException();
        } finally {
            pool.checkIn(con);
        }
    }

    @Override
    public List<Customer> findAll() {
        Connection con = pool.checkOut();
        try {
            pool.startTransaction(con);
            List<Customer> customerList = new ArrayList<>();
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(FIND_ALL_SQL);
            pool.commitTransaction(con);
            while (rs.next()) {
                Customer customer = mapCustomer(rs);
                customerList.add(customer);
            }
            return customerList;
        } catch (SQLException e) {
            throw new EntityNotFoundException();
        } finally {
            pool.checkIn(con);
        }
    }

    @Override
    public Customer update(Customer entity) {
        Connection con = pool.checkOut();
        try {
            pool.startTransaction(con);
            PreparedStatement prep = con.prepareStatement(UPDATE_SQL);
            prep.setString(1, entity.getUsername());
            prep.setString(2, entity.getEmail());
            prep.setString(3, entity.getPassword());
            prep.setBigDecimal(4, entity.getWallet());
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
