package main.java.service.impl;

import main.java.model.Comments;
import main.java.repository.CommentRepository;
import main.java.service.CommentService;

import java.util.List;
import java.util.UUID;

public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;

    public CommentServiceImpl() {
        this.commentRepository = new CommentRepository();
    }

    @Override
    public boolean createComment(Comments comment) {
        try {

            if (comment == null) {
                System.out.println("Comment không được null");
                return false;
            }

            // Validate thông tin comment
            if (!isValidCommentData(comment)) {
                return false;
            }

            // Tạo ID mới nếu chưa có
            if (comment.getCommentId() == null || comment.getCommentId().trim().isEmpty()) {
                comment.setCommentId(UUID.randomUUID().toString());
            }

            // Kiểm tra comment đã tồn tại chưa
            if (commentRepository.findByCommentId(comment.getCommentId()) != null) {
                System.out.println("Comment ID đã tồn tại: " + comment.getCommentId());
                return false;
            }

            // Set created time nếu chưa có
            if (comment.getCreatedAt() == null) {
                comment.setCreatedAt(new java.sql.Timestamp(System.currentTimeMillis()));
            }

            return commentRepository.createComment(comment);
        } catch (Exception e) {
            System.out.println("Lỗi khi tạo comment: " + e.getMessage());
            return false;
        }
    }


    @Override
    public Comments getCommentById(String commentId) {
        try {
            if (commentId == null || commentId.trim().isEmpty()) {
                System.out.println("Comment ID không được null hoặc rỗng");
                return null;
            }

            return commentRepository.findByCommentId(commentId);
        } catch (Exception e) {
            System.out.println("Lỗi khi lấy comment theo ID: " + e.getMessage());
            return null;
        }
    }

    @Override
    public List<Comments> getCommentsByTaskId(String taskId) {
        try {
            if (taskId == null || taskId.trim().isEmpty()) {
                System.out.println("Task ID không được null hoặc rỗng");
                return List.of();
            }

            return commentRepository.getAllComments().stream()
                    .filter(comment -> taskId.equals(comment.getTaskId()))
                    .toList();
        } catch (Exception e) {
            System.out.println("Lỗi khi lấy comments theo task ID: " + e.getMessage());
            return List.of();
        }
    }

    @Override
    public List<Comments> getCommentsByUserId(String userId) {
        try {
            if (userId == null || userId.trim().isEmpty()) {
                System.out.println("User ID không được null hoặc rỗng");
                return List.of();
            }

            return commentRepository.getAllComments().stream()
                    .filter(comment -> userId.equals(comment.getUserId()))
                    .toList();
        } catch (Exception e) {
            System.out.println("Lỗi khi lấy comments theo user ID: " + e.getMessage());
            return List.of();
        }
    }

    @Override
    public boolean updateComment(Comments comment) {
        try {
            if (comment == null) {
                System.out.println("Comment không được null");
                return false;
            }

            if (!isValidCommentData(comment)) {
                return false;
            }

            // Kiểm tra comment có tồn tại không
            if (!commentExists(comment.getCommentId())) {
                System.out.println("Comment không tồn tại");
                return false;
            }

            return commentRepository.updateComment(comment);
        } catch (Exception e) {
            System.out.println("Lỗi khi cập nhật comment: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean updateCommentBody(String commentId, String body) {
        try {
            if (commentId == null || commentId.trim().isEmpty()) {
                System.out.println("Comment ID không được null hoặc rỗng");
                return false;
            }

            if (body == null || body.trim().isEmpty()) {
                System.out.println("Nội dung comment không được null hoặc rỗng");
                return false;
            }

            if (body.length() > 1000) {
                System.out.println("Nội dung comment quá dài (tối đa 1000 ký tự)");
                return false;
            }

            Comments comment = getCommentById(commentId);
            if (comment == null) {
                System.out.println("Comment không tồn tại");
                return false;
            }

            return commentRepository.updateCommentBody(commentId, body.trim());
        } catch (Exception e) {
            System.out.println("Lỗi khi cập nhật nội dung comment: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean deleteComment(String commentId) {
        try {
            if (commentId == null || commentId.trim().isEmpty()) {
                System.out.println("Comment ID không được null hoặc rỗng");
                return false;
            }

            if (!commentExists(commentId)) {
                System.out.println("Comment không tồn tại");
                return false;
            }

            return commentRepository.deleteByCommentId(commentId);
        } catch (Exception e) {
            System.out.println("Lỗi khi xóa comment: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean deleteCommentsByTaskId(String taskId) {
        try {
            if (taskId == null || taskId.trim().isEmpty()) {
                System.out.println("Task ID không được null hoặc rỗng");
                return false;
            }

            List<Comments> comments = getCommentsByTaskId(taskId);
            boolean allDeleted = true;

            for (Comments comment : comments) {
                if (!commentRepository.deleteByCommentId(comment.getCommentId())) {
                    allDeleted = false;
                    System.out.println("Lỗi khi xóa comment: " + comment.getCommentId());
                }
            }

            return allDeleted;
        } catch (Exception e) {
            System.out.println("Lỗi khi xóa comments theo task ID: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean deleteCommentsByUserId(String userId) {
        try {
            if (userId == null || userId.trim().isEmpty()) {
                System.out.println("User ID không được null hoặc rỗng");
                return false;
            }

            List<Comments> comments = getCommentsByUserId(userId);
            boolean allDeleted = true;

            for (Comments comment : comments) {
                if (!commentRepository.deleteByCommentId(comment.getCommentId())) {
                    allDeleted = false;
                    System.out.println("Lỗi khi xóa comment: " + comment.getCommentId());
                }
            }

            return allDeleted;
        } catch (Exception e) {
            System.out.println("Lỗi khi xóa comments theo user ID: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean commentExists(String commentId) {
        try {
            Comments comment = getCommentById(commentId);
            return comment != null;
        } catch (Exception e) {
            System.out.println("Lỗi khi kiểm tra comment tồn tại: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean canEditComment(String commentId, String userId) {
        try {
            if (commentId == null || commentId.trim().isEmpty() || 
                userId == null || userId.trim().isEmpty()) {
                return false;
            }

            Comments comment = getCommentById(commentId);
            if (comment == null) {
                return false;
            }

            // Chỉ người tạo comment mới có quyền chỉnh sửa
            return userId.equals(comment.getUserId());
        } catch (Exception e) {
            System.out.println("Lỗi khi kiểm tra quyền chỉnh sửa comment: " + e.getMessage());
            return false;
        }
    }

    /**
     * Validate dữ liệu comment
     */
    private boolean isValidCommentData(Comments comment) {
        if (comment.getBody() == null || comment.getBody().trim().isEmpty()) {
            System.out.println("Nội dung comment không được null hoặc rỗng");
            return false;
        }

        if (comment.getBody().length() > 1000) {
            System.out.println("Nội dung comment quá dài (tối đa 1000 ký tự)");
            return false;
        }

        if (comment.getTaskId() == null || comment.getTaskId().trim().isEmpty()) {
            System.out.println("Task ID không được null hoặc rỗng");
            return false;
        }

        if (comment.getUserId() == null || comment.getUserId().trim().isEmpty()) {
            System.out.println("User ID không được null hoặc rỗng");
            return false;
        }

        return true;
    }
}
