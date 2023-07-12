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
        try {
            Connection con = pool.checkOut();
            PreparedStatement prep = con.prepareStatement(SAVE_SQL, Statement.RETURN_GENERATED_KEYS);
            prep.setString(1, entity.getName());
            prep.setString(2, entity.getAddress());
            prep.executeUpdate();

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

            pool.checkIn(con);
            return entity;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Storage findById(Long id) {
        try {
            Connection con = pool.checkOut();
            PreparedStatement p = con.prepareStatement(FIND_BY_ID_SQL);
            p.setLong(1, id);
            ResultSet rs = p.executeQuery();
            rs.next();
            Storage storage = mapStorage(rs);
            pool.checkIn(con);
            return storage;
        } catch (SQLException e) {
            throw new EntityNotFoundException();
        }
    }

    @Override
    public List<Storage> findAll() {
        try {
            List<Storage> storages = new ArrayList<>();
            Connection con = pool.checkOut();
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(FIND_ALL_SQL);
            while (rs.next()) {
                Storage storage = mapStorage(rs);
                storages.add(storage);
            }
            pool.checkIn(con);
            return storages;
        } catch (SQLException e) {
            throw new EntityNotFoundException();
        }
    }

    @Override
    public Storage update(Storage entity) {
        try {
            Connection con = pool.checkOut();
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
            pool.checkIn(con);
            return entity;
        } catch (SQLException e) {
            throw new EntityNotFoundException();
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
            throw new EntityNotFoundException();
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
        try {
            Connection con = pool.checkOut();
            PreparedStatement prep = con.prepareStatement(ADD_BOND_SQL);
            prep.setLong(1, shopId);
            prep.setLong(2, storageId);
            prep.executeUpdate();
            pool.checkIn(con);
        } catch (SQLException e) {
            throw new EntityNotFoundException();
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
            throw new EntityNotFoundException();
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
        try {
            List<Long> shops = new ArrayList<>();
            Connection con = pool.checkOut();
            PreparedStatement p = con.prepareStatement(GET_SHOPS_BY_STORAGES_SQL);
            p.setLong(1, storageId);
            ResultSet rs = p.executeQuery();
            while (rs.next()) {
                shops.add(rs.getLong(1));
            }
            pool.checkIn(con);
            return shops;
        } catch (SQLException e) {
            throw new EntityNotFoundException();
        }
    }

    private HashMap<Long, ProductWithQuantity> getQuantities(Long storageId) {
        try {
            HashMap<Long, ProductWithQuantity> productQuantities = new HashMap<>();

            Connection con = pool.checkOut();
            PreparedStatement prep = con.prepareStatement(GET_QUANTITIES_SQL);
            prep.setLong(1, storageId);
            ResultSet rs = prep.executeQuery();
            pool.checkIn(con);
            while (rs.next()) {
                Product product = productPersistenceService.findById(rs.getLong("product_id"));
                Integer quantity = rs.getInt("quantity");
                productQuantities.put(product.getId(), new ProductWithQuantity(product, quantity));
            }
            return productQuantities;
        } catch (SQLException e) {
            throw new EntityNotFoundException();
        }
    }

    private void updateQuantity(ProductWithQuantity productWithQuantity, Long storageId) {
        try {
            Connection con = pool.checkOut();
            PreparedStatement prep = con.prepareStatement(UPDATE_QUANTITY_SQL);
            prep.setInt(1, productWithQuantity.getQuantity());
            prep.setLong(2, storageId);
            prep.setLong(3, productWithQuantity.getProduct().getId());
            prep.executeUpdate();
            pool.checkIn(con);
        } catch (SQLException e) {
            throw new EntityNotFoundException();
        }
    }

    private void addQuantity(ProductWithQuantity productWithQuantity, Long storageId) {
        try {
            Connection con = pool.checkOut();
            PreparedStatement prep = con.prepareStatement(ADD_QUANTITY_SQL);
            prep.setLong(1, storageId);
            prep.setLong(2, productWithQuantity.getProduct().getId());
            prep.setInt(3, productWithQuantity.getQuantity());
            prep.executeUpdate();
            pool.checkIn(con);
        } catch (SQLException e) {
            throw new EntityNotFoundException();
        }
    }

    private boolean quantityIsPresent(Long productId, Long storageId) {
        try {
            Connection con = pool.checkOut();
            PreparedStatement prep = con.prepareStatement(FIND_QUANTITY_SQL);
            prep.setLong(1, productId);
            prep.setLong(2, storageId);
            ResultSet rs = prep.executeQuery();
            pool.checkIn(con);
            return rs.isBeforeFirst();
        } catch (SQLException e) {
            throw new EntityNotFoundException();
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
