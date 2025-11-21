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
        
        System.out.println("\n=== Testing Team Repository ===");
        testTeamRepository();
        
        System.out.println("\n=== Testing Role Repository ===");
        testRoleRepository();
        
        System.out.println("\n=== Testing Project Repository ===");
        testProjectRepository();
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
    
    private static void testTeamRepository() {
        TeamRepository teamRepo = new TeamRepository();
        
        // Test create team
        Team testTeam = new Team();
        testTeam.setTeamId(UUID.randomUUID().toString());
        testTeam.setName("Test Team");
        
        boolean created = teamRepo.createTeam(testTeam);
        System.out.println("Team created: " + created);
        
        if (created) {
            // Test get team by name
            Team foundTeam = teamRepo.getTeamByName("Test Team");
            System.out.println("Found team: " + foundTeam);
            
            // Test get all teams
            System.out.println("Total teams: " + teamRepo.getAllTeams().size());
            
            // Test delete team
            boolean deleted = teamRepo.deleteTeam(testTeam.getTeamId());
            System.out.println("Team deleted: " + deleted);
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
    
    private static void testProjectRepository() {
        ProjectRepository projectRepo = new ProjectRepository();
        
        // First create a team for the project
        TeamRepository teamRepo = new TeamRepository();
        Team testTeam = new Team();
        testTeam.setTeamId(UUID.randomUUID().toString());
        testTeam.setName("Project Test Team");
        teamRepo.createTeam(testTeam);
        
        // Test create project
        Project testProject = new Project();
        testProject.setProjectId(UUID.randomUUID().toString());
        testProject.setTeamId(testTeam.getTeamId());
        testProject.setName("Test Project");
        testProject.setDescription("A test project for testing purposes");
        
        boolean created = projectRepo.createProject(testProject);
        System.out.println("Project created: " + created);
        
        if (created) {
            // Test get project by name
            Project foundProject = projectRepo.getProjectByName("Test Project");
            System.out.println("Found project: " + foundProject);
            
            // Test get all projects
            System.out.println("Total projects: " + projectRepo.getAllProjects().size());
            
            // Test delete project
            boolean deleted = projectRepo.deleteProject(testProject.getProjectId());
            System.out.println("Project deleted: " + deleted);
        }
        
        // Clean up team
        teamRepo.deleteTeam(testTeam.getTeamId());
    }
}
