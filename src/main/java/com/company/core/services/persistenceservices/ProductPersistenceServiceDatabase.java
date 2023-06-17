package com.company.core.services.persistenceservices;

import com.company.JDBCConnectionPool;
import com.company.core.models.goods.Product;
import com.company.core.models.goods.ProductBase;
import com.company.core.models.goods.ProductType;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductPersistenceServiceDatabase implements PersistenceInterface<Product> {
    private final JDBCConnectionPool pool;
    private final String DELETE_SQL = "DELETE FROM product WHERE product.id = ?";
    private final String UPDATE_SQL = "UPDATE product SET product.brand = ?, product.name = ?, product.price = ?, product_type_id = ? WHERE product.id = ?";
    private final String ALL_SQL = "SELECT * FROM product";
    private final String SAVE_SQL = "INSERT INTO product (id, brand, name, price, product_type_id) VALUES (?, ?, ?, ?, ?)";
    private final String FIND_BY_ID_SQL = "SELECT product.id, product.brand, product.name, product.price, product_type.product_type FROM product INNER JOIN product_type ON product.product_type_id = product_type.id WHERE product.id = ?";
    private final String FIND_ALL_SQL = "SELECT product.id, product.brand, product.name, product.price, product_type.product_type FROM product INNER JOIN product_type ON product.product_type_id = product_type.id";
    private static Long idCounter = 1L;

    public ProductPersistenceServiceDatabase(JDBCConnectionPool pool) {
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
    public Product save(Product entity) {
        entity.setId(idCounter);
        try {
            Connection con = pool.checkOut();
            PreparedStatement prep = con.prepareStatement(SAVE_SQL);
            prep.setInt(1, Math.toIntExact(entity.getId()));
            prep.setString(2, entity.getBrand());
            prep.setString(3, entity.getName());
            prep.setBigDecimal(4, entity.getPrice());
            prep.setInt(5, entity.getType().ordinal() + 1);
            prep.executeUpdate();
            pool.checkIn(con);
            idCounter++;
            return entity;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Product findById(Long id) {
        try {
            Connection con = pool.checkOut();
            PreparedStatement p = con.prepareStatement(FIND_BY_ID_SQL);
            p.setInt(1, Math.toIntExact(id));
            ResultSet rs = p.executeQuery();
            rs.next();
            String brand = rs.getString("brand");
            String name = rs.getString("name");
            BigDecimal price = rs.getBigDecimal("price");
            ProductType productType = ProductType.valueOf(rs.getString("product_type"));
            pool.checkIn(con);
            return new ProductBase(id, brand, name, productType, price);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Product> findAll() {
        try {
            List<Product> productList = new ArrayList<>();
            Connection con = pool.checkOut();
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(FIND_ALL_SQL);
            while (rs.next()) {
                Long id = Long.valueOf(rs.getInt("id"));
                String brand = rs.getString("brand");
                String name = rs.getString("name");
                BigDecimal price = rs.getBigDecimal("price");
                ProductType productType = ProductType.valueOf(rs.getString("product_type"));
                productList.add(new ProductBase(id, brand, name, productType, price));
            }
            pool.checkIn(con);
            return productList;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Product update(Product entity, Long id) {
        try {
            Connection con = pool.checkOut();
            PreparedStatement prep = con.prepareStatement(UPDATE_SQL);
            prep.setString(1, entity.getBrand());
            prep.setString(2, entity.getName());
            prep.setBigDecimal(3, entity.getPrice());
            prep.setInt(4, entity.getType().ordinal() + 1);
            prep.setInt(5, id.intValue());
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
            prep.setInt(1, id.intValue());
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
            prep.setInt(1, Math.toIntExact(id));
            ResultSet rs = prep.executeQuery();
            pool.checkIn(con);
            return rs.isBeforeFirst();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
