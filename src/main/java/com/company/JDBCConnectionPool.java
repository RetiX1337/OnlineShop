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

    public void startTransaction(Connection con) {
        try {
            con.setAutoCommit(false);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void commitTransaction(Connection con) {
        try {
            con.commit();
        } catch (SQLException e) {
            try {
                con.rollback();
            } catch (SQLException ex) {
                throw new RuntimeException("failed to rollback", ex);
            }
            throw new RuntimeException("failed to commit", e);
        }
        try {
            con.setAutoCommit(true);
        } catch (SQLException exc) {
            throw new RuntimeException("failed to set auto-commit true",exc);
        }
    }
/*
    public synchronized Connection checkOut() {
        Connection conn = checkOut();
        try {
            conn.setAutoCommit(false);
            return conn;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
*/

    /*
    public synchronized void checkIn(Connection conn) {
        boolean flag = false;
        try {
            conn.commit();
        } catch (SQLException commitEx) {
            try {
                conn.rollback();
            } catch (SQLException rollbackEx) {
                throw new RuntimeException("Failed to rollback transaction.", rollbackEx);
            }
            throw new RuntimeException("Failed to commit transaction.", commitEx);
        } finally {
            try {
                conn.setAutoCommit(true);
            } catch (SQLException e) {
                flag = true;
            } finally {
                this.checkIn(conn);
            }
        }
        if (flag) {
            throw new RuntimeException("Failed to set connections' Auto Commit to 'true'.");
        }
    }
     */
}
