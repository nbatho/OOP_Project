package main.java.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import main.java.config.DatabaseConnection;
import main.java.model.Comments;

public class CommentRepository {
    private static final String INSERT_COMMENT =
            "INSERT INTO comments(comment_id, task_id, user_id, body) VALUES (?, ?, ?, ?)";
    private static final String SELECT_ALL_COMMENTS =
            "SELECT comment_id, task_id, user_id, body FROM comments";
    private static final String SELECT_COMMENT_BY_ID =
            "SELECT comment_id, task_id, user_id, body FROM comments WHERE comment_id = ?";
    private static final String SELECT_COMMENTS_BY_TASK_ID =
            "SELECT comment_id, task_id, user_id, body FROM comments WHERE task_id = ?";
    private static final String SELECT_COMMENTS_BY_USER_ID =
            "SELECT comment_id, task_id, user_id, body FROM comments WHERE user_id = ?";
    private static final String UPDATE_COMMENT_BODY =
            "UPDATE comments SET body = ? WHERE comment_id = ?";
    private static final String DELETE_COMMENT =
            "DELETE FROM comments WHERE comment_id = ?";
    private static final String DELETE_COMMENTS_BY_TASK_ID =
            "DELETE FROM comments WHERE task_id = ?";
    private static final String DELETE_COMMENTS_BY_USER_ID =
            "DELETE FROM comments WHERE user_id = ?";

    /**
     * Thêm một comment mới vào database
     * @param comment đối tượng Comments cần tạo
     * @return true nếu tạo thành công, false nếu thất bại
     */
    public boolean createComment(Comments comment) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(INSERT_COMMENT);

            stmt.setString(1, comment.getCommentId());
            stmt.setString(2, comment.getTaskId());
            stmt.setString(3, comment.getUserId());
            stmt.setString(4, comment.getBody());

            int rows = stmt.executeUpdate();
            if (rows > 0) {
                System.out.println("Comment created: " + comment.getCommentId());
                return true;
            }

        } catch (SQLException e) {
            System.err.println("Lỗi khi tạo comment: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Lấy danh sách tất cả các comments
     * @return List<Comments> danh sách comments, có thể rỗng nếu không có comment nào
     */
    public List<Comments> findAll() {
        List<Comments> comments = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(SELECT_ALL_COMMENTS);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                comments.add(mapResultSetToComment(rs));
            }
            System.out.println("Found " + comments.size() + " comments");
        } catch (SQLException e) {
            System.err.println("Lỗi khi lấy danh sách comments: " + e.getMessage());
            e.printStackTrace();
        }

        return comments;
    }

    /**
     * Lấy comment theo commentId
     * @param commentId mã của comment cần tìm
     * @return Comments nếu tìm thấy, null nếu không tìm thấy
     */
    public Comments findByCommentId(String commentId) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(SELECT_COMMENT_BY_ID);
            stmt.setString(1, commentId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                System.out.println("Comment found: " + commentId);
                return mapResultSetToComment(rs);
            } else {
                System.out.println("Comment not found: " + commentId);
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi tìm comment by id: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Lấy danh sách tất cả comments của một task
     * @param taskId mã của task
     * @return List<Comments> danh sách comments của task, có thể rỗng
     */
    public List<Comments> findByTaskId(String taskId) {
        List<Comments> comments = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(SELECT_COMMENTS_BY_TASK_ID);
            stmt.setString(1, taskId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                comments.add(mapResultSetToComment(rs));
            }
            System.out.println("Found " + comments.size() + " comments for task " + taskId);
        } catch (SQLException e) {
            System.err.println("Lỗi khi lấy comments by task_id: " + e.getMessage());
            e.printStackTrace();
        }

        return comments;
    }

    /**
     * Lấy danh sách tất cả comments của một user
     * @param userId mã của user
     * @return List<Comments> danh sách comments của user, có thể rỗng
     */
    public List<Comments> findByUserId(String userId) {
        List<Comments> comments = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(SELECT_COMMENTS_BY_USER_ID);
            stmt.setString(1, userId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                comments.add(mapResultSetToComment(rs));
            }
            System.out.println("Found " + comments.size() + " comments by user " + userId);
        } catch (SQLException e) {
            System.err.println("Lỗi khi lấy comments by user_id: " + e.getMessage());
            e.printStackTrace();
        }

        return comments;
    }

    /**
     * Cập nhật nội dung comment
     * @param commentId mã comment cần cập nhật
     * @param body nội dung mới
     * @return true nếu cập nhật thành công, false nếu thất bại
     */
    public boolean updateCommentBody(String commentId, String body) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(UPDATE_COMMENT_BODY);
            stmt.setString(1, body);
            stmt.setString(2, commentId);

            int rows = stmt.executeUpdate();
            if (rows > 0) {
                System.out.println("Comment updated: " + commentId);
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi update comment: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Xóa comment theo commentId
     * @param commentId mã comment cần xóa
     * @return true nếu xóa thành công, false nếu thất bại
     */
    public boolean deleteByCommentId(String commentId) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(DELETE_COMMENT);
            stmt.setString(1, commentId);
            int rows = stmt.executeUpdate();
            if (rows > 0) {
                System.out.println("Comment deleted: " + commentId);
                return true;
            } else {
                System.out.println("Comment not found: " + commentId);
            }

        } catch (SQLException e) {
            System.err.println("Lỗi khi xóa comment: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Xóa tất cả comments của một task
     * @param taskId mã của task
     * @return true nếu xóa thành công, false nếu thất bại
     */
    public boolean deleteByTaskId(String taskId) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(DELETE_COMMENTS_BY_TASK_ID);
            stmt.setString(1, taskId);
            int rows = stmt.executeUpdate();
            if (rows > 0) {
                System.out.println("All comments for task " + taskId + " deleted");
                return true;
            } else {
                System.out.println("No comments found for task " + taskId);
            }

        } catch (SQLException e) {
            System.err.println("Lỗi khi xóa comments by task_id: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Xóa tất cả comments của một user
     * @param userId mã của user
     * @return true nếu xóa thành công, false nếu thất bại
     */
    public boolean deleteByUserId(String userId) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(DELETE_COMMENTS_BY_USER_ID);
            stmt.setString(1, userId);
            int rows = stmt.executeUpdate();
            if (rows > 0) {
                System.out.println("All comments by user " + userId + " deleted");
                return true;
            } else {
                System.out.println("No comments found by user " + userId);
            }

        } catch (SQLException e) {
            System.err.println("Lỗi khi xóa comments by user_id: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Map dữ liệu ResultSet sang đối tượng Comments
     * @param rs ResultSet chứa dữ liệu comment
     * @return đối tượng Comments
     * @throws SQLException nếu lỗi đọc dữ liệu
     */
    public Comments mapResultSetToComment(ResultSet rs) throws SQLException {
        Comments comment = new Comments();
        comment.setCommentId(rs.getString("comment_id"));
        comment.setTaskId(rs.getString("task_id"));
        comment.setUserId(rs.getString("user_id"));
        comment.setBody(rs.getString("body"));
        return comment;
    }

    /**
     * Lấy tất cả comments
     * @return List<Comments> danh sách comments
     */
    public List<Comments> getAllComments() {
        List<Comments> comments = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(SELECT_ALL_COMMENTS);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Comments comment = mapResultSetToComment(rs);
                comments.add(comment);
            }

            return comments;
        } catch (SQLException e) {
            System.err.println("Lỗi khi lấy tất cả comments: " + e.getMessage());
            e.printStackTrace();
        }
        return comments;
    }

    /**
     * Cập nhật comment
     * @param comment đối tượng Comments cần cập nhật
     * @return true nếu thành công, false nếu thất bại
     */
    public boolean updateComment(Comments comment) {
        return updateCommentBody(comment.getCommentId(), comment.getBody());
    }

}
