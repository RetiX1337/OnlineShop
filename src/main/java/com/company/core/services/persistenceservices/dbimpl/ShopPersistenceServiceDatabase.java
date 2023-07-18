package com.company.core.services.persistenceservices.dbimpl;

import com.company.JDBCConnectionPool;
import com.company.core.models.EntityNotFoundException;
import com.company.core.models.EntityNotSavedException;
import com.company.core.models.Shop;
import com.company.core.services.persistenceservices.PersistenceInterface;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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
        Connection connection = pool.checkOut();
        try {
            pool.startTransaction(connection);
            PreparedStatement preparedStatement = connection.prepareStatement(SAVE_SQL, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, entity.getName());
            preparedStatement.setString(2, entity.getAddress());
            for (Long storageId : entity.getStorages()) {
                addBond(entity.getId(), storageId);
            }
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
    public Shop findById(Long id) {
        Connection connection = pool.checkOut();
        try {
            pool.startTransaction(connection);
            PreparedStatement preparedStatement = connection.prepareStatement(FIND_BY_ID_SQL);
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            pool.commitTransaction(connection);
            resultSet.next();
            Shop shop = mapShop(resultSet);
            return shop;
        } catch (SQLException e) {
            throw new EntityNotFoundException();
        } finally {
            pool.checkIn(connection);
        }
    }

    @Override
    public List<Shop> findAll() {
        Connection connection = pool.checkOut();
        try {
            pool.startTransaction(connection);
            List<Shop> shops = new ArrayList<>();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(FIND_ALL_SQL);
            pool.commitTransaction(connection);
            while (resultSet.next()) {
                Shop shop = mapShop(resultSet);
                shops.add(shop);
            }
            return shops;
        } catch (SQLException e) {
            throw new EntityNotFoundException();
        } finally {
            pool.checkIn(connection);
        }
    }

    @Override
    public Shop update(Shop entity) {
        Connection connection = pool.checkOut();
        try {
            pool.startTransaction(connection);
            PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_SQL);
            preparedStatement.setLong(1, entity.getId());
            preparedStatement.setString(2, entity.getName());
            preparedStatement.setString(3, entity.getAddress());
            preparedStatement.setLong(4, entity.getId());

            List<Long> newStorages = entity.getStorages();
            List<Long> oldStorages = getStoragesByShop(entity.getId());

            addNewStorages(entity, newStorages, oldStorages);

            deleteOldStorages(entity, newStorages, oldStorages);

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

    private List<Long> getStoragesByShop(Long shopId) {
        Connection connection = pool.checkOut();
        try {
            pool.startTransaction(connection);
            List<Long> storages = new ArrayList<>();
            PreparedStatement preparedStatement = connection.prepareStatement(GET_STORAGES_BY_SHOP_SQL);
            preparedStatement.setLong(1, shopId);
            ResultSet resultSet = preparedStatement.executeQuery();
            pool.commitTransaction(connection);
            while (resultSet.next()) {
                storages.add(resultSet.getLong(1));
            }
            return storages;
        } catch (SQLException e) {
            throw new EntityNotFoundException();
        } finally {
            pool.checkIn(connection);
        }
    }

    private void addBond(Long shopId, Long storageId) {
        Connection connection = pool.checkOut();
        try {
            pool.startTransaction(connection);
            PreparedStatement preparedStatement = connection.prepareStatement(ADD_BOND_SQL);
            preparedStatement.setLong(1, shopId);
            preparedStatement.setLong(2, storageId);
            preparedStatement.executeUpdate();
            pool.commitTransaction(connection);
        } catch (SQLException e) {
            throw new EntityNotFoundException();
        } finally {
            pool.checkIn(connection);
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
        Connection connection = pool.checkOut();
        try {
            pool.startTransaction(connection);
            PreparedStatement prep = connection.prepareStatement(DELETE_BOND_SQL);
            prep.setLong(1, shopId);
            prep.setLong(2, storageId);
            prep.executeUpdate();
            pool.commitTransaction(connection);
        } catch (SQLException e) {
            throw new EntityNotFoundException();
        } finally {
            pool.checkIn(connection);
        }
    }

    public Shop mapShop(ResultSet resultSet) {
        try {
            Long shopId = resultSet.getLong("id");
            String name = resultSet.getString("name");
            String address = resultSet.getString("address");
            List<Long> storages = getStoragesByShop(shopId);
            return new Shop(shopId, name, address, storages);
        } catch (SQLException e) {
            throw new EntityNotFoundException();
        }
    }
}
