package model;

import java.time.LocalDateTime;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Task {
    private int id;
    private String title;
    private String description;
    private String status;
    private String priority;
    private LocalDate deadline;
    private User createdBy;
    private User assignee;
    private Project project;
//    private List<Label> labels;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    // ===== Constructor =====
    public Task(int id, String title, String description, String priority,
                LocalDate deadline, User createdBy, Project project) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.status = "To Do";  // Mặc định khi tạo
        this.priority = priority;
        this.deadline = deadline;
        this.createdBy = createdBy;
        this.project = project;
//        this.labels = new ArrayList<>();
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    // ===== Business Methods =====

    // Cập nhật thông tin nhiệm vụ
    public void updateTask(String title, String description, String priority, LocalDate deadline) {
        this.title = title;
        this.description = description;
        this.priority = priority;
        this.deadline = deadline;
        this.updatedAt = LocalDateTime.now();
    }

    // Thay đổi trạng thái nhiệm vụ
    public void changeStatus(String newStatus) {
        this.status = newStatus;
        this.updatedAt = LocalDateTime.now();
    }

    // Gán người phụ trách
    public void assignUser(User user) {
        this.assignee = user;
        this.updatedAt = LocalDateTime.now();
    }

    // Thêm nhãn cho nhiệm vụ
//    public void addLabel(Label label) {
//        if (label != null && !labels.contains(label)) {
//            labels.add(label);
//            this.updatedAt = LocalDateTime.now();
//        }
//    }
//
//    // Xóa nhãn khỏi nhiệm vụ
//    public void removeLabel(Label label) {
//        if (labels.remove(label)) {
//            this.updatedAt = LocalDateTime.now();
//        }
//    }

    // ===== Getter & Setter =====
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
        this.updatedAt = LocalDateTime.now();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
        this.updatedAt = LocalDateTime.now();
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
        this.updatedAt = LocalDateTime.now();
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
        this.updatedAt = LocalDateTime.now();
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
        this.updatedAt = LocalDateTime.now();
    }

    public LocalDate getDeadline() {
        return deadline;
    }

    public void setDeadline(LocalDate deadline) {
        this.deadline = deadline;
        this.updatedAt = LocalDateTime.now();
    }

    public User getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(User createdBy) {
        this.createdBy = createdBy;
    }

    public User getAssignee() {
        return assignee;
    }

    public void setAssignee(User assignee) {
        this.assignee = assignee;
        this.updatedAt = LocalDateTime.now();
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
        this.updatedAt = LocalDateTime.now();
    }

//    public List<Label> getLabels() {
//        return labels;
//    }
//
//    public void setLabels(List<Label> labels) {
//        this.labels = labels;
//        this.updatedAt = LocalDateTime.now();
//    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    // ===== toString =====
    @Override
    public String toString() {
        return "Task{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", status='" + status + '\'' +
                ", priority='" + priority + '\'' +
                ", deadline=" + deadline +
                ", assignee=" + (assignee != null ? assignee.getUsername() : "null") +
                ", project=" + (project != null ? project.getName() : "null") +
                '}';
    }
}
