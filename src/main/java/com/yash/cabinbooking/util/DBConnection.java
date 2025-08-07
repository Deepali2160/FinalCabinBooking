package com.yash.cabinbooking.util;

import org.apache.commons.dbcp2.BasicDataSource;
import java.sql.Connection;
import java.sql.SQLException;


public final class DBConnection {
    private static volatile BasicDataSource dataSource;
    private static final Object lock = new Object();

    // Private constructor to prevent instantiation
    private DBConnection() {
        throw new AssertionError("Cannot instantiate utility class");
    }

    /**
     * Initializes the connection pool if not already initialized
     */
    private static void initializePool() {
        if (dataSource == null) {
            synchronized (lock) {
                if (dataSource == null) {
                    try {
                        BasicDataSource ds = new BasicDataSource();
                        ds.setUrl("jdbc:mysql://localhost:3306/cabinbooking?useSSL=false");
                        ds.setUsername("root");
                        ds.setPassword("manav1904");

                        // Connection pool configuration
                        ds.setMinIdle(5);
                        ds.setMaxIdle(20);
                        ds.setMaxTotal(100);
                        ds.setMaxWaitMillis(10000); // 10 seconds

                        // Connection validation
                        ds.setValidationQuery("SELECT 1");
                        ds.setTestOnBorrow(true);
                        ds.setTestWhileIdle(true);
                        ds.setTimeBetweenEvictionRunsMillis(30000);

                        // Connection timeouts
                        ds.setRemoveAbandonedTimeout(60); // 60 seconds
                        ds.setRemoveAbandonedOnBorrow(true);
                        ds.setRemoveAbandonedOnMaintenance(true);

                        // MySQL-specific optimizations
                        ds.setDriverClassName("com.mysql.cj.jdbc.Driver");
                        ds.addConnectionProperty("useUnicode", "true");
                        ds.addConnectionProperty("characterEncoding", "UTF-8");
                        ds.addConnectionProperty("serverTimezone", "UTC");

                        dataSource = ds;
                        System.out.println("Database connection pool initialized successfully");
                    } catch (Exception e) {
                        throw new RuntimeException("Failed to initialize connection pool", e);
                    }
                }
            }
        }
    }

    /**
     * Gets a database connection from the pool
     * @return Connection object
     * @throws SQLException if connection fails
     */
    public static Connection getConnection() throws SQLException {
        if (dataSource == null) {
            initializePool();
        }
        Connection conn = dataSource.getConnection();
        conn.setAutoCommit(false); // Enable transaction management
        return conn;
    }

    /**
     * Commits the transaction and closes the connection
     * @param conn Connection to commit and close
     */
    public static void commitAndClose(Connection conn) {
        if (conn != null) {
            try {
                if (!conn.isClosed()) {
                    conn.commit();
                }
            } catch (SQLException e) {
                System.err.println("Error committing transaction");
                e.printStackTrace();
                rollbackAndClose(conn);
                throw new RuntimeException("Commit failed", e);
            } finally {
                closeQuietly(conn);
            }
        }
    }

    /**
     * Rolls back the transaction and closes the connection
     * @param conn Connection to rollback and close
     */
    public static void rollbackAndClose(Connection conn) {
        if (conn != null) {
            try {
                if (!conn.isClosed()) {
                    conn.rollback();
                }
            } catch (SQLException e) {
                System.err.println("Error rolling back transaction");
                e.printStackTrace();
            } finally {
                closeQuietly(conn);
            }
        }
    }

    /**
     * Closes the connection quietly without throwing exceptions
     * @param conn Connection to close
     */
    public static void closeQuietly(Connection conn) {
        if (conn != null) {
            try {
                if (!conn.isClosed()) {
                    conn.close();
                }
            } catch (SQLException e) {
                System.err.println("Error closing connection");
                e.printStackTrace();
            }
        }
    }

    /**
     * Closes the connection pool and releases all resources
     */
    public static void shutdown() {
        if (dataSource != null) {
            try {
                dataSource.close();
                System.out.println("Database connection pool shutdown successfully");
            } catch (SQLException e) {
                System.err.println("Error shutting down connection pool");
                e.printStackTrace();
            }
        }
    }

    /**
     * Tests if the connection pool is working
     * @return true if connection test succeeds
     */
    public static boolean testConnection() {
        Connection conn = null;
        try {
            conn = getConnection();
            return conn.isValid(2); // 2 second timeout
        } catch (SQLException e) {
            System.err.println("Connection test failed");
            e.printStackTrace();
            return false;
        } finally {
            closeQuietly(conn);
        }
    }

    /**
     * Gets connection pool statistics
     * @return String containing pool statistics
     */
    public static String getPoolStats() {
        if (dataSource == null) {
            return "Connection pool not initialized";
        }
        return String.format(
                "Active: %d, Idle: %d, Total: %d",
                dataSource.getNumActive(),
                dataSource.getNumIdle(),
                dataSource.getMaxTotal()
        );
    }
}