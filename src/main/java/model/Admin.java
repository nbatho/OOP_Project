package main.java.model;

public class Admin extends User{

    public Admin() {
        super();
    }

    public Admin(String user_id, String full_name, String email, String password) {
        super(user_id, full_name, email, password, "Admin");
    }

    @Override
    public String toString() {
        return "Admin{" +
                "user_id='" + getUser_id() + '\'' +
                ", full_name='" + getFull_name() + '\'' +
                ", email='" + getEmail() + '\'' +
                ", password='" + getPassword() + '\'' +
                ", role='" + getRole() + '\'' +
                '}';
    }
}
