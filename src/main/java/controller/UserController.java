package main.java.controller;

import java.util.ArrayList;
import java.util.List;
import main.java.model.User;

public class UserController {
    private static final List<User> users = new ArrayList<>();

    // Tạo sẵn tài khoản mẫu
    public static void seedSampleUsers() {
        users.add(new User("1", "admin", "admin@gmail.com", "admin"));
    }

    public static User checkLogin(String identifier, String password) {
        for (User user : users) {
            System.out.println(user);
            if (user.getFullName().equalsIgnoreCase(identifier) && user.getPasswordHash().equals(password)) {
                return user;
            }
        }
        return null;
    }
//
//    public static List<User> getAllUsers() {
//        return users;
//    }
}
