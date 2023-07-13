package com.company.core.services.persistenceservices.dbimpl;

import com.company.JDBCConnectionPool;
import com.company.core.models.EntityNotFoundException;
import com.company.core.models.Storage;
import com.company.core.models.goods.Product;
import com.company.core.models.goods.ProductWithQuantity;
import com.company.core.services.persistenceservices.PersistenceInterface;

import java.sql.*;
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
        Connection con = pool.checkOut();
        try {
            pool.startTransaction(con);
            PreparedStatement prep = con.prepareStatement(SAVE_SQL, Statement.RETURN_GENERATED_KEYS);
            prep.setString(1, entity.getName());
            prep.setString(2, entity.getAddress());
            prep.executeUpdate();
            pool.commitTransaction(con);

            ResultSet rs = prep.getGeneratedKeys();
            rs.next();
            Long id = rs.getLong(1);
            entity.setId(id);

            for (Long shopId : entity.getShops()) {
                addBond(shopId, id);
            }

            for (ProductWithQuantity p : entity.getProductQuantities().values()) {
                addQuantity(p, entity.getId());
            }

            return entity;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            pool.checkIn(con);
        }
    }

    @Override
    public Storage findById(Long id) {
        Connection con = pool.checkOut();
        try {
            pool.startTransaction(con);
            PreparedStatement p = con.prepareStatement(FIND_BY_ID_SQL);
            p.setLong(1, id);
            ResultSet rs = p.executeQuery();
            pool.commitTransaction(con);
            rs.next();
            Storage storage = mapStorage(rs);
            return storage;
        } catch (SQLException e) {
            throw new EntityNotFoundException();
        } finally {
            pool.checkIn(con);
        }
    }

    @Override
    public List<Storage> findAll() {
        Connection con = pool.checkOut();
        try {
            pool.startTransaction(con);
            List<Storage> storages = new ArrayList<>();
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(FIND_ALL_SQL);
            pool.commitTransaction(con);
            while (rs.next()) {
                Storage storage = mapStorage(rs);
                storages.add(storage);
            }
            return storages;
        } catch (SQLException e) {
            throw new EntityNotFoundException();
        } finally {
            pool.checkIn(con);
        }
    }

    @Override
    public Storage update(Storage entity) {
        Connection con = pool.checkOut();
        try {
            pool.startTransaction(con);
            PreparedStatement prep = con.prepareStatement(UPDATE_SQL);
            prep.setLong(1, entity.getId());
            prep.setString(2, entity.getName());
            prep.setString(3, entity.getAddress());
            prep.setLong(4, entity.getId());

            List<Long> newShops = entity.getShops();
            List<Long> oldShops = getShopsByStorage(entity.getId());

            addNewShops(entity, newShops, oldShops);

            deleteOldShops(entity, newShops, oldShops);

            HashMap<Long, ProductWithQuantity> productQuantities = entity.getProductQuantities();

            saveQuantities(entity, productQuantities);

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
        Connection con = pool.checkOut();
        try {
            pool.startTransaction(con);
            List<Long> shops = new ArrayList<>();
            PreparedStatement p = con.prepareStatement(GET_SHOPS_BY_STORAGES_SQL);
            p.setLong(1, storageId);
            ResultSet rs = p.executeQuery();
            pool.commitTransaction(con);
            while (rs.next()) {
                shops.add(rs.getLong(1));
            }
            return shops;
        } catch (SQLException e) {
            throw new EntityNotFoundException();
        } finally {
            pool.checkIn(con);
        }
    }

    private HashMap<Long, ProductWithQuantity> getQuantities(Long storageId) {
        Connection con = pool.checkOut();
        try {
            pool.startTransaction(con);
            HashMap<Long, ProductWithQuantity> productQuantities = new HashMap<>();
            PreparedStatement prep = con.prepareStatement(GET_QUANTITIES_SQL);
            prep.setLong(1, storageId);
            ResultSet rs = prep.executeQuery();
            pool.commitTransaction(con);
            while (rs.next()) {
                Product product = productPersistenceService.findById(rs.getLong("product_id"));
                Integer quantity = rs.getInt("quantity");
                productQuantities.put(product.getId(), new ProductWithQuantity(product, quantity));
            }
            return productQuantities;
        } catch (SQLException e) {
            throw new EntityNotFoundException();
        } finally {
            pool.checkIn(con);
        }
    }

    private void updateQuantity(ProductWithQuantity productWithQuantity, Long storageId) {
        Connection con = pool.checkOut();
        try {
            pool.startTransaction(con);
            PreparedStatement prep = con.prepareStatement(UPDATE_QUANTITY_SQL);
            prep.setInt(1, productWithQuantity.getQuantity());
            prep.setLong(2, storageId);
            prep.setLong(3, productWithQuantity.getProduct().getId());
            prep.executeUpdate();
            pool.commitTransaction(con);
        } catch (SQLException e) {
            throw new EntityNotFoundException();
        } finally {
            pool.checkIn(con);
        }
    }

    private void addQuantity(ProductWithQuantity productWithQuantity, Long storageId) {
        Connection con = pool.checkOut();
        try {
            pool.startTransaction(con);
            PreparedStatement prep = con.prepareStatement(ADD_QUANTITY_SQL);
            prep.setLong(1, storageId);
            prep.setLong(2, productWithQuantity.getProduct().getId());
            prep.setInt(3, productWithQuantity.getQuantity());
            prep.executeUpdate();
            pool.commitTransaction(con);
        } catch (SQLException e) {
            throw new EntityNotFoundException();
        } finally {
            pool.checkIn(con);
        }
    }

    private boolean quantityIsPresent(Long productId, Long storageId) {
        Connection con = pool.checkOut();
        try {
            pool.startTransaction(con);
            PreparedStatement prep = con.prepareStatement(FIND_QUANTITY_SQL);
            prep.setLong(1, productId);
            prep.setLong(2, storageId);
            ResultSet rs = prep.executeQuery();
            pool.commitTransaction(con);
            return rs.isBeforeFirst();
        } catch (SQLException e) {
            throw new EntityNotFoundException();
        } finally {
            pool.checkIn(con);
        }
    }

    private Storage mapStorage(ResultSet rs) {
        try {
            Long storageId = rs.getLong("id");
            String name = rs.getString("name");
            String address = rs.getString("address");
            List<Long> shops = getShopsByStorage(storageId);
            HashMap<Long, ProductWithQuantity> productQuantities = getQuantities(storageId);
            return new Storage(storageId, name, address, shops, productQuantities);
        } catch (SQLException e) {
            throw new EntityNotFoundException();
        }
    }
}
