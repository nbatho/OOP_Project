package main.java.service.impl;

import java.util.List;
import java.util.UUID;
import main.java.model.Role;
import main.java.repository.RoleRepository;
import main.java.service.RoleService;

public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepository;

    public RoleServiceImpl() {
        this.roleRepository = new RoleRepository();
    }

    @Override
    public boolean createRole(Role role) {
        try {
            if (role == null) {
                System.out.println("Role không được null");
                return false;
            }

            // Validate thông tin role
            if (!isValidRoleData(role)) {
                return false;
            }

            // Tạo ID mới nếu chưa có
            if (role.getRoleId() == null || role.getRoleId().trim().isEmpty()) {
                role.setRoleId(UUID.randomUUID().toString());
            }

            // Kiểm tra role name đã tồn tại chưa
            if (isRoleNameExists(role.getName())) {
                System.out.println("Tên role đã tồn tại: " + role.getName());
                return false;
            }

            return roleRepository.createRole(role);
        } catch (Exception e) {
            System.out.println("Lỗi khi tạo role: " + e.getMessage());
            return false;
        }
    }

    @Override
    public List<Role> getAllRoles() {
        try {
            return roleRepository.getAllRoles();
        } catch (Exception e) {
            System.out.println("Lỗi khi lấy danh sách roles: " + e.getMessage());
            return List.of();
        }
    }

    @Override
    public Role getRoleById(String roleId) {
        try {
            if (roleId == null || roleId.trim().isEmpty()) {
                System.out.println("Role ID không được null hoặc rỗng");
                return null;
            }

            return roleRepository.getRoleById(roleId);
        } catch (Exception e) {
            System.out.println("Lỗi khi lấy role theo ID: " + e.getMessage());
            return null;
        }
    }

    @Override
    public Role getRoleByName(String name) {
        try {
            if (name == null || name.trim().isEmpty()) {
                System.out.println("Tên role không được null hoặc rỗng");
                return null;
            }

            List<Role> roles = roleRepository.getAllRoles();
            return roles.stream()
                    .filter(role -> name.equalsIgnoreCase(role.getName()))
                    .findFirst()
                    .orElse(null);
        } catch (Exception e) {
            System.out.println("Lỗi khi lấy role theo tên: " + e.getMessage());
            return null;
        }
    }

    @Override
    public boolean updateRole(Role role) {
        try {
            if (role == null) {
                System.out.println("Role không được null");
                return false;
            }

            if (!isValidRoleData(role)) {
                return false;
            }

            // Kiểm tra role có tồn tại không
            Role existingRole = getRoleById(role.getRoleId());
            if (existingRole == null) {
                System.out.println("Role không tồn tại");
                return false;
            }

            // Kiểm tra tên role có trùng với role khác không (trừ chính nó)
            Role roleWithSameName = getRoleByName(role.getName());
            if (roleWithSameName != null && !roleWithSameName.getRoleId().equals(role.getRoleId())) {
                System.out.println("Tên role đã được sử dụng bởi role khác: " + role.getName());
                return false;
            }

            return roleRepository.updateRole(role);
        } catch (Exception e) {
            System.out.println("Lỗi khi cập nhật role: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean deleteRole(String roleId) {
        try {
            if (roleId == null || roleId.trim().isEmpty()) {
                System.out.println("Role ID không được null hoặc rỗng");
                return false;
            }

            // Kiểm tra role có tồn tại không
            Role existingRole = getRoleById(roleId);
            if (existingRole == null) {
                System.out.println("Role không tồn tại");
                return false;
            }

            // Kiểm tra role có phải là role hệ thống không (không được xóa)
            if (isSystemRole(existingRole.getName())) {
                System.out.println("Không thể xóa role hệ thống: " + existingRole.getName());
                return false;
            }

            return roleRepository.deleteRole(roleId);
        } catch (Exception e) {
            System.out.println("Lỗi khi xóa role: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean isRoleNameExists(String name) {
        try {
            if (name == null || name.trim().isEmpty()) {
                return false;
            }

            Role existingRole = getRoleByName(name);
            return existingRole != null;
        } catch (Exception e) {
            System.out.println("Lỗi khi kiểm tra tên role: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean createDefaultRoles() {
        try {
            String[] defaultRoles = {"Admin", "Manager", "Member", "Guest"};
            boolean allCreated = true;

            for (String roleName : defaultRoles) {
                if (!isRoleNameExists(roleName)) {
                    Role role = new Role();
                    role.setName(roleName);
                    
                    if (!createRole(role)) {
                        allCreated = false;
                        System.out.println("Lỗi khi tạo role mặc định: " + roleName);
                    } else {
                        System.out.println("Đã tạo role mặc định: " + roleName);
                    }
                }
            }

            return allCreated;
        } catch (Exception e) {
            System.out.println("Lỗi khi tạo roles mặc định: " + e.getMessage());
            return false;
        }
    }

    /**
     * Validate dữ liệu role
     */
    private boolean isValidRoleData(Role role) {
        if (role.getName() == null || role.getName().trim().isEmpty()) {
            System.out.println("Tên role không được null hoặc rỗng");
            return false;
        }

        if (role.getName().length() > 100) {
            System.out.println("Tên role quá dài (tối đa 100 ký tự)");
            return false;
        }

        // Kiểm tra ký tự hợp lệ trong tên role
        if (!role.getName().matches("^[a-zA-Z0-9_\\s]+$")) {
            System.out.println("Tên role chỉ được chứa chữ cái, số, dấu gạch dưới và khoảng trắng");
            return false;
        }

        return true;
    }

    /**
     * Kiểm tra role có phải là role hệ thống không
     */
    private boolean isSystemRole(String roleName) {
        String[] systemRoles = {"Admin", "Manager", "Member", "Guest"};
        for (String systemRole : systemRoles) {
            if (systemRole.equalsIgnoreCase(roleName)) {
                return true;
            }
        }
        return false;
    }
}
