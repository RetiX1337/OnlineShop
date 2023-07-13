package com.company.core.services.persistenceservices.dbimpl;

import com.company.JDBCConnectionPool;
import com.company.core.models.EntityNotFoundException;
import com.company.core.models.goods.*;
import com.company.core.services.persistenceservices.PersistenceInterface;

import java.math.BigDecimal;
import java.sql.*;
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
        Connection con = pool.checkOut();
        try {
            pool.startTransaction(con);
            PreparedStatement prep = con.prepareStatement(SAVE_SQL, Statement.RETURN_GENERATED_KEYS);
            prep.setString(1, entity.getBrand());
            prep.setString(2, entity.getName());
            prep.setBigDecimal(3, entity.getPrice());
            prep.setLong(4, entity.getType().ordinal() + 1);
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
    public Product findById(Long id) {
        Connection con = pool.checkOut();
        try {
            pool.startTransaction(con);
            PreparedStatement p = con.prepareStatement(FIND_BY_ID_SQL);
            p.setLong(1, id);
            ResultSet rs = p.executeQuery();
            pool.commitTransaction(con);
            rs.next();
            Product product = mapProduct(rs);
            return product;
        } catch (SQLException e) {
            throw new EntityNotFoundException();
        } finally {
            pool.checkIn(con);
        }
    }

    @Override
    public List<Product> findAll() {
        Connection con = pool.checkOut();
        try {
            pool.startTransaction(con);
            List<Product> productList = new ArrayList<>();
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(FIND_ALL_SQL);
            pool.commitTransaction(con);
            while (rs.next()) {
                Product product = mapProduct(rs);
                productList.add(product);
            }
            return productList;
        } catch (SQLException e) {
            throw new EntityNotFoundException();
        } finally {
            pool.checkIn(con);
        }
    }

    @Override
    public Product update(Product entity) {
        Connection con = pool.checkOut();
        try {
            pool.startTransaction(con);
            PreparedStatement prep = con.prepareStatement(UPDATE_SQL);
            prep.setString(1, entity.getBrand());
            prep.setString(2, entity.getName());
            prep.setBigDecimal(3, entity.getPrice());
            prep.setLong(4, entity.getType().ordinal() + 1);
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

    public Product mapProduct(ResultSet rs) {
        try {
            Long id = rs.getLong("id");
            String brand = rs.getString("brand");
            String name = rs.getString("name");
            BigDecimal price = rs.getBigDecimal("price");
            ProductType productType = ProductType.valueOf(rs.getString("product_type"));
            return new ProductBase(id, brand, name, productType, price);
        } catch (SQLException e) {
            throw new EntityNotFoundException();
        }
    }
}
