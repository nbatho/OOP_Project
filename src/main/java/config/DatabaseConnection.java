package main.java.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseConnection {
    
    private static final String URL = "jdbc:mysql://127.0.0.1:3306/task_manager_db?useSSL=false&allowPublicKeyRetrieval=true&useUnicode=true&characterEncoding=UTF-8&serverTimezone=Asia/Ho_Chi_Minh";
    private static final String USER = "root";
    private static final String PASSWORD = "andz123456";
    
    static {
        initializeDatabase();
    }
    
    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new SQLException("MySQL Driver not found!", e);
        }
        
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
    
    private static void initializeDatabase() {
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {
            
            // Create tables
            createTables(stmt);
            System.out.println("Database initialized successfully!");
            
        } catch (SQLException e) {
            System.err.println("Error initializing database: " + e.getMessage());
        }
    }
    
    private static void createTables(Statement stmt) throws SQLException {
        String[] createTableQueries = {
            """
            CREATE TABLE IF NOT EXISTS users (
                user_id CHAR(36) PRIMARY KEY,
                full_name VARCHAR(255) NOT NULL,
                email VARCHAR(255) NOT NULL UNIQUE,
                password_hash VARCHAR(255) NOT NULL,
                created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
            )
            """,
            
            """
            CREATE TABLE IF NOT EXISTS teams (
                team_id CHAR(36) PRIMARY KEY,
                name VARCHAR(255) NOT NULL,
                created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
            )
            """,
            
            """
            CREATE TABLE IF NOT EXISTS roles (
                role_id CHAR(36) PRIMARY KEY,
                name VARCHAR(100) NOT NULL,
                description TEXT
            )
            """,
            
            """
            CREATE TABLE IF NOT EXISTS permissions (
                permission_id CHAR(36) PRIMARY KEY,
                name VARCHAR(100) NOT NULL,
                description TEXT
            )
            """,
            
            """
            CREATE TABLE IF NOT EXISTS role_permissions (
                role_id CHAR(36) NOT NULL,
                permission_id CHAR(36) NOT NULL,
                PRIMARY KEY (role_id, permission_id),
                FOREIGN KEY (role_id) REFERENCES roles(role_id) ON DELETE CASCADE,
                FOREIGN KEY (permission_id) REFERENCES permissions(permission_id) ON DELETE CASCADE
            )
            """,
            
            """
            CREATE TABLE IF NOT EXISTS team_members (
                team_id CHAR(36) NOT NULL,
                user_id CHAR(36) NOT NULL,
                role_id CHAR(36) NOT NULL,
                PRIMARY KEY (team_id, user_id),
                FOREIGN KEY (team_id) REFERENCES teams(team_id) ON DELETE CASCADE,
                FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
                FOREIGN KEY (role_id) REFERENCES roles(role_id)
            )
            """,
            
            """
            CREATE TABLE IF NOT EXISTS projects (
                project_id CHAR(36) PRIMARY KEY,
                team_id CHAR(36) NOT NULL,
                name VARCHAR(255) NOT NULL,
                description TEXT,
                created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                FOREIGN KEY (team_id) REFERENCES teams(team_id) ON DELETE CASCADE
            )
            """,
            
            """
            CREATE TABLE IF NOT EXISTS project_members (
                project_id CHAR(36) NOT NULL,
                user_id CHAR(36) NOT NULL,
                role_id CHAR(36) NOT NULL,
                PRIMARY KEY (project_id, user_id),
                FOREIGN KEY (project_id) REFERENCES projects(project_id) ON DELETE CASCADE,
                FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
                FOREIGN KEY (role_id) REFERENCES roles(role_id)
            )
            """,
            
            """
            CREATE TABLE IF NOT EXISTS tasks (
                task_id CHAR(36) PRIMARY KEY,
                project_id CHAR(36) NOT NULL,
                title VARCHAR(255) NOT NULL,
                description TEXT,
                status VARCHAR(50) NOT NULL,
                priority VARCHAR(50) NOT NULL,
                due_date DATE,
                created_by CHAR(36) NOT NULL,
                created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                FOREIGN KEY (project_id) REFERENCES projects(project_id) ON DELETE CASCADE,
                FOREIGN KEY (created_by) REFERENCES users(user_id) ON DELETE SET NULL
            )
            """,
            
            """
            CREATE TABLE IF NOT EXISTS task_assignees (
                task_id CHAR(36) NOT NULL,
                user_id CHAR(36) NOT NULL,
                PRIMARY KEY (task_id, user_id),
                FOREIGN KEY (task_id) REFERENCES tasks(task_id) ON DELETE CASCADE,
                FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE
            )
            """,
            
            """
            CREATE TABLE IF NOT EXISTS comments (
                comment_id CHAR(36) PRIMARY KEY,
                task_id CHAR(36) NOT NULL,
                user_id CHAR(36) NOT NULL,
                body TEXT NOT NULL,
                created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                FOREIGN KEY (task_id) REFERENCES tasks(task_id) ON DELETE CASCADE,
                FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE
            )
            """,
            
            """
            CREATE TABLE IF NOT EXISTS labels (
                label_id CHAR(36) PRIMARY KEY,
                team_id CHAR(36) NOT NULL,
                name VARCHAR(255) NOT NULL,
                color CHAR(7) NOT NULL,
                FOREIGN KEY (team_id) REFERENCES teams(team_id) ON DELETE CASCADE
            )
            """,
            
            """
            CREATE TABLE IF NOT EXISTS task_labels (
                task_id CHAR(36) NOT NULL,
                label_id CHAR(36) NOT NULL,
                PRIMARY KEY (task_id, label_id),
                FOREIGN KEY (task_id) REFERENCES tasks(task_id) ON DELETE CASCADE,
                FOREIGN KEY (label_id) REFERENCES labels(label_id) ON DELETE CASCADE
            )
            """,
            
            """
            CREATE TABLE IF NOT EXISTS checklists (
                checklist_id CHAR(36) PRIMARY KEY,
                task_id CHAR(36) NOT NULL,
                title VARCHAR(255) NOT NULL,
                FOREIGN KEY (task_id) REFERENCES tasks(task_id) ON DELETE CASCADE
            )
            """,
            
            """
            CREATE TABLE IF NOT EXISTS checklist_items (
                item_id CHAR(36) PRIMARY KEY,
                checklist_id CHAR(36) NOT NULL,
                content VARCHAR(255) NOT NULL,
                is_done BOOLEAN NOT NULL DEFAULT 0,
                FOREIGN KEY (checklist_id) REFERENCES checklists(checklist_id) ON DELETE CASCADE
            )
            """
        };
        
        for (String query : createTableQueries) {
            stmt.execute(query);
        }
    }
    
    public static void testConnection() {
        try (Connection conn = getConnection()) {
            if (conn != null && !conn.isClosed()) {
                System.out.println("Kết nối MySQL thành công!");
            } else {
                System.out.println("Kết nối MySQL thất bại!");
            }
        } catch (SQLException e) {
            System.out.println("Lỗi kết nối MySQL: " + e.getMessage());
        }
    }
}
