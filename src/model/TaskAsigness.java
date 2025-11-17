package model;

public class TaskAsigness {
    private String user_id;
    private String task_id;

    public TaskAsigness() {}
    public TaskAsigness(String user_id, String task_id) {
        this.user_id = user_id;
        this.task_id = task_id;
    }

    //Getter

    public String getUser_id() {
        return user_id;
    }

    public String getTask_id() {
        return task_id;
    }
    //Setter

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public void setTask_id(String task_id) {
        this.task_id = task_id;
    }

    @Override
    public String toString() {
        return "TaskAsigness{" +
                "user_id='" + user_id + '\'' +
                ", task_id='" + task_id + '\'' +
                '}';
    }
}
