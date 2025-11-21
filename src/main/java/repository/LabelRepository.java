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
            e.printStackTrace();
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
            e.printStackTrace();
        }
        return null;
    }

    public List<Label> getAllLabels() {
        List<Label> labels = new ArrayList<>();
        String sql = "SELECT * FROM labels ORDER BY name";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                labels.add(mapResultSetToLabel(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return labels;
    }

    public List<Label> getLabelsByTeamId(String teamId) {
        List<Label> labels = new ArrayList<>();
        String sql = "SELECT * FROM labels WHERE team_id = ? ORDER BY name";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, teamId);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                labels.add(mapResultSetToLabel(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return labels;
    }

    public List<Label> getLabelsByTaskId(String taskId) {
        List<Label> labels = new ArrayList<>();
        String sql = """
            SELECT l.* FROM labels l
            INNER JOIN task_labels tl ON l.label_id = tl.label_id
            WHERE tl.task_id = ?
            ORDER BY l.name
            """;
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, taskId);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                labels.add(mapResultSetToLabel(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
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
            e.printStackTrace();
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
            e.printStackTrace();
            return false;
        }
    }

    private Label mapResultSetToLabel(ResultSet rs) throws SQLException {
        Label label = new Label();
        label.setLabelId(rs.getString("label_id"));
        label.setTeamId(rs.getString("team_id"));
        label.setName(rs.getString("name"));
        label.setColor(rs.getString("color"));
        return label;
    }
}