package main.java.service;

import java.util.List;
import main.java.model.CheckListItems;

public interface CheckListItemService {
    /**
     * Tạo một checklist item mới
     * @param checklistItem đối tượng CheckListItems cần tạo
     * @return true nếu tạo thành công, false nếu thất bại
     */
    boolean createCheckListItem(CheckListItems checklistItem);

    /**
     * Lấy danh sách tất cả các checklist items
     * @return List<CheckListItems> danh sách checklist items, có thể rỗng
     */
    List<CheckListItems> getAllCheckListItems();

    /**
     * Lấy checklist item theo itemId
     * @param itemId mã của item cần tìm
     * @return CheckListItems nếu tìm thấy, null nếu không tìm thấy
     */
    CheckListItems getCheckListItemById(String itemId);

    /**
     * Lấy danh sách tất cả items của một checklist
     * @param checklistId mã của checklist
     * @return List<CheckListItems> danh sách items của checklist, có thể rỗng
     */
    List<CheckListItems> getCheckListItemsByChecklistId(String checklistId);

    /**
     * Cập nhật checklist item
     * @param checklistItem đối tượng CheckListItem với thông tin mới
     * @return true nếu cập nhật thành công, false nếu thất bại
     */
    boolean updateCheckListItem(CheckListItems checklistItem);

    /**
     * Cập nhật nội dung checklist item
     * @param itemId mã item cần cập nhật
     * @param content nội dung mới
     * @return true nếu cập nhật thành công, false nếu thất bại
     */
    boolean updateCheckListItemContent(String itemId, String content);

    /**
     * Cập nhật trạng thái hoàn thành của checklist item
     * @param itemId mã item cần cập nhật
     * @param isDone trạng thái hoàn thành mới
     * @return true nếu cập nhật thành công, false nếu thất bại
     */
    boolean updateCheckListItemStatus(String itemId, boolean isDone);

    /**
     * Xóa checklist item theo itemId
     * @param itemId mã item cần xóa
     * @return true nếu xóa thành công, false nếu thất bại
     */
    boolean deleteCheckListItem(String itemId);

    /**
     * Xóa tất cả items của một checklist
     * @param checklistId mã của checklist
     * @return true nếu xóa thành công, false nếu thất bại
     */
    boolean deleteCheckListItemsByChecklistId(String checklistId);

    /**
     * Kiểm tra checklist item có tồn tại không
     * @param itemId mã item
     * @return true nếu item tồn tại
     */
    boolean checkListItemExists(String itemId);

    /**
     * Tính phần trăm hoàn thành của checklist
     * @param checklistId mã checklist
     * @return phần trăm hoàn thành (0-100)
     */
    double getCompletionPercentage(String checklistId);
}
