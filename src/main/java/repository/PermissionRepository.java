package main.java.repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import main.java.config.DatabaseConnection;
import main.java.model.Permission;

public class PermissionRepository {

    public boolean createPermission(Permission permission) {
        String sql = "INSERT INTO permissions (permission_id, name, description) VALUES (?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            if (permission.getPermissionId() == null || permission.getPermissionId().isEmpty()) {
                permission.setPermissionId(UUID.randomUUID().toString());
            }
            
            pstmt.setString(1, permission.getPermissionId());
            pstmt.setString(2, permission.getName());
            pstmt.setString(3, permission.getDescription());
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            return false;
        }
    }

    public Permission getPermissionById(String permissionId) {
        String sql = "SELECT * FROM permissions WHERE permission_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, permissionId);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToPermission(rs);
            }
        } catch (SQLException e) {
        }
        return null;
    }

    public Permission getPermissionByName(String name) {
        String sql = "SELECT * FROM permissions WHERE name = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, name);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToPermission(rs);
            }
        } catch (SQLException e) {
        }
        return null;
    }

    public List<Permission> getAllPermissions() {
        List<Permission> permissions = new ArrayList<>();
        String sql = "SELECT * FROM permissions ORDER BY name";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                permissions.add(mapResultSetToPermission(rs));
            }
        } catch (SQLException e) {
        }
        return permissions;
    }

    public boolean updatePermission(Permission permission) {
        String sql = "UPDATE permissions SET name = ?, description = ? WHERE permission_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, permission.getName());
            pstmt.setString(2, permission.getDescription());
            pstmt.setString(3, permission.getPermissionId());
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            return false;
        }
    }

    public boolean deletePermission(String permissionId) {
        String sql = "DELETE FROM permissions WHERE permission_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, permissionId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            return false;
        }
    }

    public List<Permission> getPermissionsByRoleId(String roleId) {
        List<Permission> permissions = new ArrayList<>();
        String sql = """
            SELECT p.* FROM permissions p
            INNER JOIN role_permissions rp ON p.permission_id = rp.permission_id
            WHERE rp.role_id = ?
            """;
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, roleId);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                permissions.add(mapResultSetToPermission(rs));
            }
        } catch (SQLException e) {
        }
        return permissions;
    }

    private Permission mapResultSetToPermission(ResultSet rs) throws SQLException {
        Permission permission = new Permission();
        permission.setPermissionId(rs.getString("permission_id"));
        permission.setName(rs.getString("name"));
        permission.setDescription(rs.getString("description"));
        return permission;
    }
}
