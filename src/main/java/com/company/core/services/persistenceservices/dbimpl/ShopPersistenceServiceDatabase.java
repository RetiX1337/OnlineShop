package com.company.core.services.persistenceservices.dbimpl;

import com.company.JDBCConnectionPool;
import com.company.core.models.Shop;
import com.company.core.models.Storage;
import com.company.core.models.goods.Product;
import com.company.core.models.goods.ProductBase;
import com.company.core.models.goods.ProductType;
import com.company.core.services.persistenceservices.PersistenceInterface;

import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class ShopPersistenceServiceDatabase implements PersistenceInterface<Shop> {
    private final JDBCConnectionPool pool;
    private Long idCounter;
    private final String SAVE_SQL = "INSERT INTO shop (id, name, address) VALUES (?, ?, ?)";
    private final String FIND_ALL_SQL = "SELECT id, name, address FROM shop";
    private final String UPDATE_SQL = "UPDATE shop SET id = ?, name = ?, address = ? WHERE id = ?";
    private final String DELETE_SQL = "DELETE FROM shop WHERE id = ?";
    private final String ALL_SQL = "SELECT * FROM shop";
    private final String FIND_BY_ID_SQL = "SELECT * FROM shop WHERE id = ?";
    private final String GET_STORAGES_BY_SHOP_SQL = "SELECT s.id FROM storage s INNER JOIN shop_storage ss ON s.id = ss.storage_id INNER JOIN shop sh ON ss.shop_id = sh.id WHERE sh.id = ?";
    private final String ADD_BOND_SQL = "INSERT INTO shop_storage (shop_id, storage_id) VALUES (?, ?)";
    private final String DELETE_BOND_SQL = "DELETE FROM shop_storage WHERE shop_id = ? AND storage_id = ?";
    private final String FIND_BOND_SQL = "SELECT * FROM shop_storage WHERE shop_id = ? AND storage_id = ?";

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
        entity.setId(idCounter);
        try {
            Connection con = pool.checkOut();
            PreparedStatement prep = con.prepareStatement(SAVE_SQL);
            prep.setLong(1, entity.getId());
            prep.setString(2, entity.getName());
            prep.setString(3, entity.getAddress());
            for (Long storageId : entity.getStorages()) {
                addBond(entity.getId(), storageId);
            }
            prep.executeUpdate();
            pool.checkIn(con);
            idCounter++;
            return entity;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Shop findById(Long id) {
        try {
            Connection con = pool.checkOut();
            PreparedStatement p = con.prepareStatement(FIND_BY_ID_SQL);
            p.setLong(1, id);
            ResultSet rs = p.executeQuery();
            rs.next();
            String name = rs.getString("name");
            String address = rs.getString("address");
            List<Long> storages = getStoragesByShop(id);
            pool.checkIn(con);
            return new Shop(id, name, address, storages);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Shop> findAll() {
        try {
            List<Shop> shops = new ArrayList<>();
            Connection con = pool.checkOut();
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(FIND_ALL_SQL);
            while (rs.next()) {
                Long shopId = rs.getLong("id");
                String name = rs.getString("name");
                String address = rs.getString("address");
                List<Long> storages = getStoragesByShop(shopId);
                shops.add(new Shop(shopId, name, address, storages));
            }
            pool.checkIn(con);
            return shops;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Shop update(Shop entity, Long id) {
        try {
            Connection con = pool.checkOut();
            PreparedStatement prep = con.prepareStatement(UPDATE_SQL);
            prep.setLong(1, id);
            prep.setString(2, entity.getName());
            prep.setString(3, entity.getAddress());
            prep.setLong(4, id);

            List<Long> newStorages = entity.getStorages();
            List<Long> oldStorages = getStoragesByShop(id);

            for (Long newStorageId : newStorages) {
                if (!oldStorages.contains(newStorageId)) {
                    addBond(id, newStorageId);
                }
            }

            for (Long oldStorageId : oldStorages) {
                if (!newStorages.contains(oldStorageId)) {
                    deleteBond(id, oldStorageId);
                }
            }
            prep.executeUpdate();
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

    private List<Long> getStoragesByShop(Long shopId) {
        try {
            List<Long> storages = new ArrayList<>();
            Connection con = pool.checkOut();
            PreparedStatement p = con.prepareStatement(GET_STORAGES_BY_SHOP_SQL);
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

    private void addBond(Long shopId, Long storageId) {
        try {
            Connection con = pool.checkOut();
            PreparedStatement prep = con.prepareStatement(ADD_BOND_SQL);
            prep.setLong(1, shopId);
            prep.setLong(2, storageId);
            prep.executeUpdate();
            pool.checkIn(con);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void deleteBond(Long shopId, Long storageId) {
        try {
            Connection con = pool.checkOut();
            PreparedStatement prep = con.prepareStatement(DELETE_BOND_SQL);
            prep.setLong(1, shopId);
            prep.setLong(2, storageId);
            prep.executeUpdate();
            pool.checkIn(con);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
