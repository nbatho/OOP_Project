package main.java.test;

import main.java.config.DatabaseConnection;
import main.java.service.impl.UserServiceImpl;
import main.java.model.User;

public class AuthTest {
    public static void main(String[] args) {
        System.out.println("=== Testing MD5 Authentication ===");
        
        // Test database connection
        try {
            DatabaseConnection.getConnection();
            System.out.println("✅ Database connected successfully!");
        } catch (Exception e) {
            System.out.println("❌ Database connection failed: " + e.getMessage());
            return;
        }
        
        UserServiceImpl userService = new UserServiceImpl();
        
        // Test 1: Create user with MD5 password
        System.out.println("\n--- Test 1: Create User with MD5 Hash ---");
        User testUser = new User();
        testUser.setFullName("Test User Auth");
        testUser.setEmail("test.auth@example.com");
        testUser.setPasswordHash("password123"); // Will be hashed to MD5
        
        boolean created = userService.createUser(testUser);
        System.out.println("User created: " + created);
        
        if (created) {
            // Test 2: Authenticate with correct password
            System.out.println("\n--- Test 2: Authenticate with Correct Password ---");
            User authUser = userService.authenticateUser("test.auth@example.com", "password123");
            System.out.println("Authentication result: " + (authUser != null ? "SUCCESS" : "FAILED"));
            if (authUser != null) {
                System.out.println("Authenticated user: " + authUser.getFullName());
            }
            
            // Test 3: Authenticate with wrong password
            System.out.println("\n--- Test 3: Authenticate with Wrong Password ---");
            User wrongAuth = userService.authenticateUser("test.auth@example.com", "wrongpassword");
            System.out.println("Authentication result: " + (wrongAuth != null ? "SUCCESS" : "FAILED"));
            
            // Clean up
            if (authUser != null) {
                userService.deleteUser(authUser.getUserId());
                System.out.println("\n✅ Test user cleaned up");
            }
        }
        
        System.out.println("\n=== MD5 Authentication Test Completed ===");
    }
}