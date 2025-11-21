package main.java.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import main.java.config.DatabaseConnection;
import main.java.model.ProjectMember;

public class ProjectMemberRepository {
    private static final String INSERT_PROJECT_MEMBER =
            "INSERT INTO project_members(project_id, user_id, role_id) VALUES (?, ?, ?)";
    private static final String SELECT_ALL_PROJECT_MEMBERS =
            "SELECT project_id, user_id, role_id FROM project_members";
    private static final String SELECT_PROJECT_MEMBER_BY_ID =
            "SELECT project_id, user_id, role_id FROM project_members WHERE project_id = ? AND user_id = ?";
    private static final String SELECT_MEMBERS_BY_PROJECT_ID =
            "SELECT project_id, user_id, role_id FROM project_members WHERE project_id = ?";
    private static final String SELECT_MEMBERS_BY_USER_ID =
            "SELECT project_id, user_id, role_id FROM project_members WHERE user_id = ?";
    private static final String SELECT_MEMBERS_BY_ROLE_ID =
            "SELECT project_id, user_id, role_id FROM project_members WHERE role_id = ?";
    private static final String UPDATE_PROJECT_MEMBER_ROLE =
            "UPDATE project_members SET role_id = ? WHERE project_id = ? AND user_id = ?";
    private static final String DELETE_PROJECT_MEMBER =
            "DELETE FROM project_members WHERE project_id = ? AND user_id = ?";
    private static final String DELETE_BY_PROJECT_ID =
            "DELETE FROM project_members WHERE project_id = ?";
    private static final String DELETE_BY_USER_ID =
            "DELETE FROM project_members WHERE user_id = ?";

    /**
     * Thêm một project member mới vào database
     * @param projectMember đối tượng ProjectMember cần tạo
     * @return true nếu tạo thành công, false nếu thất bại
     */
    public boolean createProjectMember(ProjectMember projectMember) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(INSERT_PROJECT_MEMBER);

            stmt.setString(1, projectMember.getProjectId());
            stmt.setString(2, projectMember.getUserId());
            stmt.setString(3, projectMember.getRoleId());

            int rows = stmt.executeUpdate();
            if (rows > 0) {
                System.out.println("Project member created: Project " + projectMember.getProjectId() + " - User " + projectMember.getUserId());
                return true;
            }

        } catch (SQLException e) {
            System.err.println("Lỗi khi tạo project member: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Lấy danh sách tất cả các project members
     * @return List<ProjectMember> danh sách project members, có thể rỗng nếu không có member nào
     */
    public List<ProjectMember> findAll() {
        List<ProjectMember> members = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(SELECT_ALL_PROJECT_MEMBERS);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                members.add(mapResultSetToProjectMember(rs));
            }
            System.out.println("Found " + members.size() + " project members");
        } catch (SQLException e) {
            System.err.println("Lỗi khi lấy danh sách project members: " + e.getMessage());
            e.printStackTrace();
        }

        return members;
    }

    /**
     * Lấy project member theo project_id và user_id
     * @param project_id mã của project
     * @param user_id mã của user
     * @return ProjectMember nếu tìm thấy, null nếu không tìm thấy
     */
    public ProjectMember findByProjectIdAndUserId(String project_id, String user_id) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(SELECT_PROJECT_MEMBER_BY_ID);
            stmt.setString(1, project_id);
            stmt.setString(2, user_id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                System.out.println("Project member found: Project " + project_id + " - User " + user_id);
                return mapResultSetToProjectMember(rs);
            } else {
                System.out.println("Project member not found: Project " + project_id + " - User " + user_id);
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi tìm project member by project_id và user_id: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Lấy danh sách tất cả members của một project
     * @param project_id mã của project
     * @return List<ProjectMember> danh sách members của project, có thể rỗng
     */
    public List<ProjectMember> findByProjectId(String project_id) {
        List<ProjectMember> members = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(SELECT_MEMBERS_BY_PROJECT_ID);
            stmt.setString(1, project_id);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                members.add(mapResultSetToProjectMember(rs));
            }
            System.out.println("Found " + members.size() + " members in project " + project_id);
        } catch (SQLException e) {
            System.err.println("Lỗi khi lấy members by project_id: " + e.getMessage());
            e.printStackTrace();
        }

        return members;
    }

    /**
     * Lấy danh sách tất cả projects của một user
     * @param user_id mã của user
     * @return List<ProjectMember> danh sách projects của user, có thể rỗng
     */
    public List<ProjectMember> findByUserId(String user_id) {
        List<ProjectMember> members = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(SELECT_MEMBERS_BY_USER_ID);
            stmt.setString(1, user_id);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                members.add(mapResultSetToProjectMember(rs));
            }
            System.out.println("Found " + members.size() + " projects for user " + user_id);
        } catch (SQLException e) {
            System.err.println("Lỗi khi lấy members by user_id: " + e.getMessage());
            e.printStackTrace();
        }

        return members;
    }

    /**
     * Lấy danh sách tất cả members có một role cụ thể
     * @param role_id mã của role
     * @return List<ProjectMember> danh sách members có role đó, có thể rỗng
     */
    public List<ProjectMember> findByRoleId(String role_id) {
        List<ProjectMember> members = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(SELECT_MEMBERS_BY_ROLE_ID);
            stmt.setString(1, role_id);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                members.add(mapResultSetToProjectMember(rs));
            }
            System.out.println("Found " + members.size() + " members with role " + role_id);
        } catch (SQLException e) {
            System.err.println("Lỗi khi lấy members by role_id: " + e.getMessage());
            e.printStackTrace();
        }

        return members;
    }

    /**
     * Cập nhật role của project member
     * @param project_id mã project
     * @param user_id mã user
     * @param role_id role_id mới
     * @return true nếu cập nhật thành công, false nếu thất bại
     */
    public boolean updateProjectMemberRole(String project_id, String user_id, String role_id) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(UPDATE_PROJECT_MEMBER_ROLE);
            stmt.setString(1, role_id);
            stmt.setString(2, project_id);
            stmt.setString(3, user_id);

            int rows = stmt.executeUpdate();
            if (rows > 0) {
                System.out.println("Project member role updated: Project " + project_id + " - User " + user_id);
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi update project member role: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Xóa project member theo project_id và user_id
     * @param project_id mã của project
     * @param user_id mã của user
     * @return true nếu xóa thành công, false nếu thất bại
     */
    public boolean deleteByProjectIdAndUserId(String project_id, String user_id) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(DELETE_PROJECT_MEMBER);
            stmt.setString(1, project_id);
            stmt.setString(2, user_id);
            int rows = stmt.executeUpdate();
            if (rows > 0) {
                System.out.println("Project member deleted: Project " + project_id + " - User " + user_id);
                return true;
            } else {
                System.out.println("Project member not found: Project " + project_id + " - User " + user_id);
            }

        } catch (SQLException e) {
            System.err.println("Lỗi khi xóa project member: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Xóa tất cả members của một project
     * @param project_id mã của project
     * @return true nếu xóa thành công, false nếu thất bại
     */
    public boolean deleteByProjectId(String project_id) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(DELETE_BY_PROJECT_ID);
            stmt.setString(1, project_id);
            int rows = stmt.executeUpdate();
            if (rows > 0) {
                System.out.println("All members for project " + project_id + " deleted");
                return true;
            } else {
                System.out.println("No members found for project " + project_id);
            }

        } catch (SQLException e) {
            System.err.println("Lỗi khi xóa members by project_id: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Xóa tất cả project memberships của một user
     * @param user_id mã của user
     * @return true nếu xóa thành công, false nếu thất bại
     */
    public boolean deleteByUserId(String user_id) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(DELETE_BY_USER_ID);
            stmt.setString(1, user_id);
            int rows = stmt.executeUpdate();
            if (rows > 0) {
                System.out.println("All project memberships for user " + user_id + " deleted");
                return true;
            } else {
                System.out.println("No project memberships found for user " + user_id);
            }

        } catch (SQLException e) {
            System.err.println("Lỗi khi xóa members by user_id: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Map dữ liệu ResultSet sang đối tượng ProjectMember
     * @param rs ResultSet chứa dữ liệu project member
     * @return đối tượng ProjectMember
     * @throws SQLException nếu lỗi đọc dữ liệu
     */
    public ProjectMember mapResultSetToProjectMember(ResultSet rs) throws SQLException {
        ProjectMember projectMember = new ProjectMember();
        projectMember.setProjectId(rs.getString("project_id"));
        projectMember.setUserId(rs.getString("user_id"));
        projectMember.setRoleId(rs.getString("role_id"));
        return projectMember;
    }

}
