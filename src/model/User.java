package model;
import java.util.*;
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
        setName(name);
        setEmail(email);
        setPassword(password);
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        this.assignedTasks = new ArrayList<>();
    }

    // Function

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
    public static boolean createAccount(String username, String password, String repassword) {
        return true;
    }
    public static boolean checkLogin(String email, String password,boolean remember) {

        return true;
    }

    // Getter && Setter
    public int getId() { return id; }
    public void setId(int id) {
        if (id > 0) {
            this.id = id;
        }
    }

    public String getEmail() { return email; }
    public void setEmail(String email) {
        if (email != null && email.contains("@")) {
            this.email = email;
        }
    }


    public String getName() { return name; }
    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() { return password; }
    public void setPassword(String password) {
        this.password = password;
    }

    public LocalDateTime getCreatedAt() { return createdAt;}
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
