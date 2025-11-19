package main.java.repository;

import main.java.config.DatabaseConnection;
import main.java.model.Role;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RoleRepository {
    private static final String INSERT_ROLE =
            "INSERT INTO roles(role_id, name) VALUES (?, ?)";
    private static final String SELECT_ALL_ROLES =
            "SELECT role_id, name FROM roles";
    private static final String SELECT_ROLE_BY_ID =
            "SELECT role_id, name FROM roles WHERE role_id = ?";
    private static final String SELECT_ROLE_BY_NAME =
            "SELECT role_id, name FROM roles WHERE name = ?";
    private static final String UPDATE_ROLE_NAME =
            "UPDATE roles SET name = ? WHERE role_id = ?";
    private static final String DELETE_ROLE =
            "DELETE FROM roles WHERE role_id = ?";

    /**
     * Thêm một role mới vào database
     * @param role đối tượng Role cần tạo
     * @return true nếu tạo thành công, false nếu thất bại
     */
    public boolean createRole(Role role) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(INSERT_ROLE);

            stmt.setString(1, role.getRole_id());
            stmt.setString(2, role.getName());

            int rows = stmt.executeUpdate();
            if (rows > 0) {
                System.out.println("Role created: " + role.getRole_id());
                return true;
            }

        } catch (SQLException e) {
            System.err.println("Lỗi khi tạo role: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Lấy danh sách tất cả các role
     * @return List<Role> danh sách role, có thể rỗng nếu không có role nào
     */
    public List<Role> findAll() {
        List<Role> roles = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(SELECT_ALL_ROLES);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                roles.add(mapResultSetToRole(rs));
            }
            System.out.println("Found " + roles.size() + " roles");
        } catch (SQLException e) {
            System.err.println("Lỗi khi lấy danh sách roles: " + e.getMessage());
            e.printStackTrace();
        }

        return roles;
    }

    /**
     * Lấy role theo role_id
     * @param role_id mã của role cần tìm
     * @return Role nếu tìm thấy, null nếu không tìm thấy
     */
    public Role findByRoleId(String role_id) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(SELECT_ROLE_BY_ID);
            stmt.setString(1, role_id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                System.out.println("Role found: " + role_id);
                return mapResultSetToRole(rs);
            } else {
                System.out.println("Role not found: " + role_id);
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi tìm role by id: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Lấy role theo tên
     * @param name tên role cần tìm
     * @return Role nếu tìm thấy, null nếu không tìm thấy
     */
    public Role findByRoleName(String name) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(SELECT_ROLE_BY_NAME);
            stmt.setString(1, name);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                System.out.println("Role found: " + name);
                return mapResultSetToRole(rs);
            } else {
                System.out.println("Role not found: " + name);
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi tìm role by name: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Cập nhật tên role theo role_id
     * @param role_id mã role cần cập nhật
     * @param name tên mới
     * @return true nếu cập nhật thành công, false nếu thất bại
     */
    public boolean updateRoleName(String role_id, String name) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(UPDATE_ROLE_NAME);
            stmt.setString(1, name);
            stmt.setString(2, role_id);

            int rows = stmt.executeUpdate();
            if (rows > 0) {
                System.out.println("Role updated: " + role_id);
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi update role: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Xóa role theo role_id
     * @param role_id mã role cần xóa
     * @return true nếu xóa thành công, false nếu thất bại
     */
    public boolean deleteByRoleId(String role_id) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(DELETE_ROLE);
            stmt.setString(1, role_id);
            int rows = stmt.executeUpdate();
            if (rows > 0) {
                System.out.println("Role deleted: " + role_id);
                return true;
            } else {
                System.out.println("Role not found: " + role_id);
            }

        } catch (SQLException e) {
            System.err.println("Lỗi khi xóa role: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Map dữ liệu ResultSet sang đối tượng Role
     * @param rs ResultSet chứa dữ liệu role
     * @return đối tượng Role
     * @throws SQLException nếu lỗi đọc dữ liệu
     */
    public Role mapResultSetToRole(ResultSet rs) throws SQLException {
        Role role = new Role();
        role.setRole_id(rs.getString("role_id"));
        role.setName(rs.getString("name"));
        return role;
    }

}
