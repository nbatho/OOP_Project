package main.java.repository;

import main.java.config.DatabaseConnection;
import main.java.model.Comments;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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

            stmt.setString(1, comment.getComment_id());
            stmt.setString(2, comment.getTask_id());
            stmt.setString(3, comment.getUser_id());
            stmt.setString(4, comment.getBody());

            int rows = stmt.executeUpdate();
            if (rows > 0) {
                System.out.println("Comment created: " + comment.getComment_id());
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
     * Lấy comment theo comment_id
     * @param comment_id mã của comment cần tìm
     * @return Comments nếu tìm thấy, null nếu không tìm thấy
     */
    public Comments findByCommentId(String comment_id) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(SELECT_COMMENT_BY_ID);
            stmt.setString(1, comment_id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                System.out.println("Comment found: " + comment_id);
                return mapResultSetToComment(rs);
            } else {
                System.out.println("Comment not found: " + comment_id);
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi tìm comment by id: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Lấy danh sách tất cả comments của một task
     * @param task_id mã của task
     * @return List<Comments> danh sách comments của task, có thể rỗng
     */
    public List<Comments> findByTaskId(String task_id) {
        List<Comments> comments = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(SELECT_COMMENTS_BY_TASK_ID);
            stmt.setString(1, task_id);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                comments.add(mapResultSetToComment(rs));
            }
            System.out.println("Found " + comments.size() + " comments for task " + task_id);
        } catch (SQLException e) {
            System.err.println("Lỗi khi lấy comments by task_id: " + e.getMessage());
            e.printStackTrace();
        }

        return comments;
    }

    /**
     * Lấy danh sách tất cả comments của một user
     * @param user_id mã của user
     * @return List<Comments> danh sách comments của user, có thể rỗng
     */
    public List<Comments> findByUserId(String user_id) {
        List<Comments> comments = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(SELECT_COMMENTS_BY_USER_ID);
            stmt.setString(1, user_id);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                comments.add(mapResultSetToComment(rs));
            }
            System.out.println("Found " + comments.size() + " comments by user " + user_id);
        } catch (SQLException e) {
            System.err.println("Lỗi khi lấy comments by user_id: " + e.getMessage());
            e.printStackTrace();
        }

        return comments;
    }

    /**
     * Cập nhật nội dung comment
     * @param comment_id mã comment cần cập nhật
     * @param body nội dung mới
     * @return true nếu cập nhật thành công, false nếu thất bại
     */
    public boolean updateCommentBody(String comment_id, String body) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(UPDATE_COMMENT_BODY);
            stmt.setString(1, body);
            stmt.setString(2, comment_id);

            int rows = stmt.executeUpdate();
            if (rows > 0) {
                System.out.println("Comment updated: " + comment_id);
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi update comment: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Xóa comment theo comment_id
     * @param comment_id mã comment cần xóa
     * @return true nếu xóa thành công, false nếu thất bại
     */
    public boolean deleteByCommentId(String comment_id) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(DELETE_COMMENT);
            stmt.setString(1, comment_id);
            int rows = stmt.executeUpdate();
            if (rows > 0) {
                System.out.println("Comment deleted: " + comment_id);
                return true;
            } else {
                System.out.println("Comment not found: " + comment_id);
            }

        } catch (SQLException e) {
            System.err.println("Lỗi khi xóa comment: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Xóa tất cả comments của một task
     * @param task_id mã của task
     * @return true nếu xóa thành công, false nếu thất bại
     */
    public boolean deleteByTaskId(String task_id) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(DELETE_COMMENTS_BY_TASK_ID);
            stmt.setString(1, task_id);
            int rows = stmt.executeUpdate();
            if (rows > 0) {
                System.out.println("All comments for task " + task_id + " deleted");
                return true;
            } else {
                System.out.println("No comments found for task " + task_id);
            }

        } catch (SQLException e) {
            System.err.println("Lỗi khi xóa comments by task_id: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Xóa tất cả comments của một user
     * @param user_id mã của user
     * @return true nếu xóa thành công, false nếu thất bại
     */
    public boolean deleteByUserId(String user_id) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(DELETE_COMMENTS_BY_USER_ID);
            stmt.setString(1, user_id);
            int rows = stmt.executeUpdate();
            if (rows > 0) {
                System.out.println("All comments by user " + user_id + " deleted");
                return true;
            } else {
                System.out.println("No comments found by user " + user_id);
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
        comment.setComment_id(rs.getString("comment_id"));
        comment.setTask_id(rs.getString("task_id"));
        comment.setUser_id(rs.getString("user_id"));
        comment.setBody(rs.getString("body"));
        return comment;
    }

}
