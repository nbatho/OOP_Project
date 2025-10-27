package model;
import java.time.LocalDateTime;
public class Attachments {
    private int id;                     // Mã định danh tệp đính kèm
    private String filePath;            // Đường dẫn tới file
    private LocalDateTime uploadedAt;   // Thời gian upload
    private User uploader;              // Người tải file lên
    private Task task;                  // Nhiệm vụ liên quan

    // ===== Constructor =====
    public Attachments(int id, String filePath, User uploader, Task task) {
        this.id = id;
        this.filePath = filePath;
        this.uploader = uploader;
        this.task = task;
        this.uploadedAt = LocalDateTime.now();
    }

    // ===== Business Methods =====
    public void uploadFile(String newPath) {
        this.filePath = newPath;
        this.uploadedAt = LocalDateTime.now();
    }

    public void deleteFile() {
        this.filePath = "[deleted]";
    }

    // ===== Getter & Setter =====
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public LocalDateTime getUploadedAt() {
        return uploadedAt;
    }

    public void setUploadedAt(LocalDateTime uploadedAt) {
        this.uploadedAt = uploadedAt;
    }

    public User getUploader() {
        return uploader;
    }

    public void setUploader(User uploader) {
        this.uploader = uploader;
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
        return "Attachment{" +
                "id=" + id +
                ", filePath='" + filePath + '\'' +
                ", uploadedAt=" + uploadedAt +
                ", uploader=" + (uploader != null ? uploader.getUsername() : "null") +
                ", task=" + (task != null ? task.getTitle() : "null") +
                '}';
    }
}
