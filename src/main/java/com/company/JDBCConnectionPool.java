package com.company;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import com.company.core.exceptions.JDBCInitializationException;
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
            logger.error("DB Driver not found", e);
            throw new JDBCInitializationException();
        }
        this.dbLink = dbLink;
        this.username = username;
        this.password = password;
        Connection connection = create();
        expire(connection);
    }

    @Override
    protected Connection create() {
        try {
            return (DriverManager.getConnection(dbLink, username, password));
        } catch (SQLException e) {
            System.out.println("why is it not writing down in a log bro");
            logger.error("Connection creation error", e);
            throw new JDBCInitializationException();
        }
    }

    @Override
    public void expire(Connection connection) {
        try {
            connection.close();
        } catch (SQLException e) {
            logger.error("Connection closing error", e);
            throw new JDBCInitializationException();
        }
    }

    @Override
    public boolean validate(Connection connection) {
        try {
            return (!connection.isClosed());
        } catch (SQLException e) {
            logger.error("Connection validation error", e);
            throw new JDBCInitializationException();
        }
    }

    public void startTransaction(Connection connection) {
        try {
            connection.setAutoCommit(false);
        } catch (SQLException e) {
            logger.error("Connection validation error", e);
            throw new JDBCInitializationException();
        }
    }

    public void commitTransaction(Connection connection) {
        try {
            connection.commit();
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException ex) {
                logger.error("Connection validation error", ex);
                throw new JDBCInitializationException();
            }
            logger.error("Commit error", e);
            throw new JDBCInitializationException();
        }
        try {
            connection.setAutoCommit(true);
        } catch (SQLException exc) {
            logger.error("Set auto commit true error", exc);
            throw new JDBCInitializationException();
        }
    }
}
