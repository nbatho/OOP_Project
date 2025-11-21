package main.java.service;

import java.util.List;
import main.java.model.User;

public interface UserService {
    /**
     * Tạo một user mới
     * @param user đối tượng User cần tạo
     * @return true nếu tạo thành công, false nếu thất bại
     */
    boolean createUser(User user);

    /**
     * Lấy danh sách tất cả các user
     * @return List<User> danh sách user, có thể rỗng
     */
    List<User> getAllUsers();

    /**
     * Lấy user theo userId
     * @param userId mã của user cần tìm
     * @return User nếu tìm thấy, null nếu không tìm thấy
     */
    User getUserById(String userId);

    /**
     * Lấy user theo email
     * @param email email user cần tìm
     * @return User nếu tìm thấy, null nếu không tìm thấy
     */
    User getUserByEmail(String email);

    /**
     * Cập nhật thông tin user
     * @param user đối tượng User với thông tin mới
     * @return true nếu cập nhật thành công, false nếu thất bại
     */
    boolean updateUser(User user);

    /**
     * Cập nhật mật khẩu user
     * @param userId mã user cần cập nhật
     * @param newPasswordHash mật khẩu mới (đã hash)
     * @return true nếu cập nhật thành công, false nếu thất bại
     */
    boolean updatePassword(String userId, String newPasswordHash);

    /**
     * Xóa user theo userId
     * @param userId mã user cần xóa
     * @return true nếu xóa thành công, false nếu thất bại
     */
    boolean deleteUser(String userId);

    /**
     * Xác thực đăng nhập user
     * @param email email đăng nhập
     * @param password mật khẩu (chưa hash)
     * @return User nếu đăng nhập thành công, null nếu thất bại
     */
    User authenticateUser(String email, String password);

    /**
     * Kiểm tra email đã tồn tại chưa
     * @param email email cần kiểm tra
     * @return true nếu email đã tồn tại, false nếu chưa
     */
    boolean isEmailExists(String email);

    /**
     * Kiểm tra user có tồn tại không
     * @param userId mã user cần kiểm tra
     * @return true nếu user tồn tại, false nếu không
     */
    boolean userExists(String userId);
}
