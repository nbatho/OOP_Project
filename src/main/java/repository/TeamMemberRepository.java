package main.java.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import main.java.config.DatabaseConnection;
import main.java.model.TeamMember;

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

            stmt.setString(1, teamMember.getTeamId());
            stmt.setString(2, teamMember.getUserId());
            stmt.setString(3, teamMember.getRoleId());

            int rows = stmt.executeUpdate();
            if (rows > 0) {
                System.out.println("Successfully added team member: " + teamMember.getUserId());
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi thêm member: " + e.getMessage());
        }
        return false;
    }

    /**
     * Lấy danh sách tất cả member của một team
     * @param teamId mã team
     * @return List<TeamMember> danh sách member, có thể rỗng nếu không có
     */
    public List<TeamMember> findAll(String teamId) {
        List<TeamMember> members = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_ALL_TEAMS_MEMBER)) {

            stmt.setString(1, teamId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    members.add(mapResultSetToTeamMember(rs));
                }
            }

        } catch (SQLException e) {
            System.err.println("Lỗi khi lấy danh sách member: " + e.getMessage());
        }
        return members;
    }

    /**
     * Lấy một team member theo teamId và userId
     * @param teamId mã team
     * @param userId mã user
     * @return TeamMember nếu tìm thấy, null nếu không tìm thấy
     */
    public TeamMember findByUserId(String teamId, String userId) {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_TEAM_MEMBER_BY_USER_ID)) {

            stmt.setString(1, teamId);
            stmt.setString(2, userId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToTeamMember(rs);
                }
            }

        } catch (SQLException e) {
            System.err.println("Lỗi khi tìm team member: " + e.getMessage());
        }
        return null;
    }

    /**
     * Cập nhật thông tin userId và roleId của một team member
     * @param teamId mã team
     * @param oldUserId userId cũ cần update
     * @param newUserId userId mới
     * @param roleId roleId mới
     * @return true nếu update thành công, false nếu thất bại
     */
    public boolean updateTeamMember(String teamId, String oldUserId, String newUserId, String roleId) {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(UPDATE_TEAM_MEMBER_BY_ID)) {

            stmt.setString(1, newUserId);
            stmt.setString(2, roleId);
            stmt.setString(3, teamId);
            stmt.setString(4, oldUserId);

            int rows = stmt.executeUpdate();
            if (rows > 0) {
                System.out.println("Team member updated: " + oldUserId);
                return true;
            }

        } catch (SQLException e) {
            System.err.println("Lỗi khi update member: " + e.getMessage());
        }
        return false;
    }

    /**
     * Xóa một team member theo teamId và userId
     * @param teamId mã team
     * @param userId mã user
     * @return true nếu xóa thành công, false nếu thất bại
     */
    public boolean deleteTeamMember(String teamId, String userId) {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(DELETE_TEAM_MEMBER_BY_ID)) {

            stmt.setString(1, teamId);
            stmt.setString(2, userId);

            int rows = stmt.executeUpdate();
            if (rows > 0) {
                System.out.println("Team member deleted: " + userId);
                return true;
            }

        } catch (SQLException e) {
            System.err.println("Lỗi khi xóa member: " + e.getMessage());
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
        member.setTeamId(rs.getString("team_id"));
        member.setUserId(rs.getString("user_id"));
        member.setRoleId(rs.getString("role_id"));
        return member;
    }
}
