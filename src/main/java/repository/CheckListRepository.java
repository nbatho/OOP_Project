package main.java.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import main.java.config.DatabaseConnection;
import main.java.model.Checklist;

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

            stmt.setString(1, checklist.getChecklistId());
            stmt.setString(2, checklist.getTaskId());
            stmt.setString(3, checklist.getTitle());

            int rows = stmt.executeUpdate();
            if (rows > 0) {
                System.out.println("Checklist created: " + checklist.getChecklistId());
                return true;
            }

        } catch (SQLException e) {
            System.err.println("Lỗi khi tạo checklist: " + e.getMessage());
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
        }

        return checklists;
    }

    /**
     * Lấy checklist theo checklistId
     * @param checklistId mã của checklist cần tìm
     * @return Checklist nếu tìm thấy, null nếu không tìm thấy
     */
    public Checklist findByChecklistId(String checklistId) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(SELECT_CHECKLIST_BY_ID);
            stmt.setString(1, checklistId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                System.out.println("Checklist found: " + checklistId);
                return mapResultSetToChecklist(rs);
            } else {
                System.out.println("Checklist not found: " + checklistId);
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi tìm checklist by id: " + e.getMessage());
        }
        return null;
    }

    /**
     * Lấy danh sách tất cả checklists của một task
     * @param taskId mã của task
     * @return List<Checklist> danh sách checklists của task, có thể rỗng
     */
    public List<Checklist> findByTaskId(String taskId) {
        List<Checklist> checklists = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(SELECT_CHECKLISTS_BY_TASK_ID);
            stmt.setString(1, taskId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                checklists.add(mapResultSetToChecklist(rs));
            }
            System.out.println("Found " + checklists.size() + " checklists for task " + taskId);
        } catch (SQLException e) {
            System.err.println("Lỗi khi lấy checklists by task_id: " + e.getMessage());
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
        }
        return null;
    }

    /**
     * Cập nhật tiêu đề checklist
     * @param checklistId mã checklist cần cập nhật
     * @param title tiêu đề mới
     * @return true nếu cập nhật thành công, false nếu thất bại
     */
    public boolean updateChecklistTitle(String checklistId, String title) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(UPDATE_CHECKLIST_TITLE);
            stmt.setString(1, title);
            stmt.setString(2, checklistId);

            int rows = stmt.executeUpdate();
            if (rows > 0) {
                System.out.println("Checklist updated: " + checklistId);
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi update checklist: " + e.getMessage());
        }
        return false;
    }

    /**
     * Xóa checklist theo checklistId
     * @param checklistId mã checklist cần xóa
     * @return true nếu xóa thành công, false nếu thất bại
     */
    public boolean deleteByChecklistId(String checklistId) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(DELETE_CHECKLIST);
            stmt.setString(1, checklistId);
            int rows = stmt.executeUpdate();
            if (rows > 0) {
                System.out.println("Checklist deleted: " + checklistId);
                return true;
            } else {
                System.out.println("Checklist not found: " + checklistId);
            }

        } catch (SQLException e) {
            System.err.println("Lỗi khi xóa checklist: " + e.getMessage());
        }
        return false;
    }

    /**
     * Xóa tất cả checklists của một task
     * @param taskId mã của task
     * @return true nếu xóa thành công, false nếu thất bại
     */
    public boolean deleteByTaskId(String taskId) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(DELETE_CHECKLISTS_BY_TASK_ID);
            stmt.setString(1, taskId);
            int rows = stmt.executeUpdate();
            if (rows > 0) {
                System.out.println("All checklists for task " + taskId + " deleted");
                return true;
            } else {
                System.out.println("No checklists found for task " + taskId);
            }

        } catch (SQLException e) {
            System.err.println("Lỗi khi xóa checklists by task_id: " + e.getMessage());
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
        checklist.setChecklistId(rs.getString("checklist_id"));
        checklist.setTaskId(rs.getString("task_id"));
        checklist.setTitle(rs.getString("title"));
        return checklist;
    }

}
