package model;

import java.time.LocalDateTime;

public class Task {
    private int id;
    private String title;
    private String description;
    private TaskStatus status;
    private TaskPriority priority;
    private LocalDateTime dueDate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private User assignee;

    public Task(int id, String title, String description, TaskPriority priority, LocalDateTime dueDate) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.priority = priority;
        this.dueDate = dueDate;
        this.status = TaskStatus.TODO;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    // Cập nhật thông tin công việc
    public void updateTask(String title, String description, TaskPriority priority, LocalDateTime dueDate) {
        this.title = title;
        this.description = description;
        this.priority = priority;
        this.dueDate = dueDate;
        this.updatedAt = LocalDateTime.now();
    }

    // Thay đổi trạng thái
    public void changeStatus(TaskStatus newStatus) {
        this.status = newStatus;
        this.updatedAt = LocalDateTime.now();
    }

    // Gán người thực hiện
    public void assignUser(User user) {
        this.assignee = user;
        this.updatedAt = LocalDateTime.now();
    }

    // Thêm comment (tạm thời chỉ in ra console)
    public void addComment(String comment, User author) {
        System.out.println(author.getName() + " commented: " + comment);
    }

    // Thêm file đính kèm (chỉ demo)
    public void addAttachment(String fileName) {
        System.out.println("Attachment added: " + fileName);
    }

    // Getter
    public int getId() { return id; }
    public String getTitle() { return title; }
    public TaskStatus getStatus() { return status; }
    public TaskPriority getPriority() { return priority; }
    public User getAssignee() { return assignee; }
    public LocalDateTime getDueDate() { return dueDate; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
}
