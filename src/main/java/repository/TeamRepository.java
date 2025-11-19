package main.java.repository;

import main.java.config.DatabaseConnection;
import main.java.model.Team;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TeamRepository {
    private static final String INSERT_TEAM =
            "INSERT INTO teams(team_id,name) VALUES (?,?)";
    private static final String SELECT_ALL_TEAMS =
            "SELECT team_id, name, created_at FROM teams";
    private static final String SELECT_TEAM_BY_ID =
            "SELECT team_id, name, created_at FROM teams WHERE team_id = ?";
    private static final String SELECT_TEAM_BY_NAME =
            "SELECT team_id, name, created_at FROM teams WHERE name = ?";
    private static final String UPDATE_TEAM_NAME =
            "UPDATE teams SET name = ? WHERE team_id = ?";
    private static final String DELETE_TEAM =
            "DELETE FROM teams WHERE team_id = ?";

    /**
     * Thêm một team mới vào database
     * @param team đối tượng Team cần tạo
     * @return true nếu tạo thành công, false nếu thất bại
     */
    public boolean createTeam(Team team) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(INSERT_TEAM);

            stmt.setString(1, team.getTeam_id());
            stmt.setString(2, team.getName());

            int rows = stmt.executeUpdate();
            if (rows > 0) {
                System.out.println("Team created: " + team.getTeam_id());
                return true;
            }

        } catch (SQLException e) {
            System.err.println("Lỗi khi tạo team: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Lấy danh sách tất cả các team
     * @return List<Team> danh sách team, có thể rỗng nếu không có team nào
     */
    public List<Team> findAll(String uuid) {
        List<Team> teams = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(SELECT_ALL_TEAMS);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                teams.add(mapResultSetToTeam(rs));
            }
            System.out.println("Found " + teams.size() + " teams");
        } catch (SQLException e) {
            System.err.println("Lỗi khi lấy danh sách teams: " + e.getMessage());
            e.printStackTrace();
        }

        return teams;
    }

    /**
     * Lấy team theo team_id
     * @param team_id mã của team cần tìm
     * @return Team nếu tìm thấy, null nếu không tìm thấy
     */
    public Team findByTeamId(String team_id) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(SELECT_TEAM_BY_ID);
            stmt.setString(1, team_id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                System.out.println("Team found: " + team_id);
                return mapResultSetToTeam(rs);
            } else {
                System.out.println("Team not found: " + team_id);
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi tìm team by id: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Lấy team theo tên
     * @param name tên team cần tìm
     * @return Team nếu tìm thấy, null nếu không tìm thấy
     */
    public Team findByTeamName(String name) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(SELECT_TEAM_BY_NAME);
            stmt.setString(1, name);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                System.out.println("Team found: " + name);
                return mapResultSetToTeam(rs);
            } else {
                System.out.println("Team not found: " + name);
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi tìm team by name: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Cập nhật tên team theo team_id
     * @param team_id mã team cần cập nhật
     * @param name tên mới
     * @return true nếu cập nhật thành công, false nếu thất bại
     */
    public boolean updateTeamName(String team_id, String name) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(UPDATE_TEAM_NAME);
            stmt.setString(1, name);
            stmt.setString(2, team_id);

            int rows = stmt.executeUpdate();
            if (rows > 0) {
                System.out.println("Team updated: " + team_id);
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi update team: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Xóa team theo team_id
     * @param team_id mã team cần xóa
     * @return true nếu xóa thành công, false nếu thất bại
     */
    public boolean deleteByTeamId(String team_id) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(DELETE_TEAM);
            stmt.setString(1, team_id);
            int rows = stmt.executeUpdate();
            if (rows > 0) {
                System.out.println("Team deleted: " + team_id);
                return true;
            } else {
                System.out.println("Team not found: " + team_id);
            }

        } catch (SQLException e) {
            System.err.println("Lỗi khi xóa team: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Map dữ liệu ResultSet sang đối tượng Team
     * @param rs ResultSet chứa dữ liệu team
     * @return đối tượng Team
     * @throws SQLException nếu lỗi đọc dữ liệu
     */
    public Team mapResultSetToTeam(ResultSet rs) throws SQLException {
        Team team = new Team();
        team.setTeam_id(rs.getString("team_id"));
        team.setName(rs.getString("name"));
        return team;
    }

}
