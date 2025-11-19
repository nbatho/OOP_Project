package main.java.service;

import main.java.model.Task;

import java.util.Date;
import java.util.List;

public interface TaskService {
    /**
     * Tạo một task mới
     * @param task đối tượng Task cần tạo
     * @return true nếu tạo thành công, false nếu thất bại
     */
    boolean create(Task task);

    /**
     * Lấy danh sách tất cả các task theo project_id
     * @param project_id mã project
     * @return List<Task> danh sách task, có thể rỗng
     */
    List<Task> getAll(String project_id);

    /**
     * Lấy task theo task_id
     * @param task_id mã của task cần tìm
     * @param project_id mã project
     * @return Task nếu tìm thấy, null nếu không tìm thấy
     */
    Task getById(String task_id, String project_id);

    /**
     * Cập nhật toàn bộ thông tin task
     * @param task_id mã task cần cập nhật
     * @param project_id mã project
     * @param title tiêu đề mới
     * @param description mô tả mới
     * @param status trạng thái mới
     * @param priority ưu tiên mới
     * @param due_date hạn chót mới
     * @return true nếu cập nhật thành công, false nếu thất bại
     */
    boolean update(String task_id, String project_id, String title, String description, 
                   String status, String priority, Date due_date);

    /**
     * Cập nhật trạng thái task
     * @param task_id mã task cần cập nhật
     * @param project_id mã project
     * @param status trạng thái mới
     * @return true nếu cập nhật thành công, false nếu thất bại
     */
    boolean updateStatus(String task_id, String project_id, String status);

    /**
     * Cập nhật mức độ ưu tiên task
     * @param task_id mã task cần cập nhật
     * @param project_id mã project
     * @param priority mức độ ưu tiên mới
     * @return true nếu cập nhật thành công, false nếu thất bại
     */
    boolean updatePriority(String task_id, String project_id, String priority);

    /**
     * Cập nhật hạn chót task
     * @param task_id mã task cần cập nhật
     * @param project_id mã project
     * @param due_date hạn chót mới
     * @return true nếu cập nhật thành công, false nếu thất bại
     */
    boolean updateDueDate(String task_id, String project_id, Date due_date);

    /**
     * Xóa task theo task_id
     * @param task_id mã task cần xóa
     * @param project_id mã project
     * @return true nếu xóa thành công, false nếu thất bại
     */
    boolean deleteById(String task_id, String project_id);
}
