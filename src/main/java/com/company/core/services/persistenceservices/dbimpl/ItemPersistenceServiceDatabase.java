package com.company.core.services.persistenceservices.dbimpl;

import com.company.JDBCConnectionPool;
import com.company.core.models.goods.Item;
import com.company.core.services.persistenceservices.PersistenceInterface;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;

public class ItemPersistenceServiceDatabase implements PersistenceInterface<Item> {
    private final JDBCConnectionPool pool;
    private Properties sqlProps;

    {
        sqlProps = new Properties();
        try {
            sqlProps.load(new FileInputStream("src\\main\\java\\com\\company\\core\\sql_queries\\item.properties"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private final String DELETE_SQL = sqlProps.getProperty("DELETE_SQL");
    private final String UPDATE_SQL = sqlProps.getProperty("UPDATE_SQL");
    private final String ALL_SQL = sqlProps.getProperty("ALL_SQL");
    private final String SAVE_SQL = sqlProps.getProperty("SAVE_SQL");
    private final String FIND_BY_ID_SQL = sqlProps.getProperty("FIND_BY_ID_SQL");
    private final String FIND_ALL_SQL = sqlProps.getProperty("FIND_ALL_SQL");
    private Long idCounter;

    public ItemPersistenceServiceDatabase(JDBCConnectionPool pool) {
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
    public Item save(Item entity) {
        return null;
    }

    @Override
    public Item findById(Long id) {
        return null;
    }

    @Override
    public List<Item> findAll() {
        return null;
    }

    @Override
    public Item update(Item entity, Long id) {
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
