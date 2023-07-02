package com.company;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class JDBCConnectionPool extends ObjectPool<Connection> {
    private final String dbLink;
    private final String username;
    private final String password;

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
            e.printStackTrace();
            return (null);
        }
    }

    @Override
    public void expire(Connection o) {
        try {
            o.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean validate(Connection o) {
        try {
            return (!o.isClosed());
        } catch (SQLException e) {
            e.printStackTrace();
            return (false);
        }
    }
}
