package main.java.service;

import main.java.model.CheckListItems;

import java.util.List;

public interface CheckListItemService {
    /**
     * Tạo một checklist item mới
     * @param checklistItem đối tượng CheckListItems cần tạo
     * @return true nếu tạo thành công, false nếu thất bại
     */
    boolean create(CheckListItems checklistItem);

    /**
     * Lấy danh sách tất cả các checklist items
     * @return List<CheckListItems> danh sách checklist items, có thể rỗng
     */
    List<CheckListItems> getAll();

    /**
     * Lấy checklist item theo item_id
     * @param item_id mã của item cần tìm
     * @return CheckListItems nếu tìm thấy, null nếu không tìm thấy
     */
    CheckListItems getById(String item_id);

    /**
     * Lấy danh sách tất cả items của một checklist
     * @param checklist_id mã của checklist
     * @return List<CheckListItems> danh sách items của checklist, có thể rỗng
     */
    List<CheckListItems> getByChecklistId(String checklist_id);

    /**
     * Cập nhật nội dung checklist item
     * @param item_id mã item cần cập nhật
     * @param content nội dung mới
     * @return true nếu cập nhật thành công, false nếu thất bại
     */
    boolean updateContent(String item_id, String content);

    /**
     * Cập nhật trạng thái hoàn thành của checklist item
     * @param item_id mã item cần cập nhật
     * @param is_done trạng thái hoàn thành mới
     * @return true nếu cập nhật thành công, false nếu thất bại
     */
    boolean updateStatus(String item_id, boolean is_done);

    /**
     * Cập nhật toàn bộ thông tin checklist item
     * @param item_id mã item cần cập nhật
     * @param content nội dung mới
     * @param is_done trạng thái hoàn thành mới
     * @return true nếu cập nhật thành công, false nếu thất bại
     */
    boolean update(String item_id, String content, boolean is_done);

    /**
     * Xóa checklist item theo item_id
     * @param item_id mã item cần xóa
     * @return true nếu xóa thành công, false nếu thất bại
     */
    boolean deleteById(String item_id);

    /**
     * Xóa tất cả items của một checklist
     * @param checklist_id mã của checklist
     * @return true nếu xóa thành công, false nếu thất bại
     */
    boolean deleteByChecklistId(String checklist_id);
}
