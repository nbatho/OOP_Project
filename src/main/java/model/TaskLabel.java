package main.java.model;

public class TaskLabel {
    private String taskId;
    private String labelId;

    public TaskLabel() {}

    public TaskLabel(String taskId, String labelId) {
        this.taskId = taskId;
        this.labelId = labelId;
    }

    // Getters
    public String getTaskId() {
        return taskId;
    }

    public String getLabelId() {
        return labelId;
    }

    // Setters
    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public void setLabelId(String labelId) {
        this.labelId = labelId;
    }

    @Override
    public String toString() {
        return "TaskLabel{" +
                "taskId='" + taskId + '\'' +
                ", labelId='" + labelId + '\'' +
                '}';
    }
}
