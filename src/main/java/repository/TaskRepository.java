package main.java.repository;

import main.java.config.DatabaseConnection;
import main.java.model.Task;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;

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

            stmt.setString(1, task.getTask_id());
            stmt.setString(2, task.getProject_id());
            stmt.setString(3, task.getTitle());
            stmt.setString(4, task.getDescription());
            stmt.setString(5, task.getStatus());
            stmt.setString(6, task.getPriority());
            stmt.setDate(7, new java.sql.Date(task.getDue_date().getTime()));
            stmt.setString(8, task.getCreated_by());

            int rows = stmt.executeUpdate();
            if (rows > 0) {
                System.out.println("Task created: " + task.getTask_id());
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
     * @param project_id mã project
     * @return List<Task> danh sách task, có thể rỗng nếu không có task nào
     */
    public List<Task> findAll(String project_id) {
        List<Task> tasks = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(SELECT_ALL_TASKS);
            stmt.setString(1, project_id);
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
     * Lấy task theo task_id
     * @param task_id mã của task cần tìm
     * @param project_id mã project
     * @return Task nếu tìm thấy, null nếu không tìm thấy
     */
    public Task findByTaskId(String task_id, String project_id) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(SELECT_TASK_BY_ID);
            stmt.setString(1, task_id);
            stmt.setString(2, project_id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                System.out.println("Task found: " + task_id);
                return mapResultSetToTask(rs);
            } else {
                System.out.println("Task not found: " + task_id);
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi tìm task by id: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Cập nhật toàn bộ thông tin task
     * @param task_id mã task cần cập nhật
     * @param project_id mã project
     * @param title tiêu đề mới
     * @param description mô tả mới
     * @param status trạng thái mới
     * @param priority ưu tiên mới
     * @param due_date hạn chót mới
     * @return true nếu cập nhật thành công, false nếu thất bại
     */
    public boolean updateTask(String task_id, String project_id, String title, String description, 
                              String status, String priority, Date due_date) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(UPDATE_TASK);
            stmt.setString(1, title);
            stmt.setString(2, description);
            stmt.setString(3, status);
            stmt.setString(4, priority);
            stmt.setDate(5, new java.sql.Date(due_date.getTime()));
            stmt.setString(6, task_id);
            stmt.setString(7, project_id);

            int rows = stmt.executeUpdate();
            if (rows > 0) {
                System.out.println("Task updated: " + task_id);
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
     * @param task_id mã task cần cập nhật
     * @param project_id mã project
     * @param status trạng thái mới
     * @return true nếu cập nhật thành công, false nếu thất bại
     */
    public boolean updateTaskStatus(String task_id, String project_id, String status) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(UPDATE_TASK_STATUS);
            stmt.setString(1, status);
            stmt.setString(2, task_id);
            stmt.setString(3, project_id);

            int rows = stmt.executeUpdate();
            if (rows > 0) {
                System.out.println("Task status updated: " + task_id);
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
     * @param task_id mã task cần cập nhật
     * @param project_id mã project
     * @param priority mức độ ưu tiên mới
     * @return true nếu cập nhật thành công, false nếu thất bại
     */
    public boolean updateTaskPriority(String task_id, String project_id, String priority) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(UPDATE_TASK_PRIORITY);
            stmt.setString(1, priority);
            stmt.setString(2, task_id);
            stmt.setString(3, project_id);

            int rows = stmt.executeUpdate();
            if (rows > 0) {
                System.out.println("Task priority updated: " + task_id);
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
     * @param task_id mã task cần cập nhật
     * @param project_id mã project
     * @param due_date hạn chót mới
     * @return true nếu cập nhật thành công, false nếu thất bại
     */
    public boolean updateTaskDueDate(String task_id, String project_id, Date due_date) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(UPDATE_TASK_DUE_DATE);
            stmt.setDate(1, new java.sql.Date(due_date.getTime()));
            stmt.setString(2, task_id);
            stmt.setString(3, project_id);

            int rows = stmt.executeUpdate();
            if (rows > 0) {
                System.out.println("Task due date updated: " + task_id);
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi update task due date: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Xóa task theo task_id
     * @param task_id mã task cần xóa
     * @param project_id mã project
     * @return true nếu xóa thành công, false nếu thất bại
     */
    public boolean deleteByTaskId(String task_id, String project_id) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(DELETE_TASK_BY_ID);
            stmt.setString(1, task_id);
            stmt.setString(2, project_id);
            int rows = stmt.executeUpdate();
            if (rows > 0) {
                System.out.println("Task deleted: " + task_id);
                return true;
            } else {
                System.out.println("Task not found: " + task_id);
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
        task.setTask_id(rs.getString("task_id"));
        task.setProject_id(rs.getString("project_id"));
        task.setTitle(rs.getString("title"));
        task.setDescription(rs.getString("description"));
        task.setStatus(rs.getString("status"));
        task.setPriority(rs.getString("priority"));
        task.setDue_date(new Date(rs.getDate("due_date").getTime()));
        task.setCreated_by(rs.getString("created_by"));
        return task;
    }

}
