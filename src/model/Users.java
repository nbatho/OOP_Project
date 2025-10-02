package model;

import java.time.LocalDateTime;

public class User {
    private int id;
    private String name;
    private String email;
    private String password;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<Task> assignedTasks;

    public User(int id, String name, String email, String password) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        this.assignedTasks = new ArrayList<>()
    }

    public void addTask(Task task) {
        assignedTasks.add(task);
    }


    public void removeTask(Task task) {
        assignedTasks.remove(task);
    }

    public List<Task> getAssignedTasks() {
        return assignedTasks;
    }
    public boolean authenticate(String password) {
        return this.password.equals(password);
    }

    public void updateUser(String name, String email) {
        this.name = name;
        this.email = email;
        this.updatedAt = LocalDateTime.now();
    }

    public int getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}
