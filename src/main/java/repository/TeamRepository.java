package main.java.repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import main.java.config.DatabaseConnection;
import main.java.model.Team;

public class TeamRepository {

    public boolean createTeam(Team team) {
        String sql = "INSERT INTO teams (team_id, name) VALUES (?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            if (team.getTeamId() == null || team.getTeamId().isEmpty()) {
                team.setTeamId(UUID.randomUUID().toString());
            }
            
            pstmt.setString(1, team.getTeamId());
            pstmt.setString(2, team.getName());
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            return false;
        }
    }

    public Team getTeamById(String teamId) {
        String sql = "SELECT * FROM teams WHERE team_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, teamId);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToTeam(rs);
            }
        } catch (SQLException e) {
        }
        return null;
    }

    public Team getTeamByName(String name) {
        String sql = "SELECT * FROM teams WHERE name = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, name);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToTeam(rs);
            }
        } catch (SQLException e) {
        }
        return null;
    }

    public List<Team> getAllTeams() {
        List<Team> teams = new ArrayList<>();
        String sql = "SELECT * FROM teams ORDER BY created_at DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                teams.add(mapResultSetToTeam(rs));
            }
        } catch (SQLException e) {
        }
        return teams;
    }

    public boolean updateTeam(Team team) {
        String sql = "UPDATE teams SET name = ? WHERE team_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, team.getName());
            pstmt.setString(2, team.getTeamId());
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            return false;
        }
    }

    public boolean deleteTeam(String teamId) {
        String sql = "DELETE FROM teams WHERE team_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, teamId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            return false;
        }
    }

    public List<Team> getTeamsByUserId(String userId) {
        List<Team> teams = new ArrayList<>();
        String sql = """
            SELECT t.* FROM teams t
            INNER JOIN team_members tm ON t.team_id = tm.team_id
            WHERE tm.user_id = ?
            """;
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, userId);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                teams.add(mapResultSetToTeam(rs));
            }
        } catch (SQLException e) {
        }
        return teams;
    }

    private Team mapResultSetToTeam(ResultSet rs) throws SQLException {
        Team team = new Team();
        team.setTeamId(rs.getString("team_id"));
        team.setName(rs.getString("name"));
        team.setCreatedAt(rs.getTimestamp("created_at"));
        return team;
    }
}
