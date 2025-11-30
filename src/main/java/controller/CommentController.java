package main.java.controller;

import main.java.Utility.Helper;
import main.java.component.TaskCard;
import main.java.model.Comments;
import main.java.model.Task;
import main.java.model.User;
import main.java.service.CommentService;
import main.java.service.UserService;
import main.java.service.impl.CommentServiceImpl;
import main.java.service.impl.UserServiceImpl;
import main.java.view.DashboardView;

import java.util.List;

public class CommentController {
    private final DashboardView view;
    private final TaskCard taskCard;
    private final Task task;
    private final Helper helper;

    private final CommentService commentService;
    private final UserService userService ;
    public CommentController(DashboardView view, TaskCard taskCard, Task task) {
        this.commentService = new CommentServiceImpl();
        this.userService = UserServiceImpl.getInstance();
        this.view = view;
        this.taskCard = taskCard;
        this.task = task;
        this.helper = new Helper();
        initController();
    }
    private void initController() {
        loadComments();
        this.taskCard.getBtnSendComment().addActionListener(e -> handleCreateComment());
    }
    private void loadComments() {
        try {
            List<Comments> comments = commentService.getCommentsByTaskId(this.task.getTaskId());
            taskCard.clearComments();
            for (Comments comment : comments) {
                User user = userService.getUserById(comment.getUserId());
                String userName = user != null ? user.getFullName() : "Unknown User";
                String timestamp = helper.formatTimestamp(comment.getCreatedAt());
                taskCard.addCommentToList(userName, comment.getBody(), timestamp);
            }
        } catch (Exception ex) {
            System.err.println("Không thể load comments: " + ex.getMessage());
        }
    }
    private void handleCreateComment() {
        try {
            String commentText = taskCard.getTxtComment().getText().trim();
            if (!taskCard.validateComment()) return;
            Comments newComment = new Comments(null, task.getTaskId(), userService.getCurrentUser().getUserId(), commentText);
            boolean success = commentService.createComment(newComment);
            if (!success) {
                view.showErrorMessage("Không thể gửi bình luận");
                return;
            }
            String userName = userService.getCurrentUser().getFullName();
            String timestamp = helper.formatTimestamp(new java.sql.Timestamp(System.currentTimeMillis()));
            taskCard.addCommentToList(userName, commentText, timestamp);
            taskCard.getTxtComment().setText("");
            taskCard.showSuccessMessage("Đã gửi bình luận");
            loadComments();
        } catch (Exception ex) {
            taskCard.showErrorMessage("Lỗi khi gửi bình luận: " + ex.getMessage());
        }
    }
}