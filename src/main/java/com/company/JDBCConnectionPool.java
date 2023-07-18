package com.company;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class JDBCConnectionPool extends ObjectPool<Connection> {
    private final String dbLink;
    private final String username;
    private final String password;
    private final static Logger logger = LogManager.getLogger(JDBCConnectionPool.class);

    public JDBCConnectionPool(String driver, String dbLink, String username, String password) {
        super();
        try {
            Class.forName(driver);
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.dbLink = dbLink;
        this.username = username;
        this.password = password;
    }

    @Override
    protected Connection create() {
        try {
            return (DriverManager.getConnection(dbLink, username, password));
        } catch (SQLException e) {
            logger.error("Connection creation error", e);
            return null;
        }
    }

    @Override
    public void expire(Connection connection) {
        try {
            connection.close();
        } catch (SQLException e) {
            logger.error("Connection closing error", e);
        }
    }

    @Override
    public boolean validate(Connection connection) {
        try {
            return (!connection.isClosed());
        } catch (SQLException e) {
            logger.error("Connection validation error", e);
            return false;
        }
    }

    public void startTransaction(Connection connection) {
        try {
            connection.setAutoCommit(false);
        } catch (SQLException e) {
            logger.error("Set auto commit false error", e);
        }
    }

    public void commitTransaction(Connection connection) {
        try {
            connection.commit();
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException ex) {
                logger.error("Commit rollback error", ex);
            }
            logger.error("Commit error", e);
        }
        try {
            connection.setAutoCommit(true);
        } catch (SQLException exc) {
            logger.error("Set auto commit true error", exc);
        }
    }
}
