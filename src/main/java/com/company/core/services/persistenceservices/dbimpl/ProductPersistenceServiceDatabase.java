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

public class ProductPersistenceServiceDatabase implements PersistenceInterface<Product> {
    private final JDBCConnectionPool pool;
    private final String DELETE_SQL = "DELETE FROM product WHERE product.id = ?";
    private final String UPDATE_SQL = "UPDATE product SET product.brand = ?, product.name = ?, product.price = ?, product_type_id = ? WHERE product.id = ?";
    private final String ALL_SQL = "SELECT * FROM product";
    private final String SAVE_SQL = "INSERT INTO product (brand, name, price, product_type_id) VALUES (?, ?, ?, ?)";
    private final String FIND_BY_ID_SQL = "SELECT product.id, product.brand, product.name, product.price, product_type.product_type FROM product INNER JOIN product_type ON product.product_type_id = product_type.id WHERE product.id = ?";
    private final String FIND_ALL_SQL = "SELECT product.id, product.brand, product.name, product.price, product_type.product_type FROM product INNER JOIN product_type ON product.product_type_id = product_type.id";


    public ProductPersistenceServiceDatabase(JDBCConnectionPool pool) {
        this.pool = pool;
    }

    @Override
    public Product save(Product entity) {
        Connection connection = pool.checkOut();
        try {
            pool.startTransaction(connection);
            PreparedStatement preparedStatement = connection.prepareStatement(SAVE_SQL, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, entity.getBrand());
            preparedStatement.setString(2, entity.getName());
            preparedStatement.setBigDecimal(3, entity.getPrice());
            preparedStatement.setLong(4, entity.getType().ordinal() + 1);
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
    public Product findById(Long id) {
        Connection connection = pool.checkOut();
        try {
            pool.startTransaction(connection);
            PreparedStatement preparedStatement = connection.prepareStatement(FIND_BY_ID_SQL);
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            pool.commitTransaction(connection);
            resultSet.next();
            Product product = mapProduct(resultSet);
            return product;
        } catch (SQLException e) {
            throw new EntityNotFoundException();
        } finally {
            pool.checkIn(connection);
        }
    }

    @Override
    public List<Product> findAll() {
        Connection connection = pool.checkOut();
        try {
            pool.startTransaction(connection);
            List<Product> productList = new ArrayList<>();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(FIND_ALL_SQL);
            pool.commitTransaction(connection);
            while (resultSet.next()) {
                Product product = mapProduct(resultSet);
                productList.add(product);
            }
            return productList;
        } catch (SQLException e) {
            throw new EntityNotFoundException();
        } finally {
            pool.checkIn(connection);
        }
    }

    @Override
    public Product update(Product entity) {
        Connection connection = pool.checkOut();
        try {
            pool.startTransaction(connection);
            PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_SQL);
            preparedStatement.setString(1, entity.getBrand());
            preparedStatement.setString(2, entity.getName());
            preparedStatement.setBigDecimal(3, entity.getPrice());
            preparedStatement.setLong(4, entity.getType().ordinal() + 1);
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

    public Product mapProduct(ResultSet resultSet) {
        try {
            ProductBase productBase = new ProductBase();

            productBase.setId(resultSet.getLong("id"));
            productBase.setBrand(resultSet.getString("brand"));
            productBase.setName(resultSet.getString("name"));
            productBase.setPrice(resultSet.getBigDecimal("price"));
            productBase.setProductType(ProductType.valueOf(resultSet.getString("product_type")));

            return productBase;
        } catch (SQLException e) {
            throw new EntityNotFoundException();
        }
    }
}
