package main.java.service;

import java.util.List;
import main.java.model.Role;

public interface RoleService {
    /**
     * Tạo một role mới
     * @param role đối tượng Role cần tạo
     * @return true nếu tạo thành công, false nếu thất bại
     */
    boolean createRole(Role role);

    /**
     * Lấy danh sách tất cả các role
     * @return List<Role> danh sách role, có thể rỗng
     */
    List<Role> getAllRoles();

    /**
     * Lấy role theo roleId
     * @param roleId mã của role cần tìm
     * @return Role nếu tìm thấy, null nếu không tìm thấy
     */
    Role getRoleById(String roleId);

    /**
     * Lấy role theo tên
     * @param name tên role cần tìm
     * @return Role nếu tìm thấy, null nếu không tìm thấy
     */
    Role getRoleByName(String name);

    /**
     * Cập nhật thông tin role
     * @param role đối tượng Role với thông tin mới
     * @return true nếu cập nhật thành công, false nếu thất bại
     */
    boolean updateRole(Role role);

    /**
     * Xóa role theo roleId
     * @param roleId mã role cần xóa
     * @return true nếu xóa thành công, false nếu thất bại
     */
    boolean deleteRole(String roleId);

    /**
     * Kiểm tra tên role đã tồn tại chưa
     * @param name tên role cần kiểm tra
     * @return true nếu tên đã tồn tại, false nếu chưa
     */
    boolean isRoleNameExists(String name);

    /**
     * Tạo các role mặc định của hệ thống
     * @return true nếu tạo thành công
     */
    boolean createDefaultRoles();
}
