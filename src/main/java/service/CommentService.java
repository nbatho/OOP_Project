package main.java.service;

import main.java.model.Comments;

import java.util.List;

public interface CommentService {
    /**
     * Tạo một comment mới
     * @param comment đối tượng Comments cần tạo
     * @return true nếu tạo thành công, false nếu thất bại
     */
    boolean create(Comments comment);

    /**
     * Lấy danh sách tất cả các comments
     * @return List<Comments> danh sách comments, có thể rỗng
     */
    List<Comments> getAll();

    /**
     * Lấy comment theo comment_id
     * @param comment_id mã của comment cần tìm
     * @return Comments nếu tìm thấy, null nếu không tìm thấy
     */
    Comments getById(String comment_id);

    /**
     * Lấy danh sách tất cả comments của một task
     * @param task_id mã của task
     * @return List<Comments> danh sách comments của task, có thể rỗng
     */
    List<Comments> getByTaskId(String task_id);

    /**
     * Lấy danh sách tất cả comments của một user
     * @param user_id mã của user
     * @return List<Comments> danh sách comments của user, có thể rỗng
     */
    List<Comments> getByUserId(String user_id);

    /**
     * Cập nhật nội dung comment
     * @param comment_id mã comment cần cập nhật
     * @param body nội dung mới
     * @return true nếu cập nhật thành công, false nếu thất bại
     */
    boolean updateBody(String comment_id, String body);

    /**
     * Xóa comment theo comment_id
     * @param comment_id mã comment cần xóa
     * @return true nếu xóa thành công, false nếu thất bại
     */
    boolean deleteById(String comment_id);

    /**
     * Xóa tất cả comments của một task
     * @param task_id mã của task
     * @return true nếu xóa thành công, false nếu thất bại
     */
    boolean deleteByTaskId(String task_id);

    /**
     * Xóa tất cả comments của một user
     * @param user_id mã của user
     * @return true nếu xóa thành công, false nếu thất bại
     */
    boolean deleteByUserId(String user_id);
}
