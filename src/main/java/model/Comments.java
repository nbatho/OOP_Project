package main.java.model;

import java.sql.Timestamp;

public class Comments {
    private String commentId;
    private String taskId;
    private String userId;
    private String body;
    private Timestamp createdAt;

    public Comments() {}

    public Comments(String commentId, String taskId, String userId, String body) {
        this.commentId = commentId;
        this.taskId = taskId;
        this.userId = userId;
        this.body = body;
        this.createdAt = new Timestamp(System.currentTimeMillis());
    }

    public Comments(String commentId, String taskId, String userId, String body, Timestamp createdAt) {
        this.commentId = commentId;
        this.taskId = taskId;
        this.userId = userId;
        this.body = body;
        this.createdAt = createdAt;
    }

    // Getters
    public String getCommentId() {
        return commentId;
    }

    public String getTaskId() {
        return taskId;
    }

    public String getUserId() {
        return userId;
    }

    public String getBody() {
        return body;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    // Setters
    public void setCommentId(String commentId) {
        this.commentId = commentId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "Comments{" +
                "commentId='" + commentId + '\'' +
                ", taskId='" + taskId + '\'' +
                ", userId='" + userId + '\'' +
                ", body='" + body + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}
