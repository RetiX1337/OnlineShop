package com.company.core.services.persistenceservices.dbimpl;

import com.company.JDBCConnectionPool;
import com.company.core.models.EntityNotFoundException;
import com.company.core.models.Shop;
import com.company.core.services.persistenceservices.PersistenceInterface;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ShopPersistenceServiceDatabase implements PersistenceInterface<Shop> {
    private final JDBCConnectionPool pool;
    private final String SAVE_SQL = "INSERT INTO shop (name, address) VALUES (?, ?)";
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
    }

    @Override
    public Shop save(Shop entity) {
        Connection con = pool.checkOut();
        try {
            pool.startTransaction(con);
            PreparedStatement prep = con.prepareStatement(SAVE_SQL, Statement.RETURN_GENERATED_KEYS);
            prep.setString(1, entity.getName());
            prep.setString(2, entity.getAddress());
            for (Long storageId : entity.getStorages()) {
                addBond(entity.getId(), storageId);
            }
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
    public Shop findById(Long id) {
        Connection con = pool.checkOut();
        try {
            pool.startTransaction(con);
            PreparedStatement p = con.prepareStatement(FIND_BY_ID_SQL);
            p.setLong(1, id);
            ResultSet rs = p.executeQuery();
            pool.commitTransaction(con);
            rs.next();
            Shop shop = mapShop(rs);
            return shop;
        } catch (SQLException e) {
            throw new EntityNotFoundException();
        } finally {
            pool.checkIn(con);
        }
    }

    @Override
    public List<Shop> findAll() {
        Connection con = pool.checkOut();
        try {
            pool.startTransaction(con);
            List<Shop> shops = new ArrayList<>();
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(FIND_ALL_SQL);
            pool.commitTransaction(con);
            while (rs.next()) {
                Shop shop = mapShop(rs);
                shops.add(shop);
            }
            return shops;
        } catch (SQLException e) {
            throw new EntityNotFoundException();
        } finally {
            pool.checkIn(con);
        }
    }

    @Override
    public Shop update(Shop entity) {
        Connection con = pool.checkOut();
        try {
            pool.startTransaction(con);
            PreparedStatement prep = con.prepareStatement(UPDATE_SQL);
            prep.setLong(1, entity.getId());
            prep.setString(2, entity.getName());
            prep.setString(3, entity.getAddress());
            prep.setLong(4, entity.getId());

            List<Long> newStorages = entity.getStorages();
            List<Long> oldStorages = getStoragesByShop(entity.getId());

            addNewStorages(entity, newStorages, oldStorages);

            deleteOldStorages(entity, newStorages, oldStorages);

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

    private List<Long> getStoragesByShop(Long shopId) {
        Connection con = pool.checkOut();
        try {
            pool.startTransaction(con);
            List<Long> storages = new ArrayList<>();
            PreparedStatement p = con.prepareStatement(GET_STORAGES_BY_SHOP_SQL);
            p.setLong(1, shopId);
            ResultSet rs = p.executeQuery();
            pool.commitTransaction(con);
            while (rs.next()) {
                storages.add(rs.getLong(1));
            }
            return storages;
        } catch (SQLException e) {
            throw new EntityNotFoundException();
        } finally {
            pool.checkIn(con);
        }
    }

    private void addBond(Long shopId, Long storageId) {
        Connection con = pool.checkOut();
        try {
            pool.startTransaction(con);
            PreparedStatement prep = con.prepareStatement(ADD_BOND_SQL);
            prep.setLong(1, shopId);
            prep.setLong(2, storageId);
            prep.executeUpdate();
            pool.commitTransaction(con);
        } catch (SQLException e) {
            throw new EntityNotFoundException();
        } finally {
            pool.checkIn(con);
        }
    }

    private void deleteOldStorages(Shop entity, List<Long> newStorages, List<Long> oldStorages) {
        for (Long oldStorageId : oldStorages) {
            if (!newStorages.contains(oldStorageId)) {
                deleteBond(entity.getId(), oldStorageId);
            }
        }
    }

    private void addNewStorages(Shop entity, List<Long> newStorages, List<Long> oldStorages) {
        for (Long newStorageId : newStorages) {
            if (!oldStorages.contains(newStorageId)) {
                addBond(entity.getId(), newStorageId);
            }
        }
    }

    private void deleteBond(Long shopId, Long storageId) {
        Connection con = pool.checkOut();
        try {
            pool.startTransaction(con);
            PreparedStatement prep = con.prepareStatement(DELETE_BOND_SQL);
            prep.setLong(1, shopId);
            prep.setLong(2, storageId);
            prep.executeUpdate();
            pool.commitTransaction(con);
        } catch (SQLException e) {
            throw new EntityNotFoundException();
        } finally {
            pool.checkIn(con);
        }
    }

    public Shop mapShop(ResultSet rs) {
        try {
            Long shopId = rs.getLong("id");
            String name = rs.getString("name");
            String address = rs.getString("address");
            List<Long> storages = getStoragesByShop(shopId);
            return new Shop(shopId, name, address, storages);
        } catch (SQLException e) {
            throw new EntityNotFoundException();
        }
    }
}
