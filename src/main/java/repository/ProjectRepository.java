package main.java.repository;

import main.java.config.DatabaseConnection;
import main.java.model.Project;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProjectRepository {
    private static final String INSERT_PROJECT =
            "INSERT INTO projects(project_id, team_id, name, description) VALUES (?, ?, ?, ?)";
    private static final String SELECT_ALL_PROJECTS =
            "SELECT project_id, team_id, name, description FROM projects";
    private static final String SELECT_PROJECT_BY_ID =
            "SELECT project_id, team_id, name, description FROM projects WHERE project_id = ?";
    private static final String SELECT_PROJECTS_BY_TEAM_ID =
            "SELECT project_id, team_id, name, description FROM projects WHERE team_id = ?";
    private static final String SELECT_PROJECT_BY_NAME =
            "SELECT project_id, team_id, name, description FROM projects WHERE name = ?";
    private static final String UPDATE_PROJECT =
            "UPDATE projects SET team_id = ?, name = ?, description = ? WHERE project_id = ?";
    private static final String UPDATE_PROJECT_NAME =
            "UPDATE projects SET name = ? WHERE project_id = ?";
    private static final String UPDATE_PROJECT_DESCRIPTION =
            "UPDATE projects SET description = ? WHERE project_id = ?";
    private static final String DELETE_PROJECT =
            "DELETE FROM projects WHERE project_id = ?";
    private static final String DELETE_PROJECTS_BY_TEAM_ID =
            "DELETE FROM projects WHERE team_id = ?";

    /**
     * Thêm một project mới vào database
     * @param project đối tượng Project cần tạo
     * @return true nếu tạo thành công, false nếu thất bại
     */
    public boolean createProject(Project project) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(INSERT_PROJECT);

            stmt.setString(1, project.getProject_id());
            stmt.setString(2, project.getTeam_id());
            stmt.setString(3, project.getName());
            stmt.setString(4, project.getDescription());

            int rows = stmt.executeUpdate();
            if (rows > 0) {
                System.out.println("Project created: " + project.getProject_id());
                return true;
            }

        } catch (SQLException e) {
            System.err.println("Lỗi khi tạo project: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Lấy danh sách tất cả các project
     * @return List<Project> danh sách project, có thể rỗng nếu không có project nào
     */
    public List<Project> findAll() {
        List<Project> projects = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(SELECT_ALL_PROJECTS);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                projects.add(mapResultSetToProject(rs));
            }
            System.out.println("Found " + projects.size() + " projects");
        } catch (SQLException e) {
            System.err.println("Lỗi khi lấy danh sách projects: " + e.getMessage());
            e.printStackTrace();
        }

        return projects;
    }

    /**
     * Lấy project theo project_id
     * @param project_id mã của project cần tìm
     * @return Project nếu tìm thấy, null nếu không tìm thấy
     */
    public Project findByProjectId(String project_id) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(SELECT_PROJECT_BY_ID);
            stmt.setString(1, project_id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                System.out.println("Project found: " + project_id);
                return mapResultSetToProject(rs);
            } else {
                System.out.println("Project not found: " + project_id);
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi tìm project by id: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Lấy danh sách các project theo team_id
     * @param team_id mã của team
     * @return List<Project> danh sách projects của team, có thể rỗng
     */
    public List<Project> findByTeamId(String team_id) {
        List<Project> projects = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(SELECT_PROJECTS_BY_TEAM_ID);
            stmt.setString(1, team_id);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                projects.add(mapResultSetToProject(rs));
            }
            System.out.println("Found " + projects.size() + " projects for team " + team_id);
        } catch (SQLException e) {
            System.err.println("Lỗi khi lấy projects by team_id: " + e.getMessage());
            e.printStackTrace();
        }

        return projects;
    }

    /**
     * Lấy project theo tên
     * @param name tên project cần tìm
     * @return Project nếu tìm thấy, null nếu không tìm thấy
     */
    public Project findByProjectName(String name) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(SELECT_PROJECT_BY_NAME);
            stmt.setString(1, name);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                System.out.println("Project found: " + name);
                return mapResultSetToProject(rs);
            } else {
                System.out.println("Project not found: " + name);
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi tìm project by name: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Cập nhật toàn bộ thông tin project
     * @param project_id mã project cần cập nhật
     * @param team_id mã team mới
     * @param name tên mới
     * @param description mô tả mới
     * @return true nếu cập nhật thành công, false nếu thất bại
     */
    public boolean updateProject(String project_id, String team_id, String name, String description) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(UPDATE_PROJECT);
            stmt.setString(1, team_id);
            stmt.setString(2, name);
            stmt.setString(3, description);
            stmt.setString(4, project_id);

            int rows = stmt.executeUpdate();
            if (rows > 0) {
                System.out.println("Project updated: " + project_id);
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi update project: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Cập nhật tên project
     * @param project_id mã project cần cập nhật
     * @param name tên mới
     * @return true nếu cập nhật thành công, false nếu thất bại
     */
    public boolean updateProjectName(String project_id, String name) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(UPDATE_PROJECT_NAME);
            stmt.setString(1, name);
            stmt.setString(2, project_id);

            int rows = stmt.executeUpdate();
            if (rows > 0) {
                System.out.println("Project name updated: " + project_id);
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi update project name: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Cập nhật mô tả project
     * @param project_id mã project cần cập nhật
     * @param description mô tả mới
     * @return true nếu cập nhật thành công, false nếu thất bại
     */
    public boolean updateProjectDescription(String project_id, String description) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(UPDATE_PROJECT_DESCRIPTION);
            stmt.setString(1, description);
            stmt.setString(2, project_id);

            int rows = stmt.executeUpdate();
            if (rows > 0) {
                System.out.println("Project description updated: " + project_id);
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi update project description: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Xóa project theo project_id
     * @param project_id mã project cần xóa
     * @return true nếu xóa thành công, false nếu thất bại
     */
    public boolean deleteByProjectId(String project_id) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(DELETE_PROJECT);
            stmt.setString(1, project_id);
            int rows = stmt.executeUpdate();
            if (rows > 0) {
                System.out.println("Project deleted: " + project_id);
                return true;
            } else {
                System.out.println("Project not found: " + project_id);
            }

        } catch (SQLException e) {
            System.err.println("Lỗi khi xóa project: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Xóa tất cả projects của một team
     * @param team_id mã team
     * @return true nếu xóa thành công, false nếu thất bại
     */
    public boolean deleteByTeamId(String team_id) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(DELETE_PROJECTS_BY_TEAM_ID);
            stmt.setString(1, team_id);
            int rows = stmt.executeUpdate();
            if (rows > 0) {
                System.out.println("All projects for team " + team_id + " deleted");
                return true;
            } else {
                System.out.println("No projects found for team " + team_id);
            }

        } catch (SQLException e) {
            System.err.println("Lỗi khi xóa projects by team_id: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Map dữ liệu ResultSet sang đối tượng Project
     * @param rs ResultSet chứa dữ liệu project
     * @return đối tượng Project
     * @throws SQLException nếu lỗi đọc dữ liệu
     */
    public Project mapResultSetToProject(ResultSet rs) throws SQLException {
        Project project = new Project();
        project.setProject_id(rs.getString("project_id"));
        project.setTeam_id(rs.getString("team_id"));
        project.setName(rs.getString("name"));
        project.setDescription(rs.getString("description"));
        return project;
    }

}
