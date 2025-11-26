package main.java.repository;

import java.sql.*;
import java.util.*;
import main.java.config.DatabaseConnection;
import main.java.model.Task;

public class TaskRepository {

    private enum SQL {
        INSERT("""
            INSERT INTO tasks(task_id, project_id, title, description, status, priority, due_date, created_by)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?)
        """),
        SELECT_ALL("SELECT * FROM tasks WHERE project_id = ?"),
        SELECT_BY_ID("SELECT * FROM tasks WHERE task_id = ? AND project_id = ?"),

        UPDATE("""
            UPDATE tasks
            SET title = ?, description = ?, status = ?, priority = ?, due_date = ?
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
        return executeUpdate(
                SQL.INSERT.query,
                task.getTaskId(),
                task.getProjectId(),
                task.getTitle(),
                task.getDescription(),
                task.getStatus(),
                task.getPriority(),
                new java.sql.Date(task.getDueDate().getTime()),
                task.getCreatedBy()
        );
    }

    public List<Task> findAll(String projectId) {
        return queryList(SQL.SELECT_ALL.query, projectId);
    }

    public Task findByTaskId(String taskId, String projectId) {
        return querySingle(SQL.SELECT_BY_ID.query, taskId, projectId);
    }

    public boolean updateTask(String taskId, String projectId, String title,
                              String description, String status, String priority, java.util.Date dueDate) {
        return executeUpdate(
                SQL.UPDATE.query,
                title,
                description,
                status,
                priority,
                new java.sql.Date(dueDate.getTime()),
                taskId,
                projectId
        );
    }

    public boolean updateTaskStatus(String taskId, String projectId, String status) {
        return executeUpdate(SQL.UPDATE_STATUS.query, status, taskId, projectId);
    }

    public boolean updateTaskPriority(String taskId, String projectId, String priority) {
        return executeUpdate(SQL.UPDATE_PRIORITY.query, priority, taskId, projectId);
    }

    public boolean updateTaskDueDate(String taskId, String projectId, java.util.Date dueDate) {
        return executeUpdate(
                SQL.UPDATE_DUE_DATE.query,
                new java.sql.Date(dueDate.getTime()),
                taskId,
                projectId
        );
    }

    public boolean deleteByTaskId(String taskId, String projectId) {
        return executeUpdate(SQL.DELETE.query, taskId, projectId);
    }

    /* ============================== PRIVATE HELPERS ============================== */

    private Task map(ResultSet rs) throws SQLException {
        return new Task(
                rs.getString("task_id"),
                rs.getString("project_id"),
                rs.getString("title"),
                rs.getString("description"),
                rs.getString("status"),
                rs.getString("priority"),
                rs.getDate("due_date"),
                rs.getString("created_by")
        );
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
