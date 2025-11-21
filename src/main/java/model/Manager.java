package main.java.model;

public class Manager extends User {
    public Manager() {
        super();
    }

    public Manager(String userId, String fullName, String email, String passwordHash) {
        super(userId, fullName, email, passwordHash);
    }

    @Override
    public String toString() {
        return "Manager{" +
                "userId='" + getUserId() + '\'' +
                ", fullName='" + getFullName() + '\'' +
                ", email='" + getEmail() + '\'' +
                ", passwordHash='" + getPasswordHash() + '\'' +
                ", createdAt=" + getCreatedAt() +
                '}';
    }
}
