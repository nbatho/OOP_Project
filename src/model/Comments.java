package model;
import java.time.LocalDateTime;
public class Comments {
    private int id;
    private String content;
    private LocalDateTime createdAt;
    private User user;
    private Task task;

    // ===== Constructor =====
    public Comments(int id, String content, User user, Task task) {
        this.id = id;
        this.content = content;
        this.user = user;
        this.task = task;
//        this.createdAt = LocalDateTime.now();
    }

    // ===== Business Methods =====
    public void addComment(String newContent) {
        this.content = newContent;
//        this.createdAt = LocalDateTime.now();
    }

    public void deleteComment() {
        this.content = "[deleted]";
    }

    // ===== Getter & Setter =====
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

//    public LocalDateTime getCreatedAt() {
//        return createdAt;
//    }

//    public void setCreatedAt(LocalDateTime createdAt) {
//        this.createdAt = createdAt;
//    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    // ===== toString =====
    @Override
    public String toString() {
        return "Comment{" +
                "id=" + id +
                ", content='" + content + '\'' +
                ", createdAt=" + createdAt +
                ", user=" + (user != null ? user.getName() : "null") +
                ", task=" + (task != null ? task.getTitle() : "null") +
                '}';
    }

}
