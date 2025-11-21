package main.java.repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import main.java.config.DatabaseConnection;
import main.java.model.User;

public class UserRepository {

    public boolean createUser(User user) {
        String sql = "INSERT INTO users (user_id, full_name, email, password_hash) VALUES (?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            if (user.getUserId() == null || user.getUserId().isEmpty()) {
                user.setUserId(UUID.randomUUID().toString());
            }
            
            pstmt.setString(1, user.getUserId());
            pstmt.setString(2, user.getFullName());
            pstmt.setString(3, user.getEmail());
            pstmt.setString(4, user.getPasswordHash());
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public User getUserById(String userId) {
        String sql = "SELECT * FROM users WHERE user_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, userId);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToUser(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public User getUserByEmail(String email) {
        String sql = "SELECT * FROM users WHERE email = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, email);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToUser(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM users ORDER BY created_at DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                users.add(mapResultSetToUser(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    public boolean updateUser(User user) {
        String sql = "UPDATE users SET full_name = ?, email = ?, password_hash = ? WHERE user_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, user.getFullName());
            pstmt.setString(2, user.getEmail());
            pstmt.setString(3, user.getPasswordHash());
            pstmt.setString(4, user.getUserId());
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteUser(String userId) {
        String sql = "DELETE FROM users WHERE user_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, userId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean emailExists(String email) {
        String sql = "SELECT COUNT(*) FROM users WHERE email = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, email);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<User> getUsersByTeamId(String teamId) {
        List<User> users = new ArrayList<>();
        String sql = """
            SELECT u.* FROM users u
            INNER JOIN team_members tm ON u.user_id = tm.user_id
            WHERE tm.team_id = ?
            """;
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, teamId);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                users.add(mapResultSetToUser(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    private User mapResultSetToUser(ResultSet rs) throws SQLException {
        User user = new User();
        user.setUserId(rs.getString("user_id"));
        user.setFullName(rs.getString("full_name"));
        user.setEmail(rs.getString("email"));
        user.setPasswordHash(rs.getString("password_hash"));
        user.setCreatedAt(rs.getTimestamp("created_at"));
        return user;
    }
}
