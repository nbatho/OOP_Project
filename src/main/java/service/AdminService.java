package main.java.service;

import java.util.List;
import main.java.model.Admin;
import main.java.model.User;

public interface AdminService {
    /**
     * Tạo một admin mới
     * @param admin đối tượng Admin cần tạo
     * @return true nếu tạo thành công, false nếu thất bại
     */
    boolean createAdmin(Admin admin);

    /**
     * Lấy danh sách tất cả các admins
     * @return List<Admin> danh sách admins, có thể rỗng
     */
    List<Admin> getAllAdmins();

    /**
     * Lấy admin theo adminId
     * @param adminId mã của admin cần tìm
     * @return Admin nếu tìm thấy, null nếu không tìm thấy
     */
    Admin getAdminById(String adminId);

    /**
     * Lấy admin theo userId
     * @param userId mã user cần tìm
     * @return Admin nếu tìm thấy, null nếu không tìm thấy
     */
    Admin getAdminByUserId(String userId);

    /**
     * Cập nhật thông tin admin
     * @param admin đối tượng Admin với thông tin mới
     * @return true nếu cập nhật thành công, false nếu thất bại
     */
    boolean updateAdmin(Admin admin);

    /**
     * Xóa admin theo adminId
     * @param adminId mã admin cần xóa
     * @return true nếu xóa thành công, false nếu thất bại
     */
    boolean deleteAdmin(String adminId);

    /**
     * Kiểm tra admin có tồn tại không
     * @param adminId mã admin
     * @return true nếu admin tồn tại
     */
    boolean adminExists(String adminId);

    /**
     * Kiểm tra user đã là admin chưa
     * @param userId mã user
     * @return true nếu đã là admin
     */
    boolean isUserAdmin(String userId);

    /**
     * Thăng cấp user lên admin
     * @param userId mã user cần thăng cấp
     * @return true nếu thành công
     */
    boolean promoteUserToAdmin(String userId);

    /**
     * Hạ cấp admin xuống user thường
     * @param adminId mã admin cần hạ cấp
     * @return true nếu thành công
     */
    boolean demoteAdminToUser(String adminId);

    /**
     * Lấy danh sách tất cả users trong hệ thống (cho admin quản lý)
     * @return List<User> danh sách users
     */
    List<User> getAllUsersForManagement();

    /**
     * Khóa/mở khóa user account
     * @param userId mã user
     * @param isActive trạng thái mới
     * @return true nếu thành công
     */
    boolean setUserActiveStatus(String userId, boolean isActive);
}
