package main.java.repository;

import main.java.config.DatabaseConnection;
import main.java.model.TeamMember;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TeamMemberRepository {

    private static final String INSERT_TEAM_MEMBER =
            "INSERT INTO team_members(team_id,user_id,role_id) VALUES (?,?,?)";
    private static final String SELECT_ALL_TEAMS_MEMBER =
            "SELECT team_id, user_id, role_id FROM team_members WHERE team_id = ?";
    private static final String SELECT_TEAM_MEMBER_BY_USER_ID =
            "SELECT team_id, user_id, role_id FROM team_members WHERE team_id = ? AND user_id = ?";
    private static final String UPDATE_TEAM_MEMBER_BY_ID =
            "UPDATE team_members SET user_id = ?, role_id = ? WHERE team_id = ? AND user_id = ?";
    private static final String DELETE_TEAM_MEMBER_BY_ID =
            "DELETE FROM team_members WHERE team_id = ? AND user_id = ?";

    /**
     * Thêm một team member mới
     * @param teamMember đối tượng TeamMember cần thêm
     * @return true nếu thêm thành công, false nếu thất bại
     */
    public boolean addTeamMember(TeamMember teamMember) {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(INSERT_TEAM_MEMBER)) {

            stmt.setString(1, teamMember.getTeam_id());
            stmt.setString(2, teamMember.getUser_id());
            stmt.setString(3, teamMember.getRole_id());

            int rows = stmt.executeUpdate();
            if (rows > 0) {
                System.out.println("Successfully added team member: " + teamMember.getUser_id());
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi thêm member: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Lấy danh sách tất cả member của một team
     * @param team_id mã team
     * @return List<TeamMember> danh sách member, có thể rỗng nếu không có
     */
    public List<TeamMember> findAll(String team_id) {
        List<TeamMember> members = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_ALL_TEAMS_MEMBER)) {

            stmt.setString(1, team_id);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    members.add(mapResultSetToTeamMember(rs));
                }
            }

        } catch (SQLException e) {
            System.err.println("Lỗi khi lấy danh sách member: " + e.getMessage());
            e.printStackTrace();
        }
        return members;
    }

    /**
     * Lấy một team member theo team_id và user_id
     * @param team_id mã team
     * @param user_id mã user
     * @return TeamMember nếu tìm thấy, null nếu không tìm thấy
     */
    public TeamMember findByUserId(String team_id, String user_id) {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_TEAM_MEMBER_BY_USER_ID)) {

            stmt.setString(1, team_id);
            stmt.setString(2, user_id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToTeamMember(rs);
                }
            }

        } catch (SQLException e) {
            System.err.println("Lỗi khi tìm team member: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Cập nhật thông tin user_id và role_id của một team member
     * @param team_id mã team
     * @param old_user_id user_id cũ cần update
     * @param new_user_id user_id mới
     * @param role_id role_id mới
     * @return true nếu update thành công, false nếu thất bại
     */
    public boolean updateTeamMember(String team_id, String old_user_id, String new_user_id, String role_id) {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(UPDATE_TEAM_MEMBER_BY_ID)) {

            stmt.setString(1, new_user_id);
            stmt.setString(2, role_id);
            stmt.setString(3, team_id);
            stmt.setString(4, old_user_id);

            int rows = stmt.executeUpdate();
            if (rows > 0) {
                System.out.println("Team member updated: " + old_user_id);
                return true;
            }

        } catch (SQLException e) {
            System.err.println("Lỗi khi update member: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Xóa một team member theo team_id và user_id
     * @param team_id mã team
     * @param user_id mã user
     * @return true nếu xóa thành công, false nếu thất bại
     */
    public boolean deleteTeamMember(String team_id, String user_id) {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(DELETE_TEAM_MEMBER_BY_ID)) {

            stmt.setString(1, team_id);
            stmt.setString(2, user_id);

            int rows = stmt.executeUpdate();
            if (rows > 0) {
                System.out.println("Team member deleted: " + user_id);
                return true;
            }

        } catch (SQLException e) {
            System.err.println("Lỗi khi xóa member: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Map dữ liệu ResultSet sang đối tượng TeamMember
     * @param rs ResultSet chứa dữ liệu team member
     * @return TeamMember
     * @throws SQLException nếu lỗi đọc dữ liệu
     */
    private TeamMember mapResultSetToTeamMember(ResultSet rs) throws SQLException {
        TeamMember member = new TeamMember();
        member.setTeam_id(rs.getString("team_id"));
        member.setUser_id(rs.getString("user_id"));
        member.setRole_id(rs.getString("role_id"));
        return member;
    }
}
