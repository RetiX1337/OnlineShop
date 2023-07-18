package com.company.core.services.persistenceservices.dbimpl;

import com.company.JDBCConnectionPool;
import com.company.core.models.EntityNotFoundException;
import com.company.core.models.EntityNotSavedException;
import com.company.core.models.Storage;
import com.company.core.models.goods.Product;
import com.company.core.models.goods.ProductWithQuantity;
import com.company.core.services.persistenceservices.PersistenceInterface;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class StoragePersistenceServiceDatabase implements PersistenceInterface<Storage> {
    private final JDBCConnectionPool pool;
    private final PersistenceInterface<Product> productPersistenceService;
    private final String FIND_QUANTITY_SQL = "SELECT * FROM product_storage WHERE product_id = ? AND storage_id = ?";
    private final String UPDATE_QUANTITY_SQL = "UPDATE product_storage SET quantity = ? WHERE storage_id = ? AND product_id = ?";
    private final String ADD_QUANTITY_SQL = "INSERT INTO product_storage (storage_id, product_id, quantity) VALUES (?, ?, ?)";
    private final String SAVE_SQL = "INSERT INTO storage (name, address) VALUES (?, ?)";
    private final String FIND_ALL_SQL = "SELECT id, name, address FROM storage";
    private final String UPDATE_SQL = "UPDATE storage SET id = ?, name = ?, address = ? WHERE id = ?";
    private final String DELETE_SQL = "DELETE FROM storage WHERE id = ?";
    private final String GET_SHOPS_BY_STORAGES_SQL = "SELECT s.id FROM storage s INNER JOIN shop_storage ss ON s.id = ss.storage_id INNER JOIN shop sh ON ss.shop_id = sh.id WHERE sh.id = ?";
    private final String ADD_BOND_SQL = "INSERT INTO shop_storage (shop_id, storage_id) VALUES (?, ?)";
    private final String DELETE_BOND_SQL = "DELETE FROM shop_storage WHERE shop_id = ? AND storage_id = ?";
    private final String FIND_BOND_SQL = "SELECT * FROM shop_storage WHERE shop_id = ? AND storage_id = ?";
    private final String ALL_SQL = "SELECT * FROM storage";
    private final String FIND_BY_ID_SQL = "SELECT * FROM storage WHERE id = ?";
    private final String GET_QUANTITIES_SQL = "SELECT product_id, quantity FROM product_storage WHERE storage_id = ?";

    public StoragePersistenceServiceDatabase(JDBCConnectionPool pool, PersistenceInterface<Product> productPersistenceService) {
        this.pool = pool;
        this.productPersistenceService = productPersistenceService;
    }

    @Override
    public Storage save(Storage entity) {
        Connection connection = pool.checkOut();
        try {
            pool.startTransaction(connection);
            PreparedStatement preparedStatement = connection.prepareStatement(SAVE_SQL, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, entity.getName());
            preparedStatement.setString(2, entity.getAddress());
            preparedStatement.executeUpdate();
            pool.commitTransaction(connection);

            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            resultSet.next();
            Long id = resultSet.getLong(1);
            entity.setId(id);

            for (Long shopId : entity.getShops()) {
                addBond(shopId, id);
            }

            for (ProductWithQuantity productWithQuantity : entity.getProductQuantities().values()) {
                addQuantity(productWithQuantity, entity.getId());
            }

            return entity;
        } catch (SQLException e) {
            throw new EntityNotSavedException();
        } finally {
            pool.checkIn(connection);
        }
    }

    @Override
    public Storage findById(Long id) {
        Connection connection = pool.checkOut();
        try {
            pool.startTransaction(connection);
            PreparedStatement preparedStatement = connection.prepareStatement(FIND_BY_ID_SQL);
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            pool.commitTransaction(connection);
            resultSet.next();
            Storage storage = mapStorage(resultSet);
            return storage;
        } catch (SQLException e) {
            throw new EntityNotFoundException();
        } finally {
            pool.checkIn(connection);
        }
    }

    @Override
    public List<Storage> findAll() {
        Connection connection = pool.checkOut();
        try {
            pool.startTransaction(connection);
            List<Storage> storages = new ArrayList<>();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(FIND_ALL_SQL);
            pool.commitTransaction(connection);
            while (resultSet.next()) {
                Storage storage = mapStorage(resultSet);
                storages.add(storage);
            }
            return storages;
        } catch (SQLException e) {
            throw new EntityNotFoundException();
        } finally {
            pool.checkIn(connection);
        }
    }

    @Override
    public Storage update(Storage entity) {
        Connection connection = pool.checkOut();
        try {
            pool.startTransaction(connection);
            PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_SQL);
            preparedStatement.setLong(1, entity.getId());
            preparedStatement.setString(2, entity.getName());
            preparedStatement.setString(3, entity.getAddress());
            preparedStatement.setLong(4, entity.getId());

            List<Long> newShops = entity.getShops();
            List<Long> oldShops = getShopsByStorage(entity.getId());

            addNewShops(entity, newShops, oldShops);

            deleteOldShops(entity, newShops, oldShops);

            HashMap<Long, ProductWithQuantity> productQuantities = entity.getProductQuantities();

            saveQuantities(entity, productQuantities);

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

    private void deleteBond(Long shopId, Long storageId) {
        Connection connection = pool.checkOut();
        try {
            pool.startTransaction(connection);
            PreparedStatement preparedStatement = connection.prepareStatement(DELETE_BOND_SQL);
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

    private void saveQuantities(Storage entity, HashMap<Long, ProductWithQuantity> productQuantities) {
        for (ProductWithQuantity productWithQuantity : productQuantities.values()) {
            if (!quantityIsPresent(productWithQuantity.getProduct().getId(), entity.getId())) {
                addQuantity(productWithQuantity, entity.getId());
            } else {
                updateQuantity(productWithQuantity, entity.getId());
            }
        }
    }

    private void deleteOldShops(Storage entity, List<Long> newShops, List<Long> oldShops) {
        for (Long oldShopId : oldShops) {
            if (!newShops.contains(oldShopId)) {
                deleteBond(entity.getId(), oldShopId);
            }
        }
    }

    private void addNewShops(Storage entity, List<Long> newShops, List<Long> oldShops) {
        for (Long newShopId : newShops) {
            if (!oldShops.contains(newShopId)) {
                addBond(entity.getId(), newShopId);
            }
        }
    }

    private List<Long> getShopsByStorage(Long storageId) {
        Connection connection = pool.checkOut();
        try {
            pool.startTransaction(connection);
            List<Long> shops = new ArrayList<>();
            PreparedStatement preparedStatement = connection.prepareStatement(GET_SHOPS_BY_STORAGES_SQL);
            preparedStatement.setLong(1, storageId);
            ResultSet resultSet = preparedStatement.executeQuery();
            pool.commitTransaction(connection);
            while (resultSet.next()) {
                shops.add(resultSet.getLong(1));
            }
            return shops;
        } catch (SQLException e) {
            throw new EntityNotFoundException();
        } finally {
            pool.checkIn(connection);
        }
    }

    private HashMap<Long, ProductWithQuantity> getQuantities(Long storageId) {
        Connection connection = pool.checkOut();
        try {
            pool.startTransaction(connection);
            HashMap<Long, ProductWithQuantity> productQuantities = new HashMap<>();
            PreparedStatement preparedStatement = connection.prepareStatement(GET_QUANTITIES_SQL);
            preparedStatement.setLong(1, storageId);
            ResultSet resultSet = preparedStatement.executeQuery();
            pool.commitTransaction(connection);
            while (resultSet.next()) {
                Product product = productPersistenceService.findById(resultSet.getLong("product_id"));
                Integer quantity = resultSet.getInt("quantity");
                productQuantities.put(product.getId(), new ProductWithQuantity(product, quantity));
            }
            return productQuantities;
        } catch (SQLException e) {
            throw new EntityNotFoundException();
        } finally {
            pool.checkIn(connection);
        }
    }

    private void updateQuantity(ProductWithQuantity productWithQuantity, Long storageId) {
        Connection connection = pool.checkOut();
        try {
            pool.startTransaction(connection);
            PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_QUANTITY_SQL);
            preparedStatement.setInt(1, productWithQuantity.getQuantity());
            preparedStatement.setLong(2, storageId);
            preparedStatement.setLong(3, productWithQuantity.getProduct().getId());
            preparedStatement.executeUpdate();
            pool.commitTransaction(connection);
        } catch (SQLException e) {
            throw new EntityNotFoundException();
        } finally {
            pool.checkIn(connection);
        }
    }

    private void addQuantity(ProductWithQuantity productWithQuantity, Long storageId) {
        Connection connection = pool.checkOut();
        try {
            pool.startTransaction(connection);
            PreparedStatement preparedStatement = connection.prepareStatement(ADD_QUANTITY_SQL);
            preparedStatement.setLong(1, storageId);
            preparedStatement.setLong(2, productWithQuantity.getProduct().getId());
            preparedStatement.setInt(3, productWithQuantity.getQuantity());
            preparedStatement.executeUpdate();
            pool.commitTransaction(connection);
        } catch (SQLException e) {
            throw new EntityNotFoundException();
        } finally {
            pool.checkIn(connection);
        }
    }

    private boolean quantityIsPresent(Long productId, Long storageId) {
        Connection connection = pool.checkOut();
        try {
            pool.startTransaction(connection);
            PreparedStatement preparedStatement = connection.prepareStatement(FIND_QUANTITY_SQL);
            preparedStatement.setLong(1, productId);
            preparedStatement.setLong(2, storageId);
            ResultSet resultSet = preparedStatement.executeQuery();
            pool.commitTransaction(connection);
            return resultSet.isBeforeFirst();
        } catch (SQLException e) {
            throw new EntityNotFoundException();
        } finally {
            pool.checkIn(connection);
        }
    }

    private Storage mapStorage(ResultSet resultSet) {
        try {
            Long storageId = resultSet.getLong("id");
            String name = resultSet.getString("name");
            String address = resultSet.getString("address");
            List<Long> shops = getShopsByStorage(storageId);
            HashMap<Long, ProductWithQuantity> productQuantities = getQuantities(storageId);
            return new Storage(storageId, name, address, shops, productQuantities);
        } catch (SQLException e) {
            throw new EntityNotFoundException();
        }
    }
}
