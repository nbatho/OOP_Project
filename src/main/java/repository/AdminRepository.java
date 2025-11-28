package main.java.repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import main.java.config.DatabaseConnection;
import main.java.model.Admin;

public class AdminRepository {

    private enum SQL {
        INSERT("INSERT INTO admins (user_id) VALUES (?)"),
        SELECT_ALL("SELECT user_id FROM admins"),
        SELECT_BY_ID("SELECT user_id FROM admins WHERE user_id = ?"),
        DELETE("DELETE FROM admins WHERE user_id = ?");

        final String query;

        SQL(String q) { this.query = q; }
    }


    public boolean createAdmin(Admin admin) {
        return executeUpdate(SQL.INSERT.query, admin.getUserId());
    }

    public List<Admin> getAllAdmins() {
        return queryList(SQL.SELECT_ALL.query);
    }

    public Admin getAdminById(String userId) {
        return querySingle(SQL.SELECT_BY_ID.query, userId);
    }

    public boolean updateAdmin(Admin admin) {
        return true;
    }

    public boolean deleteAdmin(String userId) {
        return executeUpdate(SQL.DELETE.query, userId);
    }


    private Admin map(ResultSet rs) throws SQLException {
        return new Admin(
            rs.getString("user_id"),
            rs.getString("full_name") != null ? rs.getString("full_name") : "",
            rs.getString("email") != null ? rs.getString("email") : "",
            rs.getString("password_hash") != null ? rs.getString("password_hash") : ""
        );
    }

    private List<Admin> queryList(String sql, Object... params) {
        List<Admin> list = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = prepare(conn, sql, params);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) list.add(map(rs));

        } catch (SQLException e) {
            System.err.println("Database error: " + e.getMessage());
        }
        return list;
    }

    private Admin querySingle(String sql, Object... params) {
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
