package com.employee.repository;

import com.employee.model.User;
import java.sql.*;
import java.util.Optional;

public class UserRepository {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/employeeData";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "password";

    static {
        try {
            System.out.println("Loading MySQL JDBC driver...");
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("MySQL JDBC driver loaded successfully");
            
            // Test connection
            try (Connection conn = getConnection()) {
                System.out.println("Successfully connected to database");
            }
            
            createTableIfNotExists();
            createDefaultAdminIfNotExists();
            createDefaultEmployeeIfNotExists();
        } catch (ClassNotFoundException e) {
            System.err.println("MySQL JDBC Driver not found: " + e.getMessage());
            throw new RuntimeException("MySQL JDBC Driver not found", e);
        } catch (SQLException e) {
            System.err.println("Database connection error: " + e.getMessage());
            throw new RuntimeException("Database connection error", e);
        }
    }

    private static void createTableIfNotExists() {
        System.out.println("Checking if users table exists...");
        String sql = "CREATE TABLE IF NOT EXISTS users (" +
                    "id INT PRIMARY KEY AUTO_INCREMENT, " +
                    "username VARCHAR(50) UNIQUE NOT NULL, " +
                    "password VARCHAR(100) NOT NULL, " +
                    "role VARCHAR(20) NOT NULL" +
                    ")";
        
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
            System.out.println("Users table created or already exists");
        } catch (SQLException e) {
            System.err.println("Error creating users table: " + e.getMessage());
            throw new RuntimeException("Error creating users table", e);
        }
    }

    private static void createDefaultAdminIfNotExists() {
        System.out.println("Checking if default admin user exists...");
        String checkSql = "SELECT COUNT(*) FROM users WHERE username = 'admin'";
        String insertSql = "INSERT INTO users (username, password, role) VALUES (?, ?, ?)";
        
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(checkSql)) {
            
            rs.next();
            int count = rs.getInt(1);
            
            if (count == 0) {
                System.out.println("Creating default admin user...");
                try (PreparedStatement pstmt = conn.prepareStatement(insertSql)) {
                    pstmt.setString(1, "admin");
                    pstmt.setString(2, "admin123");
                    pstmt.setString(3, "ADMIN");
                    pstmt.executeUpdate();
                    System.out.println("Default admin user created successfully");
                }
            } else {
                System.out.println("Default admin user already exists");
            }
        } catch (SQLException e) {
            System.err.println("Error creating default admin user: " + e.getMessage());
            throw new RuntimeException("Error creating default admin user", e);
        }
    }

    private static void createDefaultEmployeeIfNotExists() {
        System.out.println("Checking if default employee user exists...");
        String checkSql = "SELECT COUNT(*) FROM users WHERE username = 'employee1'";
        String insertSql = "INSERT INTO users (username, password, role) VALUES (?, ?, ?)";
        
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(checkSql)) {
            
            rs.next();
            int count = rs.getInt(1);
            
            if (count == 0) {
                System.out.println("Creating default employee user...");
                try (PreparedStatement pstmt = conn.prepareStatement(insertSql)) {
                    pstmt.setString(1, "employee1");
                    pstmt.setString(2, "employee123");
                    pstmt.setString(3, "EMPLOYEE");
                    pstmt.executeUpdate();
                    System.out.println("Default employee user created successfully");
                }
            } else {
                System.out.println("Default employee user already exists");
            }
        } catch (SQLException e) {
            System.err.println("Error creating default employee user: " + e.getMessage());
            throw new RuntimeException("Error creating default employee user", e);
        }
    }

    private static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
    }

    public Optional<User> findByUsername(String username) {
        System.out.println("Searching for user: " + username);
        String sql = "SELECT * FROM users WHERE username = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                User user = new User(
                    rs.getString("username"),
                    rs.getString("password"),
                    rs.getString("role")
                );
                System.out.println("User found: " + user.getUsername());
                return Optional.of(user);
            }
            System.out.println("User not found");
            return Optional.empty();
        } catch (SQLException e) {
            System.err.println("Error finding user by username: " + e.getMessage());
            throw new RuntimeException("Error finding user by username", e);
        }
    }

    public void save(User user) {
        System.out.println("Saving user: " + user.getUsername());
        String sql = "INSERT INTO users (username, password, role) VALUES (?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, user.getUsername());
            pstmt.setString(2, user.getPassword());
            pstmt.setString(3, user.getRole());
            pstmt.executeUpdate();
            System.out.println("User saved successfully");
        } catch (SQLException e) {
            System.err.println("Error saving user: " + e.getMessage());
            throw new RuntimeException("Error saving user", e);
        }
    }
} 