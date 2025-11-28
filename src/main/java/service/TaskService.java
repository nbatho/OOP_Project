package main.java.service;

import java.sql.Date;
import java.util.List;
import main.java.model.Task;
import main.java.model.User;

public interface TaskService {
    /**
     * Tạo một task mới
     * @param task đối tượng Task cần tạo
     * @return true nếu tạo thành công, false nếu thất bại
     */
    boolean createTask(Task task, List<User> assignees);

    /**
     * Lấy danh sách tất cả các task theo projectId
     * @param projectId mã project
     * @return List<Task> danh sách task, có thể rỗng
     */
    List<Task> getTasksByProjectId(String projectId);

    List<Task> getTasksWithAssignees(String projectId);
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
    boolean updateTask(Task task, List<User> assignees);
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
