package main.java.repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import main.java.config.DatabaseConnection;
import main.java.model.Comments;

public class CommentRepository {

    private enum SQL {
        INSERT("INSERT INTO comments(comment_id, task_id, user_id, body) VALUES (?, ?, ?, ?)"),
        SELECT_ALL("SELECT comment_id, task_id, user_id, body FROM comments"),
        SELECT_BY_ID("SELECT comment_id, task_id, user_id, body FROM comments WHERE comment_id = ?"),
        SELECT_BY_TASK("SELECT comment_id, task_id, user_id, body FROM comments WHERE task_id = ?"),
        SELECT_BY_USER("SELECT comment_id, task_id, user_id, body FROM comments WHERE user_id = ?"),
        UPDATE_BODY("UPDATE comments SET body = ? WHERE comment_id = ?"),
        DELETE("DELETE FROM comments WHERE comment_id = ?"),
        DELETE_BY_TASK("DELETE FROM comments WHERE task_id = ?"),
        DELETE_BY_USER("DELETE FROM comments WHERE user_id = ?");

        final String query;
        SQL(String q) { this.query = q; }
    }



    public boolean createComment(Comments comment) {
        return executeUpdate(
            SQL.INSERT.query,
            comment.getCommentId(),
            comment.getTaskId(),
            comment.getUserId(),
            comment.getBody()
        );
    }

    public List<Comments> findAll() {
        return queryList(SQL.SELECT_ALL.query);
    }

    public List<Comments> getAllComments() {
        return findAll();
    }

    public Comments findByCommentId(String id) {
        return querySingle(SQL.SELECT_BY_ID.query, id);
    }

    public List<Comments> findByTaskId(String taskId) {
        return queryList(SQL.SELECT_BY_TASK.query, taskId);
    }

    public List<Comments> findByUserId(String userId) {
        return queryList(SQL.SELECT_BY_USER.query, userId);
    }

    public boolean updateCommentBody(String commentId, String body) {
        return executeUpdate(SQL.UPDATE_BODY.query, body, commentId);
    }

    public boolean updateComment(Comments c) {
        return updateCommentBody(c.getCommentId(), c.getBody());
    }

    public boolean deleteByCommentId(String id) {
        return executeUpdate(SQL.DELETE.query, id);
    }

    public boolean deleteByTaskId(String taskId) {
        return executeUpdate(SQL.DELETE_BY_TASK.query, taskId);
    }

    public boolean deleteByUserId(String userId) {
        return executeUpdate(SQL.DELETE_BY_USER.query, userId);
    }



    private Comments map(ResultSet rs) throws SQLException {
        return new Comments(
            rs.getString("comment_id"),
            rs.getString("task_id"),
            rs.getString("user_id"),
            rs.getString("body")
        );
    }

    private List<Comments> queryList(String sql, Object... params) {
        List<Comments> list = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = prepare(conn, sql, params);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) list.add(map(rs));

        } catch (SQLException e) {
            System.err.println("Database error: " + e.getMessage());
        }

        return list;
    }

    private Comments querySingle(String sql, Object... params) {
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
            System.err.println("Database error: " + e.getMessage());
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
