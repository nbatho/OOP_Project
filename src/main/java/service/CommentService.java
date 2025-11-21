package main.java.service;

import java.util.List;
import main.java.model.Comments;

public interface CommentService {
    /**
     * Tạo một comment mới
     * @param comment đối tượng Comments cần tạo
     * @return true nếu tạo thành công, false nếu thất bại
     */
    boolean createComment(Comments comment);

    /**
     * Lấy danh sách tất cả các comments
     * @return List<Comments> danh sách comments, có thể rỗng
     */
    List<Comments> getAllComments();

    /**
     * Lấy comment theo commentId
     * @param commentId mã của comment cần tìm
     * @return Comments nếu tìm thấy, null nếu không tìm thấy
     */
    Comments getCommentById(String commentId);

    /**
     * Lấy danh sách tất cả comments của một task
     * @param taskId mã của task
     * @return List<Comments> danh sách comments của task, có thể rỗng
     */
    List<Comments> getCommentsByTaskId(String taskId);

    /**
     * Lấy danh sách tất cả comments của một user
     * @param userId mã của user
     * @return List<Comments> danh sách comments của user, có thể rỗng
     */
    List<Comments> getCommentsByUserId(String userId);

    /**
     * Cập nhật comment
     * @param comment đối tượng comment với thông tin mới
     * @return true nếu cập nhật thành công, false nếu thất bại
     */
    boolean updateComment(Comments comment);

    /**
     * Cập nhật nội dung comment
     * @param commentId mã comment cần cập nhật
     * @param body nội dung mới
     * @return true nếu cập nhật thành công, false nếu thất bại
     */
    boolean updateCommentBody(String commentId, String body);

    /**
     * Xóa comment theo commentId
     * @param commentId mã comment cần xóa
     * @return true nếu xóa thành công, false nếu thất bại
     */
    boolean deleteComment(String commentId);

    /**
     * Xóa tất cả comments của một task
     * @param taskId mã của task
     * @return true nếu xóa thành công, false nếu thất bại
     */
    boolean deleteCommentsByTaskId(String taskId);

    /**
     * Xóa tất cả comments của một user
     * @param userId mã của user
     * @return true nếu xóa thành công, false nếu thất bại
     */
    boolean deleteCommentsByUserId(String userId);

    /**
     * Kiểm tra comment có tồn tại không
     * @param commentId mã comment
     * @return true nếu comment tồn tại
     */
    boolean commentExists(String commentId);

    /**
     * Kiểm tra user có quyền chỉnh sửa comment không
     * @param commentId mã comment
     * @param userId mã user
     * @return true nếu có quyền
     */
    boolean canEditComment(String commentId, String userId);
}
