package main.java.repository;

import main.java.config.DatabaseConnection;
import main.java.model.CheckListItems;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CheckListItemRepository {
    private static final String INSERT_CHECKLIST_ITEM =
            "INSERT INTO checklist_items(item_id, checklist_id, content, is_done) VALUES (?, ?, ?, ?)";
    private static final String SELECT_ALL_CHECKLIST_ITEMS =
            "SELECT item_id, checklist_id, content, is_done FROM checklist_items";
    private static final String SELECT_CHECKLIST_ITEM_BY_ID =
            "SELECT item_id, checklist_id, content, is_done FROM checklist_items WHERE item_id = ?";
    private static final String SELECT_ITEMS_BY_CHECKLIST_ID =
            "SELECT item_id, checklist_id, content, is_done FROM checklist_items WHERE checklist_id = ?";
    private static final String UPDATE_CHECKLIST_ITEM_CONTENT =
            "UPDATE checklist_items SET content = ? WHERE item_id = ?";
    private static final String UPDATE_CHECKLIST_ITEM_STATUS =
            "UPDATE checklist_items SET is_done = ? WHERE item_id = ?";
    private static final String UPDATE_CHECKLIST_ITEM =
            "UPDATE checklist_items SET content = ?, is_done = ? WHERE item_id = ?";
    private static final String DELETE_CHECKLIST_ITEM =
            "DELETE FROM checklist_items WHERE item_id = ?";
    private static final String DELETE_ITEMS_BY_CHECKLIST_ID =
            "DELETE FROM checklist_items WHERE checklist_id = ?";

    /**
     * Thêm một checklist item mới vào database
     * @param checklistItem đối tượng CheckListItems cần tạo
     * @return true nếu tạo thành công, false nếu thất bại
     */
    public boolean createChecklistItem(CheckListItems checklistItem) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(INSERT_CHECKLIST_ITEM);

            stmt.setString(1, checklistItem.getItem_id());
            stmt.setString(2, checklistItem.getChecklist_id());
            stmt.setString(3, checklistItem.getContent());
            stmt.setBoolean(4, checklistItem.isIs_done());

            int rows = stmt.executeUpdate();
            if (rows > 0) {
                System.out.println("Checklist item created: " + checklistItem.getItem_id());
                return true;
            }

        } catch (SQLException e) {
            System.err.println("Lỗi khi tạo checklist item: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Lấy danh sách tất cả các checklist items
     * @return List<CheckListItems> danh sách checklist items, có thể rỗng nếu không có item nào
     */
    public List<CheckListItems> findAll() {
        List<CheckListItems> items = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(SELECT_ALL_CHECKLIST_ITEMS);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                items.add(mapResultSetToChecklistItem(rs));
            }
            System.out.println("Found " + items.size() + " checklist items");
        } catch (SQLException e) {
            System.err.println("Lỗi khi lấy danh sách checklist items: " + e.getMessage());
            e.printStackTrace();
        }

        return items;
    }

    /**
     * Lấy checklist item theo item_id
     * @param item_id mã của item cần tìm
     * @return CheckListItems nếu tìm thấy, null nếu không tìm thấy
     */
    public CheckListItems findByItemId(String item_id) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(SELECT_CHECKLIST_ITEM_BY_ID);
            stmt.setString(1, item_id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                System.out.println("Checklist item found: " + item_id);
                return mapResultSetToChecklistItem(rs);
            } else {
                System.out.println("Checklist item not found: " + item_id);
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi tìm checklist item by id: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Lấy danh sách tất cả items của một checklist
     * @param checklist_id mã của checklist
     * @return List<CheckListItems> danh sách items của checklist, có thể rỗng
     */
    public List<CheckListItems> findByChecklistId(String checklist_id) {
        List<CheckListItems> items = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(SELECT_ITEMS_BY_CHECKLIST_ID);
            stmt.setString(1, checklist_id);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                items.add(mapResultSetToChecklistItem(rs));
            }
            System.out.println("Found " + items.size() + " items in checklist " + checklist_id);
        } catch (SQLException e) {
            System.err.println("Lỗi khi lấy items by checklist_id: " + e.getMessage());
            e.printStackTrace();
        }

        return items;
    }

    /**
     * Cập nhật nội dung checklist item
     * @param item_id mã item cần cập nhật
     * @param content nội dung mới
     * @return true nếu cập nhật thành công, false nếu thất bại
     */
    public boolean updateChecklistItemContent(String item_id, String content) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(UPDATE_CHECKLIST_ITEM_CONTENT);
            stmt.setString(1, content);
            stmt.setString(2, item_id);

            int rows = stmt.executeUpdate();
            if (rows > 0) {
                System.out.println("Checklist item content updated: " + item_id);
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi update checklist item content: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Cập nhật trạng thái hoàn thành của checklist item
     * @param item_id mã item cần cập nhật
     * @param is_done trạng thái hoàn thành mới
     * @return true nếu cập nhật thành công, false nếu thất bại
     */
    public boolean updateChecklistItemStatus(String item_id, boolean is_done) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(UPDATE_CHECKLIST_ITEM_STATUS);
            stmt.setBoolean(1, is_done);
            stmt.setString(2, item_id);

            int rows = stmt.executeUpdate();
            if (rows > 0) {
                System.out.println("Checklist item status updated: " + item_id);
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi update checklist item status: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Cập nhật toàn bộ thông tin checklist item
     * @param item_id mã item cần cập nhật
     * @param content nội dung mới
     * @param is_done trạng thái hoàn thành mới
     * @return true nếu cập nhật thành công, false nếu thất bại
     */
    public boolean updateChecklistItem(String item_id, String content, boolean is_done) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(UPDATE_CHECKLIST_ITEM);
            stmt.setString(1, content);
            stmt.setBoolean(2, is_done);
            stmt.setString(3, item_id);

            int rows = stmt.executeUpdate();
            if (rows > 0) {
                System.out.println("Checklist item updated: " + item_id);
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi update checklist item: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Xóa checklist item theo item_id
     * @param item_id mã item cần xóa
     * @return true nếu xóa thành công, false nếu thất bại
     */
    public boolean deleteByItemId(String item_id) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(DELETE_CHECKLIST_ITEM);
            stmt.setString(1, item_id);
            int rows = stmt.executeUpdate();
            if (rows > 0) {
                System.out.println("Checklist item deleted: " + item_id);
                return true;
            } else {
                System.out.println("Checklist item not found: " + item_id);
            }

        } catch (SQLException e) {
            System.err.println("Lỗi khi xóa checklist item: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Xóa tất cả items của một checklist
     * @param checklist_id mã của checklist
     * @return true nếu xóa thành công, false nếu thất bại
     */
    public boolean deleteByChecklistId(String checklist_id) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(DELETE_ITEMS_BY_CHECKLIST_ID);
            stmt.setString(1, checklist_id);
            int rows = stmt.executeUpdate();
            if (rows > 0) {
                System.out.println("All items for checklist " + checklist_id + " deleted");
                return true;
            } else {
                System.out.println("No items found for checklist " + checklist_id);
            }

        } catch (SQLException e) {
            System.err.println("Lỗi khi xóa items by checklist_id: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Map dữ liệu ResultSet sang đối tượng CheckListItems
     * @param rs ResultSet chứa dữ liệu checklist item
     * @return đối tượng CheckListItems
     * @throws SQLException nếu lỗi đọc dữ liệu
     */
    public CheckListItems mapResultSetToChecklistItem(ResultSet rs) throws SQLException {
        CheckListItems item = new CheckListItems();
        item.setItem_id(rs.getString("item_id"));
        item.setChecklist_id(rs.getString("checklist_id"));
        item.setContent(rs.getString("content"));
        item.setIs_done(rs.getBoolean("is_done"));
        return item;
    }

}
