package main.java.repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import main.java.config.DatabaseConnection;
import main.java.model.Role;

public class RoleRepository {

    public boolean createRole(Role role) {
        String sql = "INSERT INTO roles (role_id, name, description) VALUES (?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            if (role.getRoleId() == null || role.getRoleId().isEmpty()) {
                role.setRoleId(UUID.randomUUID().toString());
            }
            
            pstmt.setString(1, role.getRoleId());
            pstmt.setString(2, role.getName());
            pstmt.setString(3, role.getDescription());
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public Role getRoleById(String roleId) {
        String sql = "SELECT * FROM roles WHERE role_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, roleId);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToRole(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Role getRoleByName(String name) {
        String sql = "SELECT * FROM roles WHERE name = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, name);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToRole(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Role> getAllRoles() {
        List<Role> roles = new ArrayList<>();
        String sql = "SELECT * FROM roles ORDER BY name";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                roles.add(mapResultSetToRole(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return roles;
    }

    public boolean updateRole(Role role) {
        String sql = "UPDATE roles SET name = ?, description = ? WHERE role_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, role.getName());
            pstmt.setString(2, role.getDescription());
            pstmt.setString(3, role.getRoleId());
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteRole(String roleId) {
        String sql = "DELETE FROM roles WHERE role_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, roleId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private Role mapResultSetToRole(ResultSet rs) throws SQLException {
        Role role = new Role();
        role.setRoleId(rs.getString("role_id"));
        role.setName(rs.getString("name"));
        role.setDescription(rs.getString("description"));
        return role;
    }
}
