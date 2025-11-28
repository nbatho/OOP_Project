package main.java.test;
import main.java.config.DatabaseConnection;

public class DBTest {
    public static void main(String[] args) {
        System.out.println("=== Testing Database Connection ===");
        DatabaseConnection.testConnection();
    }

}
