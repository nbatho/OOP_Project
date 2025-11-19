package main.java.repository;

import main.java.config.DatabaseConnection;
import main.java.model.Checklist;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CheckListRepository {
    private static final String INSERT_CHECKLIST =
            "INSERT INTO checklists(checklist_id, task_id, title) VALUES (?, ?, ?)";
    private static final String SELECT_ALL_CHECKLISTS =
            "SELECT checklist_id, task_id, title FROM checklists";
    private static final String SELECT_CHECKLIST_BY_ID =
            "SELECT checklist_id, task_id, title FROM checklists WHERE checklist_id = ?";
    private static final String SELECT_CHECKLISTS_BY_TASK_ID =
            "SELECT checklist_id, task_id, title FROM checklists WHERE task_id = ?";
    private static final String SELECT_CHECKLIST_BY_TITLE =
            "SELECT checklist_id, task_id, title FROM checklists WHERE title = ?";
    private static final String UPDATE_CHECKLIST_TITLE =
            "UPDATE checklists SET title = ? WHERE checklist_id = ?";
    private static final String DELETE_CHECKLIST =
            "DELETE FROM checklists WHERE checklist_id = ?";
    private static final String DELETE_CHECKLISTS_BY_TASK_ID =
            "DELETE FROM checklists WHERE task_id = ?";

    /**
     * Thêm một checklist mới vào database
     * @param checklist đối tượng Checklist cần tạo
     * @return true nếu tạo thành công, false nếu thất bại
     */
    public boolean createChecklist(Checklist checklist) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(INSERT_CHECKLIST);

            stmt.setString(1, checklist.getChecklist_id());
            stmt.setString(2, checklist.getTask_id());
            stmt.setString(3, checklist.getTitle());

            int rows = stmt.executeUpdate();
            if (rows > 0) {
                System.out.println("Checklist created: " + checklist.getChecklist_id());
                return true;
            }

        } catch (SQLException e) {
            System.err.println("Lỗi khi tạo checklist: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Lấy danh sách tất cả các checklists
     * @return List<Checklist> danh sách checklists, có thể rỗng nếu không có checklist nào
     */
    public List<Checklist> findAll() {
        List<Checklist> checklists = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(SELECT_ALL_CHECKLISTS);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                checklists.add(mapResultSetToChecklist(rs));
            }
            System.out.println("Found " + checklists.size() + " checklists");
        } catch (SQLException e) {
            System.err.println("Lỗi khi lấy danh sách checklists: " + e.getMessage());
            e.printStackTrace();
        }

        return checklists;
    }

    /**
     * Lấy checklist theo checklist_id
     * @param checklist_id mã của checklist cần tìm
     * @return Checklist nếu tìm thấy, null nếu không tìm thấy
     */
    public Checklist findByChecklistId(String checklist_id) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(SELECT_CHECKLIST_BY_ID);
            stmt.setString(1, checklist_id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                System.out.println("Checklist found: " + checklist_id);
                return mapResultSetToChecklist(rs);
            } else {
                System.out.println("Checklist not found: " + checklist_id);
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi tìm checklist by id: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Lấy danh sách tất cả checklists của một task
     * @param task_id mã của task
     * @return List<Checklist> danh sách checklists của task, có thể rỗng
     */
    public List<Checklist> findByTaskId(String task_id) {
        List<Checklist> checklists = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(SELECT_CHECKLISTS_BY_TASK_ID);
            stmt.setString(1, task_id);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                checklists.add(mapResultSetToChecklist(rs));
            }
            System.out.println("Found " + checklists.size() + " checklists for task " + task_id);
        } catch (SQLException e) {
            System.err.println("Lỗi khi lấy checklists by task_id: " + e.getMessage());
            e.printStackTrace();
        }

        return checklists;
    }

    /**
     * Lấy checklist theo tiêu đề
     * @param title tiêu đề checklist cần tìm
     * @return Checklist nếu tìm thấy, null nếu không tìm thấy
     */
    public Checklist findByTitle(String title) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(SELECT_CHECKLIST_BY_TITLE);
            stmt.setString(1, title);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                System.out.println("Checklist found: " + title);
                return mapResultSetToChecklist(rs);
            } else {
                System.out.println("Checklist not found: " + title);
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi tìm checklist by title: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Cập nhật tiêu đề checklist
     * @param checklist_id mã checklist cần cập nhật
     * @param title tiêu đề mới
     * @return true nếu cập nhật thành công, false nếu thất bại
     */
    public boolean updateChecklistTitle(String checklist_id, String title) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(UPDATE_CHECKLIST_TITLE);
            stmt.setString(1, title);
            stmt.setString(2, checklist_id);

            int rows = stmt.executeUpdate();
            if (rows > 0) {
                System.out.println("Checklist updated: " + checklist_id);
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi update checklist: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Xóa checklist theo checklist_id
     * @param checklist_id mã checklist cần xóa
     * @return true nếu xóa thành công, false nếu thất bại
     */
    public boolean deleteByChecklistId(String checklist_id) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(DELETE_CHECKLIST);
            stmt.setString(1, checklist_id);
            int rows = stmt.executeUpdate();
            if (rows > 0) {
                System.out.println("Checklist deleted: " + checklist_id);
                return true;
            } else {
                System.out.println("Checklist not found: " + checklist_id);
            }

        } catch (SQLException e) {
            System.err.println("Lỗi khi xóa checklist: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Xóa tất cả checklists của một task
     * @param task_id mã của task
     * @return true nếu xóa thành công, false nếu thất bại
     */
    public boolean deleteByTaskId(String task_id) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(DELETE_CHECKLISTS_BY_TASK_ID);
            stmt.setString(1, task_id);
            int rows = stmt.executeUpdate();
            if (rows > 0) {
                System.out.println("All checklists for task " + task_id + " deleted");
                return true;
            } else {
                System.out.println("No checklists found for task " + task_id);
            }

        } catch (SQLException e) {
            System.err.println("Lỗi khi xóa checklists by task_id: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Map dữ liệu ResultSet sang đối tượng Checklist
     * @param rs ResultSet chứa dữ liệu checklist
     * @return đối tượng Checklist
     * @throws SQLException nếu lỗi đọc dữ liệu
     */
    public Checklist mapResultSetToChecklist(ResultSet rs) throws SQLException {
        Checklist checklist = new Checklist();
        checklist.setChecklist_id(rs.getString("checklist_id"));
        checklist.setTask_id(rs.getString("task_id"));
        checklist.setTitle(rs.getString("title"));
        return checklist;
    }

}
