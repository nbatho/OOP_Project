package model;

public class Comments {
    private String comment_id;
    private String task_id;
    private String user_id;
    private String body;

    public Comments() {}

    public Comments(String comment_id, String task_id, String user_id, String body) {
        this.comment_id = comment_id;
        this.task_id = task_id;
        this.user_id = user_id;
        this.body = body;
    }

    //Getter
    public String getComment_id() {
        return comment_id;
    }

    public String getTask_id() {
        return task_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public String getBody() {
        return body;
    }

    //Setter


    public void setComment_id(String comment_id) {
        this.comment_id = comment_id;
    }

    public void setTask_id(String task_id) {
        this.task_id = task_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public void setBody(String body) {
        this.body = body;
    }

    @Override
    public String toString() {
        return "Comments{" +
                "comment_id='" + comment_id + '\'' +
                ", task_id='" + task_id + '\'' +
                ", user_id='" + user_id + '\'' +
                ", body='" + body + '\'' +
                '}';
    }
}
