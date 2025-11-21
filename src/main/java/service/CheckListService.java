package main.java.service;

import java.util.List;
import main.java.model.Checklist;

public interface CheckListService {
    /**
     * Tạo một checklist mới
     * @param checklist đối tượng Checklist cần tạo
     * @return true nếu tạo thành công, false nếu thất bại
     */
    boolean createChecklist(Checklist checklist);

    /**
     * Lấy danh sách tất cả các checklists
     * @return List<Checklist> danh sách checklists, có thể rỗng
     */
    List<Checklist> getAllChecklists();

    /**
     * Lấy checklist theo checklistId
     * @param checklistId mã của checklist cần tìm
     * @return Checklist nếu tìm thấy, null nếu không tìm thấy
     */
    Checklist getChecklistById(String checklistId);

    /**
     * Lấy danh sách tất cả checklists của một task
     * @param taskId mã của task
     * @return List<Checklist> danh sách checklists của task, có thể rỗng
     */
    List<Checklist> getChecklistsByTaskId(String taskId);

    /**
     * Lấy checklist theo tiêu đề
     * @param title tiêu đề checklist cần tìm
     * @param taskId mã task để giới hạn tìm kiếm
     * @return Checklist nếu tìm thấy, null nếu không tìm thấy
     */
    Checklist getChecklistByTitle(String title, String taskId);

    /**
     * Cập nhật checklist
     * @param checklist đối tượng checklist với thông tin mới
     * @return true nếu cập nhật thành công, false nếu thất bại
     */
    boolean updateChecklist(Checklist checklist);

    /**
     * Cập nhật tiêu đề checklist
     * @param checklistId mã checklist cần cập nhật
     * @param title tiêu đề mới
     * @return true nếu cập nhật thành công, false nếu thất bại
     */
    boolean updateChecklistTitle(String checklistId, String title);

    /**
     * Xóa checklist theo checklistId
     * @param checklistId mã checklist cần xóa
     * @return true nếu xóa thành công, false nếu thất bại
     */
    boolean deleteChecklist(String checklistId);

    /**
     * Xóa tất cả checklists của một task
     * @param taskId mã của task
     * @return true nếu xóa thành công, false nếu thất bại
     */
    boolean deleteChecklistsByTaskId(String taskId);

    /**
     * Kiểm tra checklist có tồn tại không
     * @param checklistId mã checklist
     * @return true nếu checklist tồn tại
     */
    boolean checklistExists(String checklistId);

    /**
     * Kiểm tra tiêu đề checklist đã tồn tại trong task chưa
     * @param title tiêu đề checklist
     * @param taskId mã task
     * @return true nếu đã tồn tại
     */
    boolean isTitleExistsInTask(String title, String taskId);
}
