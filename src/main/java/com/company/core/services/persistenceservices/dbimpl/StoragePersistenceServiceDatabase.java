package com.company.core.services.persistenceservices.dbimpl;

import com.company.JDBCConnectionPool;
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
    private Long idCounter;

    private final String FIND_QUANTITY_SQL = "SELECT * FROM product_storage WHERE product_id = ? AND storage_id = ?";
    private final String UPDATE_QUANTITY_SQL = "UPDATE product_storage SET quantity = ? WHERE storage_id = ? AND product_id = ?";
    private final String ADD_QUANTITY_SQL = "INSERT INTO product_storage (storage_id, product_id, quantity) VALUES (?, ?, ?)";
    private final String SAVE_SQL = "INSERT INTO storage (id, name, address) VALUES (?, ?, ?)";
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
        initCounter();
    }

    private void initCounter() {
        try {
            Connection con = pool.checkOut();
            ResultSet rs = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY).executeQuery(ALL_SQL);
            if (rs.isBeforeFirst()) {
                rs.last();
                idCounter = Long.valueOf(rs.getInt("id")) + 1;
                pool.checkIn(con);
            } else {
                idCounter = 1L;
            }
            pool.checkIn(con);
            System.out.println(idCounter);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Storage save(Storage entity) {
        entity.setId(idCounter);
        try {
            Connection con = pool.checkOut();
            PreparedStatement prep = con.prepareStatement(SAVE_SQL);
            prep.setLong(1, entity.getId());
            prep.setString(2, entity.getName());
            prep.setString(3, entity.getAddress());

            for (Long shopId : entity.getShops()) {
                addBond(shopId, idCounter);
            }

            for (ProductWithQuantity p : entity.getProductQuantities().values()) {
                addQuantity(p, entity.getId());
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
    public Storage findById(Long id) {
        try {
            Connection con = pool.checkOut();
            PreparedStatement p = con.prepareStatement(FIND_BY_ID_SQL);
            p.setLong(1, id);
            ResultSet rs = p.executeQuery();
            rs.next();
            String name = rs.getString("name");
            String address = rs.getString("address");
            List<Long> shops = getShopsByStorage(id);
            HashMap<Long, ProductWithQuantity> productQuantities = getQuantities(id);
            pool.checkIn(con);
            return new Storage(id, name, address, shops, productQuantities);
        } catch (SQLException e) {
            throw new RuntimeException(e);
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
                Long storageId = rs.getLong("id");
                String name = rs.getString("name");
                String address = rs.getString("address");
                List<Long> shops = getShopsByStorage(storageId);
                HashMap<Long, ProductWithQuantity> productQuantities = getQuantities(storageId);
                storages.add(new Storage(storageId, name, address, shops, productQuantities));
            }
            pool.checkIn(con);
            return storages;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Storage update(Storage entity, Long id) {
        try {
            Connection con = pool.checkOut();
            PreparedStatement prep = con.prepareStatement(UPDATE_SQL);
            prep.setLong(1, id);
            prep.setString(2, entity.getName());
            prep.setString(3, entity.getAddress());
            prep.setLong(4, id);

            List<Long> newShops = entity.getShops();
            List<Long> oldShops = getShopsByStorage(id);

            for (Long newShopId : newShops) {
                if (!oldShops.contains(newShopId)) {
                    addBond(id, newShopId);
                }
            }

            for (Long oldShopId : oldShops) {
                if (!newShops.contains(oldShopId)) {
                    deleteBond(id, oldShopId);
                }
            }

            HashMap<Long, ProductWithQuantity> productQuantities = entity.getProductQuantities();
            for (ProductWithQuantity productWithQuantity : productQuantities.values()) {
                if (!quantityIsPresent(productWithQuantity.getProduct().getId(), id)) {
                    addQuantity(productWithQuantity, id);
                } else {
                    updateQuantity(productWithQuantity, id);
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
            throw new RuntimeException(e);
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
            throw new RuntimeException(e);
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
            throw new RuntimeException(e);
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
            throw new RuntimeException(e);
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
            throw new RuntimeException(e);
        }
    }
}
