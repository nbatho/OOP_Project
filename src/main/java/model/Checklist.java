package main.java.model;

public class Checklist {
    private String checklistId;
    private String taskId;
    private String title;

    public Checklist() {}

    public Checklist(String checklistId, String taskId, String title) {
        this.checklistId = checklistId;
        this.taskId = taskId;
        this.title = title;
    }

    // Getters
    public String getChecklistId() {
        return checklistId;
    }

    public String getTaskId() {
        return taskId;
    }

    public String getTitle() {
        return title;
    }

    // Setters
    public void setChecklistId(String checklistId) {
        this.checklistId = checklistId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return "Checklist{" +
                "checklistId='" + checklistId + '\'' +
                ", taskId='" + taskId + '\'' +
                ", title='" + title + '\'' +
                '}';
    }
}
