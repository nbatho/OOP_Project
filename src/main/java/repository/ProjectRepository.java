package main.java.repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import main.java.config.DatabaseConnection;
import main.java.model.Project;

public class ProjectRepository {

    public boolean createProject(Project project) {
        String sql = "INSERT INTO projects (project_id, team_id, name, description) VALUES (?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            if (project.getProjectId() == null || project.getProjectId().isEmpty()) {
                project.setProjectId(UUID.randomUUID().toString());
            }
            
            pstmt.setString(1, project.getProjectId());
            pstmt.setString(2, project.getTeamId());
            pstmt.setString(3, project.getName());
            pstmt.setString(4, project.getDescription());
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            return false;
        }
    }

    public Project getProjectById(String projectId) {
        String sql = "SELECT * FROM projects WHERE project_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, projectId);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToProject(rs);
            }
        } catch (SQLException e) {
        }
        return null;
    }

    public Project getProjectByName(String name) {
        String sql = "SELECT * FROM projects WHERE name = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, name);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToProject(rs);
            }
        } catch (SQLException e) {
        }
        return null;
    }

    public List<Project> getAllProjects() {
        List<Project> projects = new ArrayList<>();
        String sql = "SELECT * FROM projects ORDER BY created_at DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                projects.add(mapResultSetToProject(rs));
            }
        } catch (SQLException e) {
        }
        return projects;
    }

    public List<Project> getProjectsByTeamId(String teamId) {
        List<Project> projects = new ArrayList<>();
        String sql = "SELECT * FROM projects WHERE team_id = ? ORDER BY created_at DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, teamId);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                projects.add(mapResultSetToProject(rs));
            }
        } catch (SQLException e) {
        }
        return projects;
    }

    public List<Project> getProjectsByUserId(String userId) {
        List<Project> projects = new ArrayList<>();
        String sql = """
            SELECT p.* FROM projects p
            INNER JOIN project_members pm ON p.project_id = pm.project_id
            WHERE pm.user_id = ?
            ORDER BY p.created_at DESC
            """;
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, userId);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                projects.add(mapResultSetToProject(rs));
            }
        } catch (SQLException e) {
        }
        return projects;
    }

    public boolean updateProject(Project project) {
        String sql = "UPDATE projects SET team_id = ?, name = ?, description = ? WHERE project_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, project.getTeamId());
            pstmt.setString(2, project.getName());
            pstmt.setString(3, project.getDescription());
            pstmt.setString(4, project.getProjectId());
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            return false;
        }
    }

    public boolean deleteProject(String projectId) {
        String sql = "DELETE FROM projects WHERE project_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, projectId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            return false;
        }
    }

    private Project mapResultSetToProject(ResultSet rs) throws SQLException {
        Project project = new Project();
        project.setProjectId(rs.getString("project_id"));
        project.setTeamId(rs.getString("team_id"));
        project.setName(rs.getString("name"));
        project.setDescription(rs.getString("description"));
        project.setCreatedAt(rs.getTimestamp("created_at"));
        return project;
    }
}
