package main.java.service;

import main.java.model.TaskAssignees;
import main.java.model.User;

import java.util.List;

public interface TaskAssigneesService {
    /**
     * Thêm một task assigness mới
     * @param taskAsigness đối tượng TaskAsigness cần tạo
     * @return true nếu tạo thành công, false nếu thất bại
     */
    boolean create(TaskAssignees taskAsigness);

    /**
     * Lấy danh sách tất cả các task assigness
     * @return List<TaskAsigness> danh sách task assigness, có thể rỗng
     */
    List<TaskAssignees> getAll();

    /**
     * Lấy task assigness theo task_id và user_id
     * @param task_id mã của task
     * @param user_id mã của user
     * @return TaskAsigness nếu tìm thấy, null nếu không tìm thấy
     */
    TaskAssignees getByTaskIdAndUserId(String task_id, String user_id);

    /**
     * Lấy danh sách tất cả user được assign cho một task
     * @param task_id mã của task
     * @return List<TaskAsigness> danh sách assigness cho task, có thể rỗng
     */
    List<TaskAssignees> getByTaskId(String task_id);

    /**
     * Lấy danh sách tất cả task được assign cho một user
     * @param user_id mã của user
     * @return List<TaskAsigness> danh sách tasks được assign cho user, có thể rỗng
     */
    List<TaskAssignees> getByUserId(String user_id);

    /**
     * Xóa task assigness theo task_id và user_id
     * @param task_id mã của task
     * @param user_id mã của user
     * @return true nếu xóa thành công, false nếu thất bại
     */
    boolean deleteByTaskIdAndUserId(String task_id, String user_id);

    /**
     * Xóa tất cả assigness của một task
     * @param task_id mã của task
     * @return true nếu xóa thành công, false nếu thất bại
     */
    boolean deleteByTaskId(String task_id);

    /**
     * Xóa tất cả assigness của một user
     * @param user_id mã của user
     * @return true nếu xóa thành công, false nếu thất bại
     */
    boolean deleteByUserId(String user_id);
}
