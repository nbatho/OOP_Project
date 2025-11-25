package main.java.model;

public class TaskAssignees {
    private String taskId;
    private String userId;

    public TaskAssignees() {}

    public TaskAssignees(String taskId, String userId) {
        this.taskId = taskId;
        this.userId = userId;
    }

    // Getters
    public String getTaskId() {
        return taskId;
    }

    public String getUserId() {
        return userId;
    }

    // Setters
    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "TaskAsigness{" +
                "taskId='" + taskId + '\'' +
                ", userId='" + userId + '\'' +
                '}';
    }
}
