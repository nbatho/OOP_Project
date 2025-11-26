package main.java.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import main.java.config.DatabaseConnection;
import main.java.model.CheckListItems;

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

            stmt.setString(1, checklistItem.getItemId());
            stmt.setString(2, checklistItem.getChecklistId());
            stmt.setString(3, checklistItem.getContent());
            stmt.setBoolean(4, checklistItem.isDone());

            int rows = stmt.executeUpdate();
            if (rows > 0) {
//                System.out.println("Checklist item created: " + checklistItem.getItemId());
                return true;
            }

        } catch (SQLException e) {
            System.err.println("Lỗi khi tạo checklist item: " + e.getMessage());
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
//            System.out.println("Found " + items.size() + " checklist items");
        } catch (SQLException e) {
            System.err.println("Lỗi khi lấy danh sách checklist items: " + e.getMessage());
        }

        return items;
    }

    /**
     * Lấy checklist item theo itemId
     * @param itemId mã của item cần tìm
     * @return CheckListItems nếu tìm thấy, null nếu không tìm thấy
     */
    public CheckListItems findByItemId(String itemId) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(SELECT_CHECKLIST_ITEM_BY_ID);
            stmt.setString(1, itemId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
//                System.out.println("Checklist item found: " + itemId);
                return mapResultSetToChecklistItem(rs);
            } else {
                System.out.println("Checklist item not found: " + itemId);
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi tìm checklist item by id: " + e.getMessage());
        }
        return null;
    }

    /**
     * Lấy danh sách tất cả items của một checklist
     * @param checklistId mã của checklist
     * @return List<CheckListItems> danh sách items của checklist, có thể rỗng
     */
    public List<CheckListItems> findByChecklistId(String checklistId) {
        List<CheckListItems> items = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(SELECT_ITEMS_BY_CHECKLIST_ID);
            stmt.setString(1, checklistId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                items.add(mapResultSetToChecklistItem(rs));
            }
//            System.out.println("Found " + items.size() + " items in checklist " + checklistId);
        } catch (SQLException e) {
            System.err.println("Lỗi khi lấy items by checklist_id: " + e.getMessage());
        }

        return items;
    }

    /**
     * Cập nhật nội dung checklist item
     * @param itemId mã item cần cập nhật
     * @param content nội dung mới
     * @return true nếu cập nhật thành công, false nếu thất bại
     */
    public boolean updateChecklistItemContent(String itemId, String content) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(UPDATE_CHECKLIST_ITEM_CONTENT);
            stmt.setString(1, content);
            stmt.setString(2, itemId);

            int rows = stmt.executeUpdate();
            if (rows > 0) {
//                System.out.println("Checklist item content updated: " + itemId);
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi update checklist item content: " + e.getMessage());
        }
        return false;
    }

    /**
     * Cập nhật trạng thái hoàn thành của checklist item
     * @param itemId mã item cần cập nhật
     * @param isDone trạng thái hoàn thành mới
     * @return true nếu cập nhật thành công, false nếu thất bại
     */
    public boolean updateChecklistItemStatus(String itemId, boolean isDone) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(UPDATE_CHECKLIST_ITEM_STATUS);
            stmt.setBoolean(1, isDone);
            stmt.setString(2, itemId);

            int rows = stmt.executeUpdate();
            if (rows > 0) {
//                System.out.println("Checklist item status updated: " + itemId);
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi update checklist item status: " + e.getMessage());
        }
        return false;
    }

    /**
     * Cập nhật toàn bộ thông tin checklist item
     * @param itemId mã item cần cập nhật
     * @param content nội dung mới
     * @param isDone trạng thái hoàn thành mới
     * @return true nếu cập nhật thành công, false nếu thất bại
     */
    public boolean updateChecklistItem(String itemId, String content, boolean isDone) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(UPDATE_CHECKLIST_ITEM);
            stmt.setString(1, content);
            stmt.setBoolean(2, isDone);
            stmt.setString(3, itemId);

            int rows = stmt.executeUpdate();
            if (rows > 0) {
//                System.out.println("Checklist item updated: " + itemId);
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi update checklist item: " + e.getMessage());
        }
        return false;
    }

    /**
     * Xóa checklist item theo itemId
     * @param itemId mã item cần xóa
     * @return true nếu xóa thành công, false nếu thất bại
     */
    public boolean deleteByItemId(String itemId) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(DELETE_CHECKLIST_ITEM);
            stmt.setString(1, itemId);
            int rows = stmt.executeUpdate();
            if (rows > 0) {
//                System.out.println("Checklist item deleted: " + itemId);
                return true;
            } else {
                System.out.println("Checklist item not found: " + itemId);
            }

        } catch (SQLException e) {
            System.err.println("Lỗi khi xóa checklist item: " + e.getMessage());
        }
        return false;
    }

    /**
     * Xóa tất cả items của một checklist
     * @param checklistId mã của checklist
     * @return true nếu xóa thành công, false nếu thất bại
     */
    public boolean deleteByChecklistId(String checklistId) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(DELETE_ITEMS_BY_CHECKLIST_ID);
            stmt.setString(1, checklistId);
            int rows = stmt.executeUpdate();
            if (rows > 0) {
//                System.out.println("All items for checklist " + checklistId + " deleted");
                return true;
            } else {
                System.out.println("No items found for checklist " + checklistId);
            }

        } catch (SQLException e) {
            System.err.println("Lỗi khi xóa items by checklist_id: " + e.getMessage());
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
        item.setItemId(rs.getString("item_id"));
        item.setChecklistId(rs.getString("checklist_id"));
        item.setContent(rs.getString("content"));
        item.setDone(rs.getBoolean("is_done"));
        return item;
    }

}
