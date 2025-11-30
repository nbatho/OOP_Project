package main.java.model;

public class Admin extends User {

    public Admin() {
        super();
    }

    public Admin(String userId, String fullName, String email, String passwordHash) {
        super(userId, fullName, email, passwordHash);
    }

    @Override
    public String getPasswordHash() {
        return super.getPasswordHash();
    }

    @Override
    public String toString() {
        return "Admin{" +
                "userId='" + getUserId() + '\'' +
                ", fullName='" + getFullName() + '\'' +
                ", email='" + getEmail() + '\'' +
                ", passwordHash='" + getPasswordHash() + '\'' +
                ", createdAt=" + getCreatedAt() +
                '}';
    }
}
