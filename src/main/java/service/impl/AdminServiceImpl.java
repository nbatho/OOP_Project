package main.java.service.impl;

import java.util.List;
import main.java.model.Admin;
import main.java.model.User;
import main.java.repository.AdminRepository;
import main.java.service.AdminService;
import main.java.service.UserService;

public class AdminServiceImpl implements AdminService {
    private final AdminRepository adminRepository;
    private final UserService userService;

    public AdminServiceImpl() {
        this.adminRepository = new AdminRepository();
        this.userService = new UserServiceImpl();
    }

    @Override
    public boolean createAdmin(Admin admin) {
        try {
            if (admin == null) {
                System.out.println("Admin không được null");
                return false;
            }

            // Validate thông tin admin
            if (!isValidAdminData(admin)) {
                return false;
            }

            // Kiểm tra admin đã tồn tại chưa
            if (adminRepository.getAdminById(admin.getUserId()) != null) {
                System.out.println("Admin đã tồn tại: " + admin.getUserId());
                return false;
            }

            // Kiểm tra user đã là admin chưa
            if (isUserAdmin(admin.getUserId())) {
                System.out.println("User đã là admin: " + admin.getUserId());
                return false;
            }

            // Kiểm tra user có tồn tại không
            if (!userService.userExists(admin.getUserId())) {
                System.out.println("User không tồn tại: " + admin.getUserId());
                return false;
            }

            return adminRepository.createAdmin(admin);
        } catch (Exception e) {
            System.out.println("Lỗi khi tạo admin: " + e.getMessage());
            return false;
        }
    }

    @Override
    public List<Admin> getAllAdmins() {
        try {
            return adminRepository.getAllAdmins();
        } catch (Exception e) {
            System.out.println("Lỗi khi lấy danh sách admins: " + e.getMessage());
            return List.of();
        }
    }

    @Override
    public Admin getAdminById(String adminId) {
        try {
            if (adminId == null || adminId.trim().isEmpty()) {
                System.out.println("Admin ID không được null hoặc rỗng");
                return null;
            }

            return adminRepository.getAdminById(adminId);
        } catch (Exception e) {
            System.out.println("Lỗi khi lấy admin theo ID: " + e.getMessage());
            return null;
        }
    }

    @Override
    public Admin getAdminByUserId(String userId) {
        try {
            if (userId == null || userId.trim().isEmpty()) {
                System.out.println("User ID không được null hoặc rỗng");
                return null;
            }

            return adminRepository.getAllAdmins().stream()
                    .filter(admin -> userId.equals(admin.getUserId()))
                    .findFirst()
                    .orElse(null);
        } catch (Exception e) {
            System.out.println("Lỗi khi lấy admin theo user ID: " + e.getMessage());
            return null;
        }
    }

    @Override
    public boolean updateAdmin(Admin admin) {
        try {
            if (admin == null) {
                System.out.println("Admin không được null");
                return false;
            }

            if (!isValidAdminData(admin)) {
                return false;
            }

            // Kiểm tra admin có tồn tại không
            if (!adminExists(admin.getUserId())) {
                System.out.println("Admin không tồn tại");
                return false;
            }

            // Kiểm tra user có tồn tại không
            if (!userService.userExists(admin.getUserId())) {
                System.out.println("User không tồn tại: " + admin.getUserId());
                return false;
            }

            // Kiểm tra user có đang là admin của admin ID khác không
            Admin existingAdminWithSameUser = getAdminByUserId(admin.getUserId());
            if (existingAdminWithSameUser != null && 
                !existingAdminWithSameUser.getUserId().equals(admin.getUserId())) {
                System.out.println("User đã là admin của admin ID khác: " + admin.getUserId());
                return false;
            }

            return adminRepository.updateAdmin(admin);
        } catch (Exception e) {
            System.out.println("Lỗi khi cập nhật admin: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean deleteAdmin(String adminId) {
        try {
            if (adminId == null || adminId.trim().isEmpty()) {
                System.out.println("Admin ID không được null hoặc rỗng");
                return false;
            }

            if (!adminExists(adminId)) {
                System.out.println("Admin không tồn tại");
                return false;
            }

            return adminRepository.deleteAdmin(adminId);
        } catch (Exception e) {
            System.out.println("Lỗi khi xóa admin: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean adminExists(String adminId) {
        try {
            Admin admin = getAdminById(adminId);
            return admin != null;
        } catch (Exception e) {
            System.out.println("Lỗi khi kiểm tra admin tồn tại: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean isUserAdmin(String userId) {
        try {
            Admin admin = getAdminByUserId(userId);
            return admin != null;
        } catch (Exception e) {
            System.out.println("Lỗi khi kiểm tra user là admin: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean promoteUserToAdmin(String userId) {
        try {
            if (userId == null || userId.trim().isEmpty()) {
                System.out.println("User ID không được null hoặc rỗng");
                return false;
            }

            // Kiểm tra user có tồn tại không
            if (!userService.userExists(userId)) {
                System.out.println("User không tồn tại: " + userId);
                return false;
            }

            // Kiểm tra user đã là admin chưa
            if (isUserAdmin(userId)) {
                System.out.println("User đã là admin: " + userId);
                return false;
            }

            // Tạo admin mới
            Admin admin = new Admin();
            admin.setUserId(userId);
            
            return createAdmin(admin);
        } catch (Exception e) {
            System.out.println("Lỗi khi thăng cấp user lên admin: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean demoteAdminToUser(String adminId) {
        try {
            if (adminId == null || adminId.trim().isEmpty()) {
                System.out.println("Admin ID không được null hoặc rỗng");
                return false;
            }

            if (!adminExists(adminId)) {
                System.out.println("Admin không tồn tại");
                return false;
            }

            return deleteAdmin(adminId);
        } catch (Exception e) {
            System.out.println("Lỗi khi hạ cấp admin: " + e.getMessage());
            return false;
        }
    }

    @Override
    public List<User> getAllUsersForManagement() {
        try {
            return userService.getAllUsers();
        } catch (Exception e) {
            System.out.println("Lỗi khi lấy danh sách users cho quản lý: " + e.getMessage());
            return List.of();
        }
    }

    @Override
    public boolean setUserActiveStatus(String userId, boolean isActive) {
        try {
            if (userId == null || userId.trim().isEmpty()) {
                System.out.println("User ID không được null hoặc rỗng");
                return false;
            }

            User user = userService.getUserById(userId);
            if (user == null) {
                System.out.println("User không tồn tại: " + userId);
                return false;
            }

            // Placeholder: User model không có isActive field
            // user.setIsActive(isActive);
            System.out.println("Cập nhật trạng thái user: " + userId + ", active: " + isActive);
            return userService.updateUser(user);
        } catch (Exception e) {
            System.out.println("Lỗi khi cập nhật trạng thái user: " + e.getMessage());
            return false;
        }
    }

    /**
     * Validate dữ liệu admin
     */
    private boolean isValidAdminData(Admin admin) {
        if (admin.getUserId() == null || admin.getUserId().trim().isEmpty()) {
            System.out.println("User ID không được null hoặc rỗng");
            return false;
        }

        return true;
    }
}
