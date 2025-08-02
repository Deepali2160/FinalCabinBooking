package com.yash.cabinbooking.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/cabinbooking";
    private static final String USER = "root";
    private static final String PASSWORD = "root";

    private static Connection connection;

    // Private constructor to prevent instantiation
    private DBConnection() {}

    public static synchronized Connection getConnection() {

        try {
            if (connection == null || connection.isClosed()) {
                try {
                    // Load MySQL JDBC Driver
                    Class.forName("com.mysql.cj.jdbc.Driver");

                    // Create connection
                    connection = DriverManager.getConnection(URL, USER, PASSWORD);

                    // Disable auto-commit to enable transactions
                    connection.setAutoCommit(false);

                    System.out.println("Database connection established successfully");
                } catch (ClassNotFoundException e) {
                    System.err.println("MySQL JDBC Driver not found!");
                    e.printStackTrace();
                    throw new RuntimeException("Database driver not found", e);
                } catch (SQLException e) {
                    System.err.println("Failed to create database connection!");
                    e.printStackTrace();
                    throw new RuntimeException("Failed to connect to database", e);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error checking connection status");
            e.printStackTrace();
            throw new RuntimeException("Error checking database connection", e);
        }
        return connection;

    }

    public static synchronized void commitConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.commit();
                System.out.println("Transaction committed successfully");
            }
        } catch (SQLException e) {
            System.err.println("Error committing transaction");
            e.printStackTrace();
            throw new RuntimeException("Failed to commit transaction", e);
        }
    }

    public static synchronized void rollbackConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.rollback();
                System.out.println("Transaction rolled back");
            }
        } catch (SQLException e) {
            System.err.println("Error rolling back transaction");
            e.printStackTrace();
            throw new RuntimeException("Failed to rollback transaction", e);
        }
    }

    public static synchronized void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                // Commit any pending transactions before closing
                connection.commit();
                connection.close();
                System.out.println("Database connection closed successfully");
            }
        } catch (SQLException e) {
            System.err.println("Error closing database connection");
            e.printStackTrace();
            try {
                // Try to rollback if there was an error during close
                if (connection != null && !connection.isClosed()) {
                    connection.rollback();
                }
            } catch (SQLException ex) {
                System.err.println("Error during rollback on close");
                ex.printStackTrace();
            }
            throw new RuntimeException("Failed to close database connection", e);
        } finally {
            connection = null; // Ensure connection is dereferenced
        }
    }

    // Additional helper method for testing connection
    public static boolean testConnection() {
        try (Connection testConn = DriverManager.getConnection(URL, USER, PASSWORD)) {
            return testConn.isValid(2); // 2 second timeout
        } catch (SQLException e) {
            System.err.println("Connection test failed");
            e.printStackTrace();
            return false;
        }
    }
}