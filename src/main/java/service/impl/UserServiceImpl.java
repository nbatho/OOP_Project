package main.java.service.impl;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.UUID;
import main.java.model.User;
import main.java.repository.UserRepository;
import main.java.service.UserService;

public class UserServiceImpl implements UserService {
    
    private final UserRepository userRepository;
    
    public UserServiceImpl() {
        this.userRepository = new UserRepository();
    }
    
    @Override
    public boolean createUser(User user) {
        if (user == null) {
            System.err.println("User object không được null");
            return false;
        }
        
        // Validate dữ liệu
        if (user.getEmail() == null || user.getEmail().trim().isEmpty()) {
            System.err.println("Email không được để trống");
            return false;
        }
        
        if (user.getFullName() == null || user.getFullName().trim().isEmpty()) {
            System.err.println("Full name không được để trống");
            return false;
        }
        
        // Kiểm tra email đã tồn tại chưa
        if (isEmailExists(user.getEmail())) {
            System.err.println("Email đã tồn tại: " + user.getEmail());
            return false;
        }
        
        // Tự động tạo ID nếu chưa có
        if (user.getUserId() == null || user.getUserId().trim().isEmpty()) {
            user.setUserId(UUID.randomUUID().toString());
        }
        
        // Hash password nếu cần
        if (user.getPasswordHash() != null && !user.getPasswordHash().isEmpty()) {
            // Giả định password đã được hash hoặc hash ở đây
            user.setPasswordHash(hashPassword(user.getPasswordHash()));
        }
        
        return userRepository.createUser(user);
    }
    
    @Override
    public List<User> getAllUsers() {
        return userRepository.getAllUsers();
    }
    
    @Override
    public User getUserById(String userId) {
        if (userId == null || userId.trim().isEmpty()) {
            System.err.println("User ID không được để trống");
            return null;
        }
        return userRepository.getUserById(userId);
    }
    
    @Override
    public User getUserByEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            System.err.println("Email không được để trống");
            return null;
        }
        return userRepository.getUserByEmail(email);
    }
    
    @Override
    public boolean updateUser(User user) {
        if (user == null || user.getUserId() == null) {
            System.err.println("User hoặc User ID không được null");
            return false;
        }
        
        // Kiểm tra user có tồn tại không
        User existingUser = getUserById(user.getUserId());
        if (existingUser == null) {
            System.err.println("User không tồn tại với ID: " + user.getUserId());
            return false;
        }
        
        // Nếu email thay đổi, kiểm tra email mới có trùng không
        if (!existingUser.getEmail().equals(user.getEmail()) && isEmailExists(user.getEmail())) {
            System.err.println("Email mới đã tồn tại: " + user.getEmail());
            return false;
        }
        
        return userRepository.updateUser(user);
    }
    
    @Override
    public boolean updatePassword(String userId, String newPasswordHash) {
        if (userId == null || userId.trim().isEmpty()) {
            System.err.println("User ID không được để trống");
            return false;
        }
        
        if (newPasswordHash == null || newPasswordHash.trim().isEmpty()) {
            System.err.println("Password không được để trống");
            return false;
        }
        
        // Lấy user hiện tại
        User user = getUserById(userId);
        if (user == null) {
            System.err.println("User không tồn tại với ID: " + userId);
            return false;
        }
        
        // Cập nhật password
        String hashedPassword = hashPassword(newPasswordHash);
        user.setPasswordHash(hashedPassword);
        
        return userRepository.updateUser(user);
    }
    
    @Override
    public boolean deleteUser(String userId) {
        if (userId == null || userId.trim().isEmpty()) {
            System.err.println("User ID không được để trống");
            return false;
        }
        
        // Kiểm tra user có tồn tại không
        User existingUser = getUserById(userId);
        if (existingUser == null) {
            System.err.println("User không tồn tại với ID: " + userId);
            return false;
        }
        
        return userRepository.deleteUser(userId);
    }
    
    @Override
    public User authenticateUser(String email, String password) {
        if (email == null || email.trim().isEmpty()) {
            System.err.println("Email không được để trống");
            return null;
        }
        
        if (password == null || password.trim().isEmpty()) {
            System.err.println("Password không được để trống");
            return null;
        }
        
        User user = getUserByEmail(email);
        if (user == null) {
            System.err.println("User không tồn tại với email: " + email);
            return null;
        }
        
        String hashedPassword = hashPassword(password);
        if (user.getPasswordHash().equals(hashedPassword)) {
            System.out.println("Đăng nhập thành công cho user: " + email);
            return user;
        } else {
            System.err.println("Sai mật khẩu cho user: " + email);
            return null;
        }
    }
    
    @Override
    public boolean isEmailExists(String email) {
        if (email == null || email.trim().isEmpty()) {
            return false;
        }
        return getUserByEmail(email) != null;
    }
    
    /**
     * Hash password bằng MD5
     * @param password password gốc
     * @return password đã hash
     */
    private String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] hashedBytes = md.digest(password.getBytes());
            
            // Convert byte array thành hex string
            StringBuilder sb = new StringBuilder();
            for (byte b : hashedBytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            System.err.println("Lỗi khi hash password: " + e.getMessage());
            return password; // Fallback, không an toàn trong production
        }
    }

    @Override
    public boolean userExists(String userId) {
        if (userId == null || userId.trim().isEmpty()) {
            return false;
        }
        User user = getUserById(userId);
        return user != null;
    }
}
