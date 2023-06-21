package com.company.core.services.persistenceservices.dbimpl;

import com.company.JDBCConnectionPool;
import com.company.core.models.Storage;
import com.company.core.services.persistenceservices.ShopStoragePersistence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ShopStoragePersistenceServiceDatabase implements ShopStoragePersistence {
    private final JDBCConnectionPool pool;
    private Long idCounter;
    private final String GET_STORAGES_SQL = "SELECT s.id FROM storage s INNER JOIN shop_storage ss ON s.id = ss.storage_id INNER JOIN shop sh ON ss.shop_id = sh.id WHERE sh.id = ?";
    private final String GET_SHOPS_SQL = "SELECT sh.id FROM online_shop.shop sh INNER JOIN online_shop.shop_storage ss ON sh.id = ss.shop_id INNER JOIN online_shop.storage s ON ss.storage_id = s.id WHERE s.id = ?";
    private final String ALL_SQL = "SELECT * FROM storage";
    private final String SAVE_SQL = "INSERT INTO shop_storage (shop_id, storage_id) VALUES (?, ?)";
    private final String DELETE_SQL = "DELETE FROM shop_storage WHERE shop_id = ? AND storage_id = ?";

    public ShopStoragePersistenceServiceDatabase(JDBCConnectionPool pool) {
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
    public List<Long> getStorages(Long shopId) {
        try {
            List<Long> storages = new ArrayList<>();
            Connection con = pool.checkOut();
            PreparedStatement p = con.prepareStatement(GET_STORAGES_SQL);
            p.setLong(1, shopId);
            ResultSet rs = p.executeQuery();
            while (rs.next()) {
                storages.add(rs.getLong(1));
            }
            pool.checkIn(con);
            return storages;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Long> getShops(Long storageId) {
        try {
            List<Long> shops = new ArrayList<>();
            Connection con = pool.checkOut();
            PreparedStatement p = con.prepareStatement(GET_SHOPS_SQL);
            p.setLong(1, storageId);
            ResultSet rs = p.executeQuery();
            while (rs.next()) {
                shops.add(rs.getLong(1));
            }
            pool.checkIn(con);
            return shops;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void addBond(Long shopId, Long storageId) {
        try {
            Connection con = pool.checkOut();
            PreparedStatement prep = con.prepareStatement(SAVE_SQL);
            prep.setLong(1, shopId);
            prep.setLong(2, storageId);
            prep.executeUpdate();
            pool.checkIn(con);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteBond(Long shopId, Long storageId) {
        try {
            Connection con = pool.checkOut();
            PreparedStatement prep = con.prepareStatement(DELETE_SQL);
            prep.setLong(1, shopId);
            prep.setLong(2, storageId);
            prep.executeUpdate();
            pool.checkIn(con);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
