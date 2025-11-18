package main.java.model;

public class Manager extends User {
    public Manager() {
        super();
    }

    public Manager(String user_id, String full_name, String email, String password) {
        super(user_id, full_name, email, password, "Manager");
    }

    @Override
    public String toString() {
        return "Manager{" +
                "user_id='" + getUser_id() + '\'' +
                ", full_name='" + getFull_name() + '\'' +
                ", email='" + getEmail() + '\'' +
                ", password='" + getPassword() + '\'' +
                ", role='" + getRole() + '\'' +
                '}';
    }
}
