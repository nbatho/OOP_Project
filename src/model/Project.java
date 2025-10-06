package model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Project {
    private int id;
    private String name;
    private String description;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private User owner;                // Chủ dự án
    private List<Task> tasks;          // Danh sách công việc

    public Project(int id, String name, String description, User owner) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.owner = owner;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        this.tasks = new ArrayList<>();
    }

    // Cập nhật thông tin dự án
    public void updateProject(String name, String description) {
        this.name = name;
        this.description = description;
        this.updatedAt = LocalDateTime.now();
    }

    // Thêm công việc
    public void addTask(Task task) {
        tasks.add(task);
    }

    // Xóa công việc
    public void removeTask(Task task) {
        tasks.remove(task);
    }

    // Lấy danh sách task
    public List<Task> getTasks() {
        return tasks;
    }

    // Getter
    public int getId() { return id; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public User getOwner() { return owner; }
}
