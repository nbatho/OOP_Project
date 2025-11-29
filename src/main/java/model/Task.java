package main.java.model;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;

public class Task {
    private String taskId;
    private String projectId;
    private String title;
    private String description;
    private String status;
    private String priority;
    private Date  startDate;
    private Date dueDate;
    private String createdBy;
    private Timestamp createdAt;
    private List<User> assignedUsers;
    public Task() {}

    public Task(String taskId, String projectId, String title, String description, 
                String status, String priority,Date startDate ,Date dueDate, String createdBy) {
        this.taskId = taskId;
        this.projectId = projectId;
        this.title = title;
        this.description = description;
        this.status = status;
        this.priority = priority;
        this.startDate = startDate;
        this.dueDate = dueDate;
        this.createdBy = createdBy;
        this.createdAt = new Timestamp(System.currentTimeMillis());
    }
    // Getters
    public String getTaskId() {
        return taskId;
    }

    public String getProjectId() {
        return projectId;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getStatus() {
        return status;
    }

    public String getPriority() {
        return priority;
    }

    public Date getStartDate() {return startDate;}


    public Date getDueDate() {
        return dueDate;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public List<User> getAssignedUsers() {
        return assignedUsers;
    }


    // Setters
    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public void setStartDate(Date startDate) {this.startDate = startDate;}
    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public void setAssignedUsers(List<User> assignedUsers) {
        this.assignedUsers = assignedUsers;
    }
    @Override
    public String toString() {
        return "Task{" +
                "taskId='" + taskId + '\'' +
                ", projectId='" + projectId + '\'' +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", status='" + status + '\'' +
                ", priority='" + priority + '\'' +
                ", dueDate=" + dueDate +
                ", createdBy='" + createdBy + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}
