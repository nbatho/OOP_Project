package main.java.service;

import main.java.model.Checklist;

import java.util.List;

public interface CheckListService {
    /**
     * Tạo một checklist mới
     * @param checklist đối tượng Checklist cần tạo
     * @return true nếu tạo thành công, false nếu thất bại
     */
    boolean create(Checklist checklist);

    /**
     * Lấy danh sách tất cả các checklists
     * @return List<Checklist> danh sách checklists, có thể rỗng
     */
    List<Checklist> getAll();

    /**
     * Lấy checklist theo checklist_id
     * @param checklist_id mã của checklist cần tìm
     * @return Checklist nếu tìm thấy, null nếu không tìm thấy
     */
    Checklist getById(String checklist_id);

    /**
     * Lấy danh sách tất cả checklists của một task
     * @param task_id mã của task
     * @return List<Checklist> danh sách checklists của task, có thể rỗng
     */
    List<Checklist> getByTaskId(String task_id);

    /**
     * Lấy checklist theo tiêu đề
     * @param title tiêu đề checklist cần tìm
     * @return Checklist nếu tìm thấy, null nếu không tìm thấy
     */
    Checklist getByTitle(String title);

    /**
     * Cập nhật tiêu đề checklist
     * @param checklist_id mã checklist cần cập nhật
     * @param title tiêu đề mới
     * @return true nếu cập nhật thành công, false nếu thất bại
     */
    boolean updateTitle(String checklist_id, String title);

    /**
     * Xóa checklist theo checklist_id
     * @param checklist_id mã checklist cần xóa
     * @return true nếu xóa thành công, false nếu thất bại
     */
    boolean deleteById(String checklist_id);

    /**
     * Xóa tất cả checklists của một task
     * @param task_id mã của task
     * @return true nếu xóa thành công, false nếu thất bại
     */
    boolean deleteByTaskId(String task_id);
}
