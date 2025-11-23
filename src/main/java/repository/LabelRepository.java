package main.java.repository;

import main.java.config.DatabaseConnection;
import main.java.model.Label;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class LabelRepository {

    public boolean createLabel(Label label) {
        String sql = "INSERT INTO labels (label_id, team_id, name, color) VALUES (?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            if (label.getLabelId() == null || label.getLabelId().isEmpty()) {
                label.setLabelId(UUID.randomUUID().toString());
            }
            
            pstmt.setString(1, label.getLabelId());
            pstmt.setString(2, label.getTeamId());
            pstmt.setString(3, label.getName());
            pstmt.setString(4, label.getColor());
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            return false;
        }
    }

    public Label getLabelById(String labelId) {
        String sql = "SELECT * FROM labels WHERE label_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, labelId);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToLabel(rs);
            }
        } catch (SQLException e) {
        }
        return null;
    }

    public List<Label> getAllLabels() {
        String sql = "SELECT * FROM labels";
        List<Label> labels = new ArrayList<>();
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                labels.add(mapResultSetToLabel(rs));
            }
        } catch (SQLException e) {
        }
        return labels;
    }

    public List<Label> getLabelsByTeamId(String teamId) {
        String sql = "SELECT * FROM labels WHERE team_id = ?";
        List<Label> labels = new ArrayList<>();
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, teamId);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                labels.add(mapResultSetToLabel(rs));
            }
        } catch (SQLException e) {
        }
        return labels;
    }

    public boolean updateLabel(Label label) {
        String sql = "UPDATE labels SET team_id = ?, name = ?, color = ? WHERE label_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, label.getTeamId());
            pstmt.setString(2, label.getName());
            pstmt.setString(3, label.getColor());
            pstmt.setString(4, label.getLabelId());
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            return false;
        }
    }

    public boolean deleteLabel(String labelId) {
        String sql = "DELETE FROM labels WHERE label_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, labelId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            return false;
        }
    }

    private Label mapResultSetToLabel(ResultSet rs) throws SQLException {
        return new Label(
            rs.getString("label_id"),
            rs.getString("team_id"),
            rs.getString("name"),
            rs.getString("color")
        );
    }
}
