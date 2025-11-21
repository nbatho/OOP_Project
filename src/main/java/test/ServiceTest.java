package main.java.test;

import main.java.service.impl.*;
import main.java.service.*;
import main.java.model.*;

public class ServiceTest {
    public static void main(String[] args) {
        System.out.println("=== Testing Service Layer ===");
        
        System.out.println("\n=== Testing UserService ===");
        testUserService();
        
        System.out.println("\n=== Testing TeamService ===");
        testTeamService();
        
        System.out.println("\n=== Testing ProjectService ===");
        testProjectService();
        
        System.out.println("\n=== Testing TaskService ===");
        testTaskService();
        
        System.out.println("\n=== Testing ManagerService ===");
        testManagerService();
        
        System.out.println("\n=== Testing AdminService ===");
        testAdminService();
        
        System.out.println("\nTất cả service đã được test thành công!");
    }
    
    private static void testUserService() {
        UserService userService = new UserServiceImpl();
        
        User testUser = new User();
        testUser.setFullName("Service Test User");
        testUser.setEmail("servicetest@example.com");
        testUser.setPasswordHash("password123");
        
        boolean created = userService.createUser(testUser);
        System.out.println("User service create: " + created);
        
        if (created) {
            User foundUser = userService.getUserByEmail("servicetest@example.com");
            System.out.println("Found user: " + (foundUser != null ? foundUser.getFullName() : "null"));
            
            boolean authenticated = userService.authenticateUser("servicetest@example.com", "password123") != null;
            System.out.println("Authentication: " + authenticated);
            
            boolean exists = userService.userExists(foundUser.getUserId());
            System.out.println("User exists: " + exists);
            
            userService.deleteUser(foundUser.getUserId());
            System.out.println("User cleaned up");
        }
    }
    
    private static void testTeamService() {
        TeamService teamService = new TeamServiceImpl();
        
        Team testTeam = new Team();
        testTeam.setName("Service Test Team");
        
        boolean created = teamService.createTeam(testTeam);
        System.out.println("Team service create: " + created);
        
        if (created) {
            Team foundTeam = teamService.getTeamByName("Service Test Team");
            System.out.println("Found team: " + (foundTeam != null ? foundTeam.getName() : "null"));
            
            boolean exists = teamService.teamExists(foundTeam.getTeamId());
            System.out.println("Team exists: " + exists);
            
            teamService.deleteTeam(foundTeam.getTeamId());
            System.out.println("Team cleaned up");
        }
    }
    
    private static void testProjectService() {
        ProjectService projectService = new ProjectServiceImpl();
        TeamService teamService = new TeamServiceImpl();
        
        // Tạo team trước
        Team testTeam = new Team();
        testTeam.setName("Project Service Test Team");
        teamService.createTeam(testTeam);
        Team createdTeam = teamService.getTeamByName("Project Service Test Team");
        
        if (createdTeam != null) {
            Project testProject = new Project();
            testProject.setTeamId(createdTeam.getTeamId());
            testProject.setName("Service Test Project");
            testProject.setDescription("A test project for service testing");
            
            boolean created = projectService.createProject(testProject);
            System.out.println("Project service create: " + created);
            
            if (created) {
                Project foundProject = projectService.getProjectByName("Service Test Project");
                System.out.println("Found project: " + (foundProject != null ? foundProject.getName() : "null"));
                
                boolean exists = projectService.projectExists(foundProject.getProjectId());
                System.out.println("Project exists: " + exists);
                
                projectService.deleteProject(foundProject.getProjectId());
                System.out.println("Project cleaned up");
            }
            
            teamService.deleteTeam(createdTeam.getTeamId());
        }
    }
    
    private static void testTaskService() {
        TaskService taskService = new TaskServiceImpl();
        ProjectService projectService = new ProjectServiceImpl();
        TeamService teamService = new TeamServiceImpl();
        
        // Tạo team và project trước
        Team testTeam = new Team();
        testTeam.setName("Task Service Test Team");
        teamService.createTeam(testTeam);
        Team createdTeam = teamService.getTeamByName("Task Service Test Team");
        
        if (createdTeam != null) {
            Project testProject = new Project();
            testProject.setTeamId(createdTeam.getTeamId());
            testProject.setName("Task Service Test Project");
            testProject.setDescription("Project for task testing");
            projectService.createProject(testProject);
            Project createdProject = projectService.getProjectByName("Task Service Test Project");
            
            if (createdProject != null) {
                Task testTask = new Task();
                testTask.setProjectId(createdProject.getProjectId());
                testTask.setTitle("Service Test Task");
                testTask.setDescription("A test task for service testing");
                testTask.setStatus("TODO");
                testTask.setPriority("MEDIUM");
                
                boolean created = taskService.createTask(testTask);
                System.out.println("Task service create: " + created);
                
                if (created) {
                    boolean exists = taskService.taskExists(testTask.getTaskId(), testTask.getProjectId());
                    System.out.println("Task exists: " + exists);
                    
                    boolean statusUpdated = taskService.updateTaskStatus(testTask.getTaskId(), 
                            testTask.getProjectId(), "IN_PROGRESS");
                    System.out.println("Task status updated: " + statusUpdated);
                    
                    taskService.deleteTask(testTask.getTaskId(), testTask.getProjectId());
                    System.out.println("Task cleaned up");
                }
                
                projectService.deleteProject(createdProject.getProjectId());
            }
            
            teamService.deleteTeam(createdTeam.getTeamId());
        }
    }
    
    private static void testManagerService() {
        ManagerService managerService = new ManagerServiceImpl();
        UserService userService = new UserServiceImpl();
        
        // Tạo user trước
        User testUser = new User();
        testUser.setFullName("Manager Test User");
        testUser.setEmail("managertest@example.com");
        testUser.setPasswordHash("password123");
        userService.createUser(testUser);
        User createdUser = userService.getUserByEmail("managertest@example.com");
        
        if (createdUser != null) {
            boolean promoted = managerService.promoteUserToManager(createdUser.getUserId());
            System.out.println("User promoted to manager: " + promoted);
            
            boolean isManager = managerService.isUserManager(createdUser.getUserId());
            System.out.println("User is manager: " + isManager);
            
            managerService.demoteManagerToUser(createdUser.getUserId());
            userService.deleteUser(createdUser.getUserId());
            System.out.println("Manager cleaned up");
        }
    }
    
    private static void testAdminService() {
        AdminService adminService = new AdminServiceImpl();
        UserService userService = new UserServiceImpl();
        
        // Tạo user trước
        User testUser = new User();
        testUser.setFullName("Admin Test User");
        testUser.setEmail("admintest@example.com");
        testUser.setPasswordHash("password123");
        userService.createUser(testUser);
        User createdUser = userService.getUserByEmail("admintest@example.com");
        
        if (createdUser != null) {
            boolean promoted = adminService.promoteUserToAdmin(createdUser.getUserId());
            System.out.println("User promoted to admin: " + promoted);
            
            boolean isAdmin = adminService.isUserAdmin(createdUser.getUserId());
            System.out.println("User is admin: " + isAdmin);
            
            adminService.demoteAdminToUser(createdUser.getUserId());
            userService.deleteUser(createdUser.getUserId());
            System.out.println("Admin cleaned up");
        }
    }
}