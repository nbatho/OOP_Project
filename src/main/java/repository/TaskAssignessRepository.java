package main.java.repository;

import main.java.config.DatabaseConnection;
import main.java.model.TaskAsigness;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TaskAssignessRepository {
    private static final String INSERT_TASK_ASSIGNESS =
            "INSERT INTO task_assigness(task_id, user_id) VALUES (?, ?)";
    private static final String SELECT_ALL_TASK_ASSIGNESS =
            "SELECT task_id, user_id FROM task_assigness";
    private static final String SELECT_TASK_ASSIGNESS_BY_ID =
            "SELECT task_id, user_id FROM task_assigness WHERE task_id = ? AND user_id = ?";
    private static final String SELECT_ASSIGNESS_BY_TASK_ID =
            "SELECT task_id, user_id FROM task_assigness WHERE task_id = ?";
    private static final String SELECT_ASSIGNESS_BY_USER_ID =
            "SELECT task_id, user_id FROM task_assigness WHERE user_id = ?";
    private static final String DELETE_TASK_ASSIGNESS =
            "DELETE FROM task_assigness WHERE task_id = ? AND user_id = ?";
    private static final String DELETE_BY_TASK_ID =
            "DELETE FROM task_assigness WHERE task_id = ?";
    private static final String DELETE_BY_USER_ID =
            "DELETE FROM task_assigness WHERE user_id = ?";

    /**
     * Thêm một task assigness mới vào database
     * @param taskAsigness đối tượng TaskAsigness cần tạo
     * @return true nếu tạo thành công, false nếu thất bại
     */
    public boolean createTaskAssigness(TaskAsigness taskAsigness) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(INSERT_TASK_ASSIGNESS);

            stmt.setString(1, taskAsigness.getTask_id());
            stmt.setString(2, taskAsigness.getUser_id());

            int rows = stmt.executeUpdate();
            if (rows > 0) {
                System.out.println("Task assigness created: Task " + taskAsigness.getTask_id() + " assigned to User " + taskAsigness.getUser_id());
                return true;
            }

        } catch (SQLException e) {
            System.err.println("Lỗi khi tạo task assigness: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Lấy danh sách tất cả các task assigness
     * @return List<TaskAsigness> danh sách task assigness, có thể rỗng nếu không có assigness nào
     */
    public List<TaskAsigness> findAll() {
        List<TaskAsigness> assignessList = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(SELECT_ALL_TASK_ASSIGNESS);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                assignessList.add(mapResultSetToTaskAsigness(rs));
            }
            System.out.println("Found " + assignessList.size() + " task assigness");
        } catch (SQLException e) {
            System.err.println("Lỗi khi lấy danh sách task assigness: " + e.getMessage());
            e.printStackTrace();
        }

        return assignessList;
    }

    /**
     * Lấy task assigness theo task_id và user_id
     * @param task_id mã của task
     * @param user_id mã của user
     * @return TaskAsigness nếu tìm thấy, null nếu không tìm thấy
     */
    public TaskAsigness findByTaskIdAndUserId(String task_id, String user_id) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(SELECT_TASK_ASSIGNESS_BY_ID);
            stmt.setString(1, task_id);
            stmt.setString(2, user_id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                System.out.println("Task assigness found: Task " + task_id + " - User " + user_id);
                return mapResultSetToTaskAsigness(rs);
            } else {
                System.out.println("Task assigness not found: Task " + task_id + " - User " + user_id);
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi tìm task assigness by task_id và user_id: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Lấy danh sách tất cả user được assign cho một task
     * @param task_id mã của task
     * @return List<TaskAsigness> danh sách assigness cho task, có thể rỗng
     */
    public List<TaskAsigness> findByTaskId(String task_id) {
        List<TaskAsigness> assignessList = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(SELECT_ASSIGNESS_BY_TASK_ID);
            stmt.setString(1, task_id);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                assignessList.add(mapResultSetToTaskAsigness(rs));
            }
            System.out.println("Found " + assignessList.size() + " users assigned to task " + task_id);
        } catch (SQLException e) {
            System.err.println("Lỗi khi lấy assigness by task_id: " + e.getMessage());
            e.printStackTrace();
        }

        return assignessList;
    }

    /**
     * Lấy danh sách tất cả task được assign cho một user
     * @param user_id mã của user
     * @return List<TaskAsigness> danh sách tasks được assign cho user, có thể rỗng
     */
    public List<TaskAsigness> findByUserId(String user_id) {
        List<TaskAsigness> assignessList = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(SELECT_ASSIGNESS_BY_USER_ID);
            stmt.setString(1, user_id);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                assignessList.add(mapResultSetToTaskAsigness(rs));
            }
            System.out.println("Found " + assignessList.size() + " tasks assigned to user " + user_id);
        } catch (SQLException e) {
            System.err.println("Lỗi khi lấy assigness by user_id: " + e.getMessage());
            e.printStackTrace();
        }

        return assignessList;
    }

    /**
     * Xóa task assigness theo task_id và user_id
     * @param task_id mã của task
     * @param user_id mã của user
     * @return true nếu xóa thành công, false nếu thất bại
     */
    public boolean deleteByTaskIdAndUserId(String task_id, String user_id) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(DELETE_TASK_ASSIGNESS);
            stmt.setString(1, task_id);
            stmt.setString(2, user_id);
            int rows = stmt.executeUpdate();
            if (rows > 0) {
                System.out.println("Task assigness deleted: Task " + task_id + " - User " + user_id);
                return true;
            } else {
                System.out.println("Task assigness not found: Task " + task_id + " - User " + user_id);
            }

        } catch (SQLException e) {
            System.err.println("Lỗi khi xóa task assigness: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Xóa tất cả assigness của một task
     * @param task_id mã của task
     * @return true nếu xóa thành công, false nếu thất bại
     */
    public boolean deleteByTaskId(String task_id) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(DELETE_BY_TASK_ID);
            stmt.setString(1, task_id);
            int rows = stmt.executeUpdate();
            if (rows > 0) {
                System.out.println("All assigness for task " + task_id + " deleted");
                return true;
            } else {
                System.out.println("No assigness found for task " + task_id);
            }

        } catch (SQLException e) {
            System.err.println("Lỗi khi xóa assigness by task_id: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Xóa tất cả assigness của một user
     * @param user_id mã của user
     * @return true nếu xóa thành công, false nếu thất bại
     */
    public boolean deleteByUserId(String user_id) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(DELETE_BY_USER_ID);
            stmt.setString(1, user_id);
            int rows = stmt.executeUpdate();
            if (rows > 0) {
                System.out.println("All assigness for user " + user_id + " deleted");
                return true;
            } else {
                System.out.println("No assigness found for user " + user_id);
            }

        } catch (SQLException e) {
            System.err.println("Lỗi khi xóa assigness by user_id: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Map dữ liệu ResultSet sang đối tượng TaskAsigness
     * @param rs ResultSet chứa dữ liệu task assigness
     * @return đối tượng TaskAsigness
     * @throws SQLException nếu lỗi đọc dữ liệu
     */
    public TaskAsigness mapResultSetToTaskAsigness(ResultSet rs) throws SQLException {
        TaskAsigness taskAsigness = new TaskAsigness();
        taskAsigness.setTask_id(rs.getString("task_id"));
        taskAsigness.setUser_id(rs.getString("user_id"));
        return taskAsigness;
    }

}
