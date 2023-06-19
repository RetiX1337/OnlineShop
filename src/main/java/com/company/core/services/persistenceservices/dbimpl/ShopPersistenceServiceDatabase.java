package com.company.core.services.persistenceservices.dbimpl;

import com.company.JDBCConnectionPool;
import com.company.core.models.Shop;
import com.company.core.models.goods.ProductBase;
import com.company.core.models.goods.ProductType;
import com.company.core.services.persistenceservices.PersistenceInterface;

import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;

public class ShopPersistenceServiceDatabase implements PersistenceInterface<Shop> {
    private final JDBCConnectionPool pool;
    private Properties sqlProps;

    {
        sqlProps = new Properties();
        try {
            sqlProps.load(new FileInputStream("src\\main\\java\\com\\company\\core\\sql_queries\\product.properties"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    private Long idCounter;

    private final String ALL_SQL = "SELECT * FROM shop";
    private final String FIND_BY_ID_SQL = "SELECT * FROM shop WHERE id = ?";

    public ShopPersistenceServiceDatabase(JDBCConnectionPool pool) {
        this.pool = pool;
        initCounter();
    }

    private void initCounter() {
        try {
            Connection con = pool.checkOut();
            ResultSet rs = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY).executeQuery(ALL_SQL);
            rs.last();
            idCounter = rs.getLong("id") + 1;
            pool.checkIn(con);
            System.out.println(idCounter);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    public Shop save(Shop entity) {
        return null;
    }

    @Override
    public Shop findById(Long id) {
        try {
        Connection con = pool.checkOut();
        PreparedStatement p = con.prepareStatement(FIND_BY_ID_SQL);
        p.setLong(1, id);
        ResultSet rs = p.executeQuery();
        rs.next();
        String name = rs.getString("brand");
        String address = rs.getString("name");
        pool.checkIn(con);
        return new Shop(id, name, address);
    } catch (SQLException e) {
        throw new RuntimeException(e);
    }
    }

    @Override
    public List<Shop> findAll() {
        return null;
    }

    @Override
    public Shop update(Shop entity, Long id) {
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
