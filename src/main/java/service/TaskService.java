package main.java.service;

import java.sql.Date;
import java.util.List;
import main.java.model.Task;

public interface TaskService {
    /**
     * Tạo một task mới
     * @param task đối tượng Task cần tạo
     * @return true nếu tạo thành công, false nếu thất bại
     */
    boolean createTask(Task task, String userId);

    /**
     * Lấy danh sách tất cả các task theo projectId
     * @param projectId mã project
     * @return List<Task> danh sách task, có thể rỗng
     */
    List<Task> getTasksByProjectId(String projectId);

    /**
     * Lấy task theo taskId
     * @param taskId mã của task cần tìm
     * @param projectId mã project
     * @return Task nếu tìm thấy, null nếu không tìm thấy
     */
    Task getTaskById(String taskId, String projectId);

    /**
     * Cập nhật thông tin task
     * @param task đối tượng Task với thông tin mới
     * @return true nếu cập nhật thành công, false nếu thất bại
     */
    boolean updateTask(Task task);

    /**
     * Cập nhật trạng thái task
     * @param taskId mã task cần cập nhật
     * @param projectId mã project
     * @param status trạng thái mới (TODO, IN_PROGRESS, DONE)
     * @return true nếu cập nhật thành công, false nếu thất bại
     */
    boolean updateTaskStatus(String taskId, String projectId, String status);

    /**
     * Cập nhật mức độ ưu tiên task
     * @param taskId mã task cần cập nhật
     * @param projectId mã project
     * @param priority mức độ ưu tiên mới (HIGH, MEDIUM, LOW)
     * @return true nếu cập nhật thành công, false nếu thất bại
     */
    boolean updateTaskPriority(String taskId, String projectId, String priority);

    /**
     * Cập nhật hạn chót task
     * @param taskId mã task cần cập nhật
     * @param projectId mã project
     * @param dueDate hạn chót mới
     * @return true nếu cập nhật thành công, false nếu thất bại
     */
    boolean updateTaskDueDate(String taskId, String projectId, Date dueDate);

    /**
     * Xóa task theo taskId
     * @param taskId mã task cần xóa
     * @param projectId mã project
     * @return true nếu xóa thành công, false nếu thất bại
     */
    boolean deleteTask(String taskId, String projectId);

    /**
     * Kiểm tra task có tồn tại không
     * @param taskId mã task
     * @param projectId mã project
     * @return true nếu task tồn tại
     */
    boolean taskExists(String taskId, String projectId);

    /**
     * Validate status của task
     * @param status trạng thái cần validate
     * @return true nếu status hợp lệ
     */
    boolean isValidStatus(String status);

    /**
     * Validate priority của task
     * @param priority mức độ ưu tiên cần validate
     * @return true nếu priority hợp lệ
     */
    boolean isValidPriority(String priority);
}
