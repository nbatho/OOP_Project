package main.java.repository;

import java.sql.*;
import java.util.*;
import main.java.config.DatabaseConnection;
import main.java.model.Task;

public class TaskRepository {

    private enum SQL {
        INSERT("""
            INSERT INTO tasks(task_id, project_id, title, description, status, priority, start_date, due_date, created_by)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
        """),

        SELECT_ALL("SELECT * FROM tasks WHERE project_id = ?"),
        SELECT_BY_ID("SELECT * FROM tasks WHERE task_id = ? AND project_id = ?"),
        UPDATE("""
            UPDATE tasks
            SET title = ?, description = ?, status = ?, priority = ?, start_date = ?, due_date = ?
            WHERE task_id = ? AND project_id = ?
        """),

        UPDATE_STATUS("UPDATE tasks SET status = ? WHERE task_id = ? AND project_id = ?"),
        UPDATE_PRIORITY("UPDATE tasks SET priority = ? WHERE task_id = ? AND project_id = ?"),
        UPDATE_DUE_DATE("UPDATE tasks SET due_date = ? WHERE task_id = ? AND project_id = ?"),

        DELETE("DELETE FROM tasks WHERE task_id = ? AND project_id = ?");

        final String query;
        SQL(String q) { this.query = q; }
    }

    public boolean createTask(Task task) {
        java.sql.Date sqlStartDate = (task.getStartDate() != null) ? new java.sql.Date(task.getStartDate().getTime()) : null;
        java.sql.Date sqlDueDate = (task.getDueDate() != null) ? new java.sql.Date(task.getDueDate().getTime()) : null;

        return executeUpdate(
                SQL.INSERT.query,
                task.getTaskId(),
                task.getProjectId(),
                task.getTitle(),
                task.getDescription(),
                task.getStatus(),
                task.getPriority(),
                sqlStartDate, // Thêm dòng này
                sqlDueDate,
                task.getCreatedBy()
        );
    }

    public List<Task> findAll(String projectId) {
        return queryList(SQL.SELECT_ALL.query, projectId);
    }

    public Task findByTaskId(String taskId, String projectId) {
        return querySingle(SQL.SELECT_BY_ID.query, taskId, projectId);
    }


    public boolean updateTask(Task task) {

        java.sql.Date sqlStartDate = (task.getStartDate() != null) ? new java.sql.Date(task.getStartDate().getTime()) : null;
        java.sql.Date sqlDueDate = (task.getDueDate() != null) ? new java.sql.Date(task.getDueDate().getTime()) : null;

        return executeUpdate(
                SQL.UPDATE.query,
                task.getTitle(),
                task.getDescription(),
                task.getStatus(),
                task.getPriority(),
                sqlStartDate, // Thêm vào
                sqlDueDate,
                task.getTaskId(),
                task.getProjectId()
        );
    }

    public boolean updateTask(String taskId, String projectId, String title,
                              String description, String status, String priority,
                              java.util.Date startDate, java.util.Date dueDate) {

        java.sql.Date sqlStartDate = (startDate != null) ? new java.sql.Date(startDate.getTime()) : null;
        java.sql.Date sqlDueDate = (dueDate != null) ? new java.sql.Date(dueDate.getTime()) : null;

        return executeUpdate(
                SQL.UPDATE.query,
                title,
                description,
                status,
                priority,
                sqlStartDate, // Thêm vào query
                sqlDueDate,
                taskId,
                projectId
        );
    }

    public boolean deleteByTaskId(String taskId, String projectId) {
        return executeUpdate(SQL.DELETE.query, taskId, projectId);
    }

    private Task map(ResultSet rs) throws SQLException {
        Task task = new Task();
        task.setTaskId(rs.getString("task_id"));
        task.setProjectId(rs.getString("project_id"));
        task.setTitle(rs.getString("title"));
        task.setDescription(rs.getString("description"));
        task.setStatus(rs.getString("status"));
        task.setPriority(rs.getString("priority"));

        task.setStartDate(rs.getDate("start_date"));

        task.setDueDate(rs.getDate("due_date"));
        task.setCreatedBy(rs.getString("created_by"));
        task.setCreatedAt(rs.getTimestamp("created_at")); // Đừng quên created_at nếu có

        return task;
    }

    private List<Task> queryList(String sql, Object... params) {
        List<Task> list = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = prepare(conn, sql, params);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) list.add(map(rs));
        } catch (SQLException e) {
            System.err.println("Database error: " + e.getMessage());
        }
        return list;
    }

    private Task querySingle(String sql, Object... params) {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = prepare(conn, sql, params);
             ResultSet rs = stmt.executeQuery()) {
            return rs.next() ? map(rs) : null;
        } catch (SQLException e) {
            System.err.println("Database error: " + e.getMessage());
            return null;
        }
    }

    private boolean executeUpdate(String sql, Object... params) {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = prepare(conn, sql, params)) {
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Database error in TaskRepository: " + e.getMessage());
            return false;
        }
    }

    private PreparedStatement prepare(Connection conn, String sql, Object... params) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement(sql);
        for (int i = 0; i < params.length; i++) {
            stmt.setObject(i + 1, params[i]);
        }
        return stmt;
    }
}