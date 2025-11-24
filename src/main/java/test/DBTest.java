package main.java.test;

import java.util.UUID;
import main.java.config.DatabaseConnection;
import main.java.model.*;
import main.java.repository.*;

public class DBTest {
    public static void main(String[] args) {
        System.out.println("=== Testing Database Connection ===");
        DatabaseConnection.testConnection();
        
        System.out.println("\n=== Testing User Repository ===");
        testUserRepository();
        System.out.println("\n=== Testing Role Repository ===");
        testRoleRepository();
    }
    
    private static void testUserRepository() {
        UserRepository userRepo = new UserRepository();
        
        // Test create user
        User testUser = new User();
        testUser.setUserId(UUID.randomUUID().toString());
        testUser.setFullName("Test User");
        testUser.setEmail("test@example.com");
        testUser.setPasswordHash("hashedpassword123");
        
        boolean created = userRepo.createUser(testUser);
        System.out.println("User created: " + created);
        
        if (created) {
            // Test get user by email
            User foundUser = userRepo.getUserByEmail("test@example.com");
            System.out.println("Found user: " + foundUser);
            
            // Test get all users
            System.out.println("Total users: " + userRepo.getAllUsers().size());
            
            // Test delete user
            boolean deleted = userRepo.deleteUser(testUser.getUserId());
            System.out.println("User deleted: " + deleted);
        }
    }
    

    
    private static void testRoleRepository() {
        RoleRepository roleRepo = new RoleRepository();
        
        // Test create role
        Role testRole = new Role();
        testRole.setRoleId(UUID.randomUUID().toString());
        testRole.setName("Test Role");
        testRole.setDescription("A test role for testing purposes");
        
        boolean created = roleRepo.createRole(testRole);
        System.out.println("Role created: " + created);
        
        if (created) {
            // Test get role by name
            Role foundRole = roleRepo.getRoleByName("Test Role");
            System.out.println("Found role: " + foundRole);
            
            // Test get all roles
            System.out.println("Total roles: " + roleRepo.getAllRoles().size());
            
            // Test delete role
            boolean deleted = roleRepo.deleteRole(testRole.getRoleId());
            System.out.println("Role deleted: " + deleted);
        }
    }
    

}
