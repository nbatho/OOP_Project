package main.java.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import main.java.config.DatabaseConnection;
import main.java.model.TaskAssignees;

public class TaskAssigneesRepository {
    private static final String INSERT_TASK_ASSIGNEES =
            "INSERT INTO task_assignees(task_id, user_id) VALUES (?, ?)";
    private static final String SELECT_ALL_TASK_ASSIGNEES =
            "SELECT task_id, user_id FROM task_assignees";
    private static final String SELECT_TASK_ASSIGNEES_BY_ID =
            "SELECT task_id, user_id FROM task_assignees WHERE task_id = ? AND user_id = ?";
    private static final String SELECT_ASSIGNEES_BY_TASK_ID =
            "SELECT task_id, user_id FROM task_assignees WHERE task_id = ?";
    private static final String SELECT_ASSIGNEES_BY_USER_ID =
            "SELECT task_id, user_id FROM task_assignees WHERE user_id = ?";
    private static final String DELETE_TASK_ASSIGNEES =
            "DELETE FROM task_assignees WHERE task_id = ? AND user_id = ?";
    private static final String DELETE_BY_TASK_ID =
            "DELETE FROM task_assignees WHERE task_id = ?";
    private static final String DELETE_BY_USER_ID =
            "DELETE FROM task_assignees WHERE user_id = ?";

    /**
     * Thêm một task assignees mới vào database
     * @param taskAssignees đối tượng TaskAssignees cần tạo
     * @return true nếu tạo thành công, false nếu thất bại
     */
    public boolean createTaskAssignees(TaskAssignees taskAssignees) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(INSERT_TASK_ASSIGNEES);

            stmt.setString(1, taskAssignees.getTaskId());
            stmt.setString(2, taskAssignees.getUserId());

            int rows = stmt.executeUpdate();
            if (rows > 0) {
                System.out.println("Task assignees created: Task " + taskAssignees.getTaskId() + " assigned to User " + taskAssignees.getUserId());
                return true;
            }

        } catch (SQLException e) {
            System.err.println("Lỗi khi tạo task assignees: " + e.getMessage());
        }
        return false;
    }

    /**
     * Lấy danh sách tất cả các task assignees
     * @return List<TaskAssignees> danh sách task assignees, có thể rỗng nếu không có assignees nào
     */
    public List<TaskAssignees> findAll() {
        List<TaskAssignees> assigneesList = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(SELECT_ALL_TASK_ASSIGNEES);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                assigneesList.add(mapResultSetToTaskAssignees(rs));
            }
            System.out.println("Found " + assigneesList.size() + " task assignees");
        } catch (SQLException e) {
            System.err.println("Lỗi khi lấy danh sách task assignees: " + e.getMessage());
        }

        return assigneesList;
    }

    /**
     * Lấy task assignees theo task_id và user_id
     * @param task_id mã của task
     * @param user_id mã của user
     * @return TaskAssignees nếu tìm thấy, null nếu không tìm thấy
     */
    public TaskAssignees findByTaskIdAndUserId(String task_id, String user_id) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(SELECT_TASK_ASSIGNEES_BY_ID);
            stmt.setString(1, task_id);
            stmt.setString(2, user_id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                System.out.println("Task assignees found: Task " + task_id + " - User " + user_id);
                return mapResultSetToTaskAssignees(rs);
            } else {
                System.out.println("Task assignees not found: Task " + task_id + " - User " + user_id);
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi tìm task assignees by task_id và user_id: " + e.getMessage());
        }
        return null;
    }

    /**
     * Lấy danh sách tất cả user được assign cho một task
     * @param task_id mã của task
     * @return List<TaskAssignees> danh sách assignees cho task, có thể rỗng
     */
    public List<TaskAssignees> findByTaskId(String task_id) {
        List<TaskAssignees> assigneesList = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(SELECT_ASSIGNEES_BY_TASK_ID);
            stmt.setString(1, task_id);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                assigneesList.add(mapResultSetToTaskAssignees(rs));
            }
            System.out.println("Found " + assigneesList.size() + " users assigned to task " + task_id);
        } catch (SQLException e) {
            System.err.println("Lỗi khi lấy assignees by task_id: " + e.getMessage());
        }

        return assigneesList;
    }

    /**
     * Lấy danh sách tất cả task được assign cho một user
     * @param user_id mã của user
     * @return List<TaskAssignees> danh sách tasks được assign cho user, có thể rỗng
     */
    public List<TaskAssignees> findByUserId(String user_id) {
        List<TaskAssignees> assigneesList = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(SELECT_ASSIGNEES_BY_USER_ID);
            stmt.setString(1, user_id);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                assigneesList.add(mapResultSetToTaskAssignees(rs));
            }
            System.out.println("Found " + assigneesList.size() + " tasks assigned to user " + user_id);
        } catch (SQLException e) {
            System.err.println("Lỗi khi lấy assignees by user_id: " + e.getMessage());
        }

        return assigneesList;
    }

    /**
     * Xóa task assignees theo task_id và user_id
     * @param task_id mã của task
     * @param user_id mã của user
     * @return true nếu xóa thành công, false nếu thất bại
     */
    public boolean deleteByTaskIdAndUserId(String task_id, String user_id) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(DELETE_TASK_ASSIGNEES);
            stmt.setString(1, task_id);
            stmt.setString(2, user_id);
            int rows = stmt.executeUpdate();
            if (rows > 0) {
                System.out.println("Task assignees deleted: Task " + task_id + " - User " + user_id);
                return true;
            } else {
                System.out.println("Task assignees not found: Task " + task_id + " - User " + user_id);
            }

        } catch (SQLException e) {
            System.err.println("Lỗi khi xóa task assignees: " + e.getMessage());
        }
        return false;
    }

    /**
     * Xóa tất cả assignees của một task
     * @param task_id mã của task
     * @return true nếu xóa thành công, false nếu thất bại
     */
    public boolean deleteByTaskId(String task_id) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(DELETE_BY_TASK_ID);
            stmt.setString(1, task_id);
            int rows = stmt.executeUpdate();
            if (rows > 0) {
                System.out.println("All assignees for task " + task_id + " deleted");
                return true;
            } else {
                System.out.println("No assignees found for task " + task_id);
            }

        } catch (SQLException e) {
            System.err.println("Lỗi khi xóa assignees by task_id: " + e.getMessage());
        }
        return false;
    }

    /**
     * Xóa tất cả assignees của một user
     * @param user_id mã của user
     * @return true nếu xóa thành công, false nếu thất bại
     */
    public boolean deleteByUserId(String user_id) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(DELETE_BY_USER_ID);
            stmt.setString(1, user_id);
            int rows = stmt.executeUpdate();
            if (rows > 0) {
                System.out.println("All assignees for user " + user_id + " deleted");
                return true;
            } else {
                System.out.println("No assignees found for user " + user_id);
            }

        } catch (SQLException e) {
            System.err.println("Lỗi khi xóa assignees by user_id: " + e.getMessage());
        }
        return false;
    }

    /**
     * Map dữ liệu ResultSet sang đối tượng TaskAssignees
     * @param rs ResultSet chứa dữ liệu task assignees
     * @return đối tượng TaskAssignees
     * @throws SQLException nếu lỗi đọc dữ liệu
     */
    public TaskAssignees mapResultSetToTaskAssignees(ResultSet rs) throws SQLException {
        TaskAssignees taskAssignees = new TaskAssignees();
        taskAssignees.setTaskId(rs.getString("task_id"));
        taskAssignees.setUserId(rs.getString("user_id"));
        return taskAssignees;
    }

}