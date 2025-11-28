package main.java.service;

import java.util.List;
import main.java.model.User;

public interface UserService {

    boolean createUser(User user);

    List<User> getAllUsers();

    User getUserById(String userId);

    User getUserByEmail(String email);

    boolean updateUser(User user);

    boolean updatePassword(String userId, String newPasswordHash);

    boolean deleteUser(String userId);

    User authenticateUser(String email, String password);

    boolean isEmailExists(String email);

    boolean userExists(String userId);
}
