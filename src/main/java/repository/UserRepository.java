package main.java.repository;

import java.sql.*;
import java.util.*;
import main.java.config.DatabaseConnection;
import main.java.model.User;

public class UserRepository {

    private enum SQL {
        INSERT("INSERT INTO users (user_id, full_name, email, password_hash) VALUES (?, ?, ?, ?)"),
        SELECT_BY_ID("SELECT user_id, full_name, email, password_hash, created_at FROM users WHERE user_id = ?"),
        SELECT_BY_EMAIL("SELECT user_id, full_name, email, password_hash, created_at FROM users WHERE email = ?"),
        SELECT_ALL("SELECT user_id, full_name, email, password_hash, created_at FROM users ORDER BY created_at DESC"),
        UPDATE("UPDATE users SET full_name = ?, email = ?, password_hash = ? WHERE user_id = ?"),
        DELETE("DELETE FROM users WHERE user_id = ?"),
        EMAIL_EXISTS("SELECT COUNT(*) AS total FROM users WHERE email = ?"),

        SELECT_BY_TEAM("""
            SELECT u.user_id, u.full_name, u.email, u.password_hash, u.created_at
            FROM users u
            INNER JOIN team_members tm ON u.user_id = tm.user_id
            WHERE tm.team_id = ?
        """);

        final String query;
        SQL(String q) { this.query = q; }
    }

    /* =========================== PUBLIC METHODS =========================== */

    public boolean createUser(User user) {
        if (user.getUserId() == null || user.getUserId().isEmpty()) {
            user.setUserId(UUID.randomUUID().toString());
        }

        return executeUpdate(
                SQL.INSERT.query,
                user.getUserId(),
                user.getFullName(),
                user.getEmail(),
                user.getPasswordHash()
        );
    }

    public User getUserById(String userId) {
        return querySingle(SQL.SELECT_BY_ID.query, userId);
    }

    public User getUserByEmail(String email) {
        return querySingle(SQL.SELECT_BY_EMAIL.query, email);
    }

    public List<User> getAllUsers() {
        return queryList(SQL.SELECT_ALL.query);
    }

    public boolean updateUser(User user) {
        return executeUpdate(
                SQL.UPDATE.query,
                user.getFullName(),
                user.getEmail(),
                user.getPasswordHash(),
                user.getUserId()
        );
    }

    public boolean deleteUser(String userId) {
        return executeUpdate(SQL.DELETE.query, userId);
    }

    public boolean emailExists(String email) {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = prepare(conn, SQL.EMAIL_EXISTS.query, email);
             ResultSet rs = stmt.executeQuery()) {

            return rs.next() && rs.getInt("total") > 0;

        } catch (SQLException e) {
            System.err.println("Database error: " + e.getMessage());
            return false;
        }
    }

    public List<User> getUsersByTeamId(String teamId) {
        return queryList(SQL.SELECT_BY_TEAM.query, teamId);
    }

    /* =========================== PRIVATE HELPERS =========================== */

    private User map(ResultSet rs) throws SQLException {
        return new User(
                rs.getString("user_id"),
                rs.getString("full_name"),
                rs.getString("email"),
                rs.getString("password_hash"),
                rs.getTimestamp("created_at")
        );
    }

    private List<User> queryList(String sql, Object... params) {
        List<User> list = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = prepare(conn, sql, params);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) list.add(map(rs));

        } catch (SQLException e) {
            System.err.println("Database error: " + e.getMessage());
        }

        return list;
    }

    private User querySingle(String sql, Object... params) {
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

        for (int i = 0; i < params.length; i++)
            stmt.setObject(i + 1, params[i]);

        return stmt;
    }
}
