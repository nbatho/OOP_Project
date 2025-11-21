package main.java.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import main.java.config.DatabaseConnection;
import main.java.model.Task;

public class TaskRepository {
    private static final String INSERT_TASK =
            "INSERT INTO tasks(task_id,project_id,title,description,status,priority,due_date,created_by) " +
            "VALUES (?,?,?,?,?,?,?,?)";
    private static final String SELECT_ALL_TASKS =
            "SELECT * FROM tasks WHERE project_id = ?";
    private static final String SELECT_TASK_BY_ID =
            "SELECT * FROM tasks WHERE task_id = ? AND project_id = ?";
    private static final String UPDATE_TASK =
            "UPDATE tasks SET title = ?, description = ?, status = ?, priority = ?, due_date = ? WHERE task_id = ? AND project_id = ?";
    private static final String UPDATE_TASK_STATUS =
            "UPDATE tasks SET status = ? WHERE task_id = ? AND project_id = ?";
    private static final String UPDATE_TASK_PRIORITY =
            "UPDATE tasks SET priority = ? WHERE task_id = ? AND project_id = ?";
    private static final String UPDATE_TASK_DUE_DATE =
            "UPDATE tasks SET due_date = ? WHERE task_id = ? AND project_id = ?";
    private static final String DELETE_TASK_BY_ID =
            "DELETE FROM tasks WHERE task_id = ? AND project_id = ?";

    /**
     * Thêm một task mới vào database
     * @param task đối tượng Task cần tạo
     * @return true nếu tạo thành công, false nếu thất bại
     */
    public boolean createTask(Task task) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(INSERT_TASK);

            stmt.setString(1, task.getTaskId());
            stmt.setString(2, task.getProjectId());
            stmt.setString(3, task.getTitle());
            stmt.setString(4, task.getDescription());
            stmt.setString(5, task.getStatus());
            stmt.setString(6, task.getPriority());
            stmt.setDate(7, new java.sql.Date(task.getDueDate().getTime()));
            stmt.setString(8, task.getCreatedBy());

            int rows = stmt.executeUpdate();
            if (rows > 0) {
                System.out.println("Task created: " + task.getTaskId());
                return true;
            }

        } catch (SQLException e) {
            System.err.println("Lỗi khi tạo task: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Lấy danh sách tất cả các task theo project_id
     * @param projectId mã project
     * @return List<Task> danh sách task, có thể rỗng nếu không có task nào
     */
    public List<Task> findAll(String projectId) {
        List<Task> tasks = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(SELECT_ALL_TASKS);
            stmt.setString(1, projectId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                tasks.add(mapResultSetToTask(rs));
            }
            System.out.println("Found " + tasks.size() + " tasks");
        } catch (SQLException e) {
            System.err.println("Lỗi khi lấy danh sách tasks: " + e.getMessage());
            e.printStackTrace();
        }

        return tasks;
    }

    /**
     * Lấy task theo taskId
     * @param taskId mã của task cần tìm
     * @param projectId mã project
     * @return Task nếu tìm thấy, null nếu không tìm thấy
     */
    public Task findByTaskId(String taskId, String projectId) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(SELECT_TASK_BY_ID);
            stmt.setString(1, taskId);
            stmt.setString(2, projectId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                System.out.println("Task found: " + taskId);
                return mapResultSetToTask(rs);
            } else {
                System.out.println("Task not found: " + taskId);
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi tìm task by id: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Cập nhật toàn bộ thông tin task
     * @param taskId mã task cần cập nhật
     * @param projectId mã project
     * @param title tiêu đề mới
     * @param description mô tả mới
     * @param status trạng thái mới
     * @param priority ưu tiên mới
     * @param dueDate hạn chót mới
     * @return true nếu cập nhật thành công, false nếu thất bại
     */
    public boolean updateTask(String taskId, String projectId, String title, String description, 
                              String status, String priority, Date dueDate) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(UPDATE_TASK);
            stmt.setString(1, title);
            stmt.setString(2, description);
            stmt.setString(3, status);
            stmt.setString(4, priority);
            stmt.setDate(5, new java.sql.Date(dueDate.getTime()));
            stmt.setString(6, taskId);
            stmt.setString(7, projectId);

            int rows = stmt.executeUpdate();
            if (rows > 0) {
                System.out.println("Task updated: " + taskId);
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi update task: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Cập nhật trạng thái task
     * @param taskId mã task cần cập nhật
     * @param projectId mã project
     * @param status trạng thái mới
     * @return true nếu cập nhật thành công, false nếu thất bại
     */
    public boolean updateTaskStatus(String taskId, String projectId, String status) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(UPDATE_TASK_STATUS);
            stmt.setString(1, status);
            stmt.setString(2, taskId);
            stmt.setString(3, projectId);

            int rows = stmt.executeUpdate();
            if (rows > 0) {
                System.out.println("Task status updated: " + taskId);
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi update task status: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Cập nhật mức độ ưu tiên task
     * @param taskId mã task cần cập nhật
     * @param projectId mã project
     * @param priority mức độ ưu tiên mới
     * @return true nếu cập nhật thành công, false nếu thất bại
     */
    public boolean updateTaskPriority(String taskId, String projectId, String priority) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(UPDATE_TASK_PRIORITY);
            stmt.setString(1, priority);
            stmt.setString(2, taskId);
            stmt.setString(3, projectId);

            int rows = stmt.executeUpdate();
            if (rows > 0) {
                System.out.println("Task priority updated: " + taskId);
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi update task priority: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Cập nhật hạn chót task
     * @param taskId mã task cần cập nhật
     * @param projectId mã project
     * @param dueDate hạn chót mới
     * @return true nếu cập nhật thành công, false nếu thất bại
     */
    public boolean updateTaskDueDate(String taskId, String projectId, Date dueDate) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(UPDATE_TASK_DUE_DATE);
            stmt.setDate(1, new java.sql.Date(dueDate.getTime()));
            stmt.setString(2, taskId);
            stmt.setString(3, projectId);

            int rows = stmt.executeUpdate();
            if (rows > 0) {
                System.out.println("Task due date updated: " + taskId);
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi update task due date: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Xóa task theo taskId
     * @param taskId mã task cần xóa
     * @param projectId mã project
     * @return true nếu xóa thành công, false nếu thất bại
     */
    public boolean deleteByTaskId(String taskId, String projectId) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(DELETE_TASK_BY_ID);
            stmt.setString(1, taskId);
            stmt.setString(2, projectId);
            int rows = stmt.executeUpdate();
            if (rows > 0) {
                System.out.println("Task deleted: " + taskId);
                return true;
            } else {
                System.out.println("Task not found: " + taskId);
            }

        } catch (SQLException e) {
            System.err.println("Lỗi khi xóa task: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Map dữ liệu ResultSet sang đối tượng Task
     * @param rs ResultSet chứa dữ liệu task
     * @return đối tượng Task
     * @throws SQLException nếu lỗi đọc dữ liệu
     */
    public Task mapResultSetToTask(ResultSet rs) throws SQLException {
        Task task = new Task();
        task.setTaskId(rs.getString("task_id"));
        task.setProjectId(rs.getString("project_id"));
        task.setTitle(rs.getString("title"));
        task.setDescription(rs.getString("description"));
        task.setStatus(rs.getString("status"));
        task.setPriority(rs.getString("priority"));
        task.setDueDate(rs.getDate("due_date"));
        task.setCreatedBy(rs.getString("created_by"));
        return task;
    }

}
