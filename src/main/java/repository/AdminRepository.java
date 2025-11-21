package main.java.repository;

import main.java.config.DatabaseConnection;
import main.java.model.Admin;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AdminRepository {
    
    public boolean createAdmin(Admin admin) {
        String sql = "INSERT INTO admins (user_id) VALUES (?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, admin.getUserId());
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Lỗi khi tạo admin: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    public List<Admin> getAllAdmins() {
        String sql = "SELECT user_id FROM admins";
        List<Admin> admins = new ArrayList<>();
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                Admin admin = mapResultSetToAdmin(rs);
                if (admin != null) {
                    admins.add(admin);
                }
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi lấy danh sách admins: " + e.getMessage());
            e.printStackTrace();
        }
        
        return admins;
    }
    
    public Admin getAdminById(String userId) {
        String sql = "SELECT user_id FROM admins WHERE user_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, userId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToAdmin(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi lấy admin theo ID: " + e.getMessage());
            e.printStackTrace();
        }
        
        return null;
    }
    
    public boolean updateAdmin(Admin admin) {
        // Admin table chỉ có user_id, không có gì để update
        return true;
    }
    
    public boolean deleteAdmin(String userId) {
        String sql = "DELETE FROM admins WHERE user_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, userId);
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Lỗi khi xóa admin: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    private Admin mapResultSetToAdmin(ResultSet rs) {
        try {
            Admin admin = new Admin();
            admin.setUserId(rs.getString("user_id"));
            return admin;
        } catch (SQLException e) {
            System.err.println("Lỗi khi mapping ResultSet to Admin: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
}