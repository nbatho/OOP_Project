package main.java.config;

import main.java.model.*;
import main.java.repository.*;
import java.util.UUID;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class DataInitializer {
    
    public static void initializeData() {
        System.out.println("Initializing sample data...");
        
        // Initialize repositories
        UserRepository userRepo = new UserRepository();
        RoleRepository roleRepo = new RoleRepository();
        TeamRepository teamRepo = new TeamRepository();
        ProjectRepository projectRepo = new ProjectRepository();
        PermissionRepository permissionRepo = new PermissionRepository();
        
        try {
            // Create basic roles
            createBasicRoles(roleRepo);
            
            // Create basic permissions
            createBasicPermissions(permissionRepo);
            
            // Create sample users
            createSampleUsers(userRepo);
            
            // Create sample teams
            createSampleTeams(teamRepo);
            
            // Create sample projects
            createSampleProjects(projectRepo);
            
            System.out.println("Sample data initialized successfully!");
            
        } catch (Exception e) {
            System.err.println("Error initializing data: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private static void createBasicRoles(RoleRepository roleRepo) {
        String[] roleNames = {"Admin", "Manager", "Member", "Viewer"};
        String[] descriptions = {
            "Full system access and administration rights",
            "Team and project management capabilities", 
            "Standard team member with project access",
            "Read-only access to assigned projects"
        };
        
        for (int i = 0; i < roleNames.length; i++) {
            Role role = new Role();
            role.setRoleId(UUID.randomUUID().toString());
            role.setName(roleNames[i]);
            role.setDescription(descriptions[i]);
            
            roleRepo.createRole(role);
            System.out.println("Created role: " + roleNames[i]);
        }
    }
    
    private static void createBasicPermissions(PermissionRepository permissionRepo) {
        String[] permNames = {"CREATE_PROJECT", "DELETE_PROJECT", "CREATE_TASK", "ASSIGN_TASK", "VIEW_ALL_PROJECTS"};
        String[] descriptions = {
            "Can create new projects",
            "Can delete projects",
            "Can create new tasks",
            "Can assign tasks to users",
            "Can view all projects in system"
        };
        
        for (int i = 0; i < permNames.length; i++) {
            Permission permission = new Permission();
            permission.setPermissionId(UUID.randomUUID().toString());
            permission.setName(permNames[i]);
            permission.setDescription(descriptions[i]);
            
            permissionRepo.createPermission(permission);
            System.out.println("Created permission: " + permNames[i]);
        }
    }
    
    private static void createSampleUsers(UserRepository userRepo) {
        String[][] users = {
            {"Admin User", "admin@company.com", "admin123"},
            {"John Manager", "john@company.com", "john123"},
            {"Alice Developer", "alice@company.com", "alice123"},
            {"Bob Tester", "bob@company.com", "bob123"}
        };
        
        for (String[] userData : users) {
            User user = new User();
            user.setUserId(UUID.randomUUID().toString());
            user.setFullName(userData[0]);
            user.setEmail(userData[1]);
            user.setPasswordHash(hashPassword(userData[2]));
            
            userRepo.createUser(user);
            System.out.println("Created user: " + userData[0]);
        }
    }
    
    private static void createSampleTeams(TeamRepository teamRepo) {
        String[] teamNames = {"Development Team", "QA Team", "Design Team", "Management Team"};
        
        for (String teamName : teamNames) {
            Team team = new Team();
            team.setTeamId(UUID.randomUUID().toString());
            team.setName(teamName);
            
            teamRepo.createTeam(team);
            System.out.println("Created team: " + teamName);
        }
    }
    
    private static void createSampleProjects(ProjectRepository projectRepo) {
        // First get a team to assign projects to
        TeamRepository teamRepo = new TeamRepository();
        var teams = teamRepo.getAllTeams();
        
        if (!teams.isEmpty()) {
            String teamId = teams.get(0).getTeamId();
            
            String[][] projects = {
                {"Task Management System", "A comprehensive task management application"},
                {"Customer Portal", "Web portal for customer self-service"},
                {"Mobile App", "Mobile application for task tracking"},
                {"API Gateway", "Centralized API management system"}
            };
            
            for (String[] projectData : projects) {
                Project project = new Project();
                project.setProjectId(UUID.randomUUID().toString());
                project.setTeamId(teamId);
                project.setName(projectData[0]);
                project.setDescription(projectData[1]);
                
                projectRepo.createProject(project);
                System.out.println("Created project: " + projectData[0]);
            }
        }
    }
    
    private static String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashedPassword = md.digest(password.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : hashedPassword) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            return password; // Fallback to plain text (not recommended for production)
        }
    }
    
    public static void main(String[] args) {
        initializeData();
    }
}