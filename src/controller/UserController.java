package controller;

import model.User;
import java.util.ArrayList;
import java.util.List;

public class UserController {
    private static final List<User> users = new ArrayList<>();

    // Tạo sẵn tài khoản mẫu
    public static void seedSampleUsers() {
        users.add(new User("1", "admin", "admin@gmail.com", "admin"));
    }

    // Đăng ký
    public static boolean createAccount(String name, String email, String password) {
        for (User u : users) {
            if (u.getEmail().equalsIgnoreCase(email)) {
                return false;
            }
        }
        int newId = (users.size()) + 1;
        users.add(new User(String.valueOf(newId), name, email, password));
        return true;
    }

    public static User checkLogin(String identifier, String password) {
        for (User user : users) {
            if ((user.getUsername().equalsIgnoreCase(identifier) && user.getPassword().equals(password))) {
                return user;
            }
        }
        return null;
    }

    public static List<User> getAllUsers() {
        return users;
    }
}
