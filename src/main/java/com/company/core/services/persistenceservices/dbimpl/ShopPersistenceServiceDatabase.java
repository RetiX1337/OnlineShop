package com.company.core.services.persistenceservices.dbimpl;

import com.company.JDBCConnectionPool;
import com.company.core.models.EntityNotFoundException;
import com.company.core.models.EntityNotSavedException;
import com.company.core.models.Shop;
import com.company.core.models.Storage;
import com.company.core.services.persistenceservices.PersistenceInterface;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ShopPersistenceServiceDatabase implements PersistenceInterface<Shop> {
    private final JDBCConnectionPool pool;
    private final PersistenceInterface<Storage> storagePersistenceService;
    private final String SAVE_SQL = "INSERT INTO shop (name, address) VALUES (?, ?)";
    private final String FIND_ALL_SQL = "SELECT id, name, address FROM shop";
    private final String UPDATE_SQL = "UPDATE shop SET id = ?, name = ?, address = ? WHERE id = ?";
    private final String DELETE_SQL = "DELETE FROM shop WHERE id = ?";
    private final String ALL_SQL = "SELECT * FROM shop";
    private final String FIND_BY_ID_SQL = "SELECT * FROM shop WHERE id = ?";
    private final String GET_STORAGES_BY_SHOP_SQL = "SELECT s.id FROM storage s INNER JOIN shop_storage ss ON s.id = ss.storage_id INNER JOIN shop sh ON ss.shop_id = sh.id WHERE sh.id = ?";
    private final String ADD_SHOP_STORAGE_RELATION = "INSERT INTO shop_storage (shop_id, storage_id) VALUES (?, ?)";
    private final String DELETE_SHOP_STORAGE_RELATION = "DELETE FROM shop_storage WHERE shop_id = ? AND storage_id = ?";
    private final String FIND_BOND_SQL = "SELECT * FROM shop_storage WHERE shop_id = ? AND storage_id = ?";

    public ShopPersistenceServiceDatabase(JDBCConnectionPool pool, PersistenceInterface<Storage> storagePersistenceService) {
        this.pool = pool;
        this.storagePersistenceService = storagePersistenceService;
    }

    @Override
    public Shop save(Shop entity) {
        Connection connection = pool.checkOut();
        try {
            pool.startTransaction(connection);
            PreparedStatement preparedStatement = connection.prepareStatement(SAVE_SQL, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, entity.getName());
            preparedStatement.setString(2, entity.getAddress());
            for (Storage storage : entity.getStorages()) {
                addOrDeleteShopStorageRelation(entity.getId(), storage.getId(), ADD_SHOP_STORAGE_RELATION);
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
            return mapShop(resultSet);
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

            Set<Storage> newStorages = entity.getStorages();
            Set<Storage> oldStorages = getStoragesByShop(entity.getId());

            addNewShopStorageRelations(entity, newStorages, oldStorages);

            deleteOldShopStorageRelations(entity, newStorages, oldStorages);

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

    private Set<Storage> getStoragesByShop(Long shopId) {
        Connection connection = pool.checkOut();
        try {
            pool.startTransaction(connection);
            Set<Storage> storages = new HashSet<>();
            PreparedStatement preparedStatement = connection.prepareStatement(GET_STORAGES_BY_SHOP_SQL);
            preparedStatement.setLong(1, shopId);
            ResultSet resultSet = preparedStatement.executeQuery();
            pool.commitTransaction(connection);
            while (resultSet.next()) {
                storages.add(storagePersistenceService.findById(resultSet.getLong("id")));
            }
            return storages;
        } catch (SQLException e) {
            throw new EntityNotFoundException();
        } finally {
            pool.checkIn(connection);
        }
    }

    private void addOrDeleteShopStorageRelation(Long shopId, Long storageId, String SQL) {
        Connection connection = pool.checkOut();
        try {
            pool.startTransaction(connection);
            PreparedStatement preparedStatement = connection.prepareStatement(SQL);
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

    private void deleteOldShopStorageRelations(Shop shop, Set<Storage> newStorages, Set<Storage> oldStorages) {
        for (Storage oldStorage : oldStorages) {
            if (!newStorages.contains(oldStorage)) {
                addOrDeleteShopStorageRelation(shop.getId(), oldStorage.getId(), DELETE_SHOP_STORAGE_RELATION);
            }
        }
    }

    private void addNewShopStorageRelations(Shop shop, Set<Storage> newStorages, Set<Storage> oldStorages) {
        for (Storage newStorage : newStorages) {
            if (!oldStorages.contains(newStorage)) {
                addOrDeleteShopStorageRelation(shop.getId(), newStorage.getId(), ADD_SHOP_STORAGE_RELATION);
            }
        }
    }


    public Shop mapShop(ResultSet resultSet) {
        try {
            Shop shop = new Shop();

            shop.setId(resultSet.getLong("id"));
            shop.setName(resultSet.getString("name"));
            shop.setAddress(resultSet.getString("address"));
            shop.setStorages(getStoragesByShop(shop.getId()));

            return shop;
        } catch (SQLException e) {
            throw new EntityNotFoundException();
        }
    }
}
