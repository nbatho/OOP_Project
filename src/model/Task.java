package model;


import java.util.Date;

public class Task {
    private String task_id;
    private String project_id;
    private String title;
    private String description;
    private String status;
    private String priority;
    private Date due_date;
    private String created_by;

    public Task() {}
    public Task(String task_id, String project_id, String title, String description, String status, String priority, Date due_date, String created_by) {
        this.task_id = task_id;
        this.project_id = project_id;
        this.title = title;
        this.description = description;
        this.status = status;
        this.priority = priority;
        this.due_date = due_date;
        this.created_by = created_by;
    }

    //Getter

    public String getTask_id() {
        return task_id;
    }

    public String getProject_id() {
        return project_id;
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

    public Date getDue_date() {
        return due_date;
    }

    public String getCreated_by() {
        return created_by;
    }

    //Setter

    public void setTask_id(String task_id) {
        this.task_id = task_id;
    }

    public void setProject_id(String project_id) {
        this.project_id = project_id;
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

    public void setDue_date(Date due_date) {
        this.due_date = due_date;
    }

    public void setCreated_by(String created_by) {
        this.created_by = created_by;
    }

    @Override
    public String toString() {
        return "Task{" +
                "task_id='" + task_id + '\'' +
                ", project_id='" + project_id + '\'' +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", status='" + status + '\'' +
                ", priority='" + priority + '\'' +
                ", due_date=" + due_date +
                ", created_by='" + created_by + '\'' +
                '}';
    }
}
