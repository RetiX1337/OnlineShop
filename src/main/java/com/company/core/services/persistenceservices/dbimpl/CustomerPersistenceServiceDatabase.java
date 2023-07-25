package com.company.core.services.persistenceservices.dbimpl;

import com.company.core.pools.JDBCConnectionPool;
import com.company.core.exceptions.EntityNotFoundException;
import com.company.core.exceptions.EntityNotSavedException;
import com.company.core.models.user.customer.Cart;
import com.company.core.models.user.customer.Customer;
import com.company.core.services.persistenceservices.PersistenceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
@Repository
public class CustomerPersistenceServiceDatabase implements PersistenceInterface<Customer> {
    private final JDBCConnectionPool pool;
    private final String DELETE_SQL = "DELETE FROM customer WHERE customer.id = ?";
    private final String UPDATE_SQL = "UPDATE customer SET username = ?, email = ?, password = ?, wallet = ? WHERE customer.id = ?";
    private final String ALL_SQL = "SELECT * FROM customer";
    private final String SAVE_SQL = "INSERT INTO customer (username, email, password, wallet) VALUES (?, ?, ?, ?)";
    private final String FIND_BY_ID_SQL = "SELECT customer.id, customer.username, customer.email, customer.password, customer.wallet FROM customer WHERE customer.id = ?";
    private final String FIND_ALL_SQL = "SELECT customer.id, customer.username, customer.email, customer.password, customer.wallet FROM customer";

    public CustomerPersistenceServiceDatabase(@Autowired JDBCConnectionPool pool) {
        this.pool = pool;
    }

    @Override
    public Customer save(Customer entity) {
        Connection connection = pool.checkOut();
        try {
            pool.startTransaction(connection);
            PreparedStatement preparedStatement = connection.prepareStatement(SAVE_SQL, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, entity.getUsername());
            preparedStatement.setString(2, entity.getEmail());
            preparedStatement.setString(3, entity.getPassword());
            preparedStatement.setBigDecimal(4, entity.getWallet());
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
    public Customer findById(Long id) {
        Connection connection = pool.checkOut();
        try {
            pool.startTransaction(connection);
            PreparedStatement preparedStatement = connection.prepareStatement(FIND_BY_ID_SQL);
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            pool.commitTransaction(connection);
            resultSet.next();
            Customer customer = mapCustomer(resultSet);
            return customer;
        } catch (SQLException e) {
            throw new EntityNotFoundException();
        } finally {
            pool.checkIn(connection);
        }
    }

    @Override
    public List<Customer> findAll() {
        Connection connection = pool.checkOut();
        try {
            pool.startTransaction(connection);
            List<Customer> customerList = new ArrayList<>();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(FIND_ALL_SQL);
            pool.commitTransaction(connection);
            while (resultSet.next()) {
                Customer customer = mapCustomer(resultSet);
                customerList.add(customer);
            }
            return customerList;
        } catch (SQLException e) {
            throw new EntityNotFoundException();
        } finally {
            pool.checkIn(connection);
        }
    }

    @Override
    public Customer update(Customer entity) {
        Connection connection = pool.checkOut();
        try {
            pool.startTransaction(connection);
            PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_SQL);
            preparedStatement.setString(1, entity.getUsername());
            preparedStatement.setString(2, entity.getEmail());
            preparedStatement.setString(3, entity.getPassword());
            preparedStatement.setBigDecimal(4, entity.getWallet());
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

    private Customer mapCustomer(ResultSet resultSet) {
        try {
            Customer customer = new Customer();

            customer.setId(resultSet.getLong("id"));
            customer.setUsername(resultSet.getString("username"));
            customer.setEmail(resultSet.getString("email"));
            customer.setPassword(resultSet.getString("password"));
            customer.setWallet(BigDecimal.valueOf(500));
            customer.setCart(new Cart());

            return customer;
        } catch (SQLException e) {
            throw new EntityNotFoundException();
        }
    }
}
