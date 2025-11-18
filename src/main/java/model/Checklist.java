package main.java.model;


public class Checklist {
    private String checklist_id;
    private String task_id;
    private String title;

    public Checklist() {}

    public Checklist(String checklist_id, String task_id, String title) {
        this.checklist_id = checklist_id;
        this.task_id = task_id;
        this.title = title;
    }

    //Getter

    public String getChecklist_id() {
        return checklist_id;
    }

    public String getTask_id() {
        return task_id;
    }

    public String getTitle() {
        return title;
    }

    //Setter

    public void setChecklist_id(String checklist_id) {
        this.checklist_id = checklist_id;
    }

    public void setTask_id(String task_id) {
        this.task_id = task_id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return "Checklist{" +
                "checklist_id='" + checklist_id + '\'' +
                ", task_id='" + task_id + '\'' +
                ", title='" + title + '\'' +
                '}';
    }
}
