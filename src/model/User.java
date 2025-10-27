package model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class User {
    private String id;
    private String username;
    private String email;
    private String password;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<Task> assignedTasks;

    public User(String id, String username, String email, String password) {
        this.id = id;
        setUsername(username);
        setEmail(email);
        setPassword(password);
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        this.assignedTasks = new ArrayList<>();
    }

    // ================== HÀNH VI CỦA USER ==================
    public void addTask(Task task) {
        assignedTasks.add(task);
    }

    public void removeTask(Task task) {
        assignedTasks.remove(task);
    }

    public boolean authenticate(String password) {
        return this.password.equals(password);
    }

    public void updateUser(String username, String email) {
        setUsername(username);
        setEmail(email);
        this.updatedAt = LocalDateTime.now();
    }

    // ================== GETTER & SETTER ==================
    public String getId() {
        return id;
    }

    public void setId(String id) {
        if (id.isEmpty()) this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        if (username != null && !username.isEmpty()) {
            this.username = username;
        }
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        if (email != null && email.contains("@")) {
            this.email = email;
        }
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        if (password != null && password.length() >= 4) {
            this.password = password;
        }
    }

    public List<Task> getAssignedTasks() {
        return assignedTasks;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
