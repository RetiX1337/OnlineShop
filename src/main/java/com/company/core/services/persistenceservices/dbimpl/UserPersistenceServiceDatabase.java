package com.company.core.services.persistenceservices.dbimpl;

import com.company.JDBCConnectionPool;
import com.company.core.exceptions.EntityNotFoundException;
import com.company.core.exceptions.EntityNotSavedException;
import com.company.core.models.user.User;
import com.company.core.models.user.UserRole;
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

public class UserPersistenceServiceDatabase implements PersistenceInterface<User> {
    private final JDBCConnectionPool pool;
    private final String DELETE_SQL = "DELETE FROM users WHERE users.id = ?";
    private final String UPDATE_SQL = "UPDATE users SET username = ?, email = ?, password_hash = ? WHERE users.id = ?";
    private final String ALL_SQL = "SELECT * FROM users";
    private final String SAVE_SQL = "INSERT INTO users (username, email, password_hash) VALUES (?, ?, ?)";
    private final String FIND_ROLES_SQL = "SELECT user_roles.user_role FROM user_roles WHERE user_roles.user_id = ?";
    private final String FIND_BY_ID_SQL = "SELECT users.id, users.username, users.email, users.password_hash FROM users WHERE users.id = ?";
    private final String FIND_ALL_SQL = "SELECT users.id, users.username, users.email, users.password_hash FROM users";

    public UserPersistenceServiceDatabase(JDBCConnectionPool pool) {
        this.pool = pool;
    }

    @Override
    public User save(User entity) {
        Connection connection = pool.checkOut();
        try {
            pool.startTransaction(connection);
            PreparedStatement preparedStatement = connection.prepareStatement(SAVE_SQL, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, entity.getUsername());
            preparedStatement.setString(2, entity.getEmail());
            preparedStatement.setString(3, entity.getPasswordHash());
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
    public User findById(Long id) {
        Connection connection = pool.checkOut();
        try {
            pool.startTransaction(connection);
            PreparedStatement preparedStatement = connection.prepareStatement(FIND_BY_ID_SQL);
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            pool.commitTransaction(connection);
            resultSet.next();
            return mapUser(resultSet);
        } catch (SQLException e) {
            throw new EntityNotFoundException();
        } finally {
            pool.checkIn(connection);
        }
    }

    @Override
    public List<User> findAll() {
        Connection connection = pool.checkOut();
        try {
            pool.startTransaction(connection);
            List<User> userList = new ArrayList<>();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(FIND_ALL_SQL);
            pool.commitTransaction(connection);
            while (resultSet.next()) {
                User user = mapUser(resultSet);
                userList.add(user);
            }
            return userList;
        } catch (SQLException e) {
            throw new EntityNotFoundException();
        } finally {
            pool.checkIn(connection);
        }
    }

    @Override
    public User update(User entity) {
        Connection connection = pool.checkOut();
        try {
            pool.startTransaction(connection);
            PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_SQL);
            preparedStatement.setString(1, entity.getUsername());
            preparedStatement.setString(2, entity.getEmail());
            preparedStatement.setString(3, entity.getPasswordHash());
            preparedStatement.setLong(4, entity.getId());
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

    private User mapUser(ResultSet resultSet) {
        try {
            User user = new User();

            user.setId(resultSet.getLong("id"));
            user.setUsername(resultSet.getString("username"));
            user.setEmail(resultSet.getString("email"));
            user.setPasswordHash(resultSet.getString("password_hash"));
            user.setRoles(getUserRoles(user.getId()));

            return user;
        } catch (SQLException e) {
            throw new EntityNotFoundException();
        }
    }

    private Set<UserRole> getUserRoles(Long userId) {
        try {
            Connection connection = pool.checkOut();
            PreparedStatement preparedStatement = connection.prepareStatement(FIND_ROLES_SQL);
            Set<UserRole> userRoleSet = new HashSet<>();
            preparedStatement.setLong(1, userId);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                UserRole userRole = UserRole.valueOf(resultSet.getString("user_role"));
                userRoleSet.add(userRole);
            }
            return userRoleSet;
        } catch (SQLException e) {
            throw new EntityNotFoundException();
        }
    }
}
