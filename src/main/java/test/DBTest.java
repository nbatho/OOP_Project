package main.java.test;


import java.sql.Connection;
import java.sql.SQLException;

import static main.java.config.DatabaseConnection.getConnection;

public class DBTest {
    public static void main(String[] args) {

        try (Connection conn = getConnection()) {
            if (conn != null && !conn.isClosed()) {
                System.out.println(" Kết nối MySQL thành công!");
            } else {
                System.out.println(" Kết nối MySQL thất bại!");
            }
        } catch (SQLException e) {
            System.out.println(" Lỗi kết nối MySQL: " + e.getMessage());
        }
    }
}
