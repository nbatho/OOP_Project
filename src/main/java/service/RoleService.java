package main.java.service;

import main.java.model.Role;

import java.util.List;

public interface RoleService {
    /**
     * Tạo một role mới
     * @param role đối tượng Role cần tạo
     * @return true nếu tạo thành công, false nếu thất bại
     */
    boolean create(Role role);

    /**
     * Lấy danh sách tất cả các role
     * @return List<Role> danh sách role, có thể rỗng
     */
    List<Role> getAll();

    /**
     * Lấy role theo role_id
     * @param role_id mã của role cần tìm
     * @return Role nếu tìm thấy, null nếu không tìm thấy
     */
    Role getById(String role_id);

    /**
     * Lấy role theo tên
     * @param name tên role cần tìm
     * @return Role nếu tìm thấy, null nếu không tìm thấy
     */
    Role getByName(String name);

    /**
     * Cập nhật tên role
     * @param role_id mã role cần cập nhật
     * @param name tên mới
     * @return true nếu cập nhật thành công, false nếu thất bại
     */
    boolean updateName(String role_id, String name);

    /**
     * Xóa role theo role_id
     * @param role_id mã role cần xóa
     * @return true nếu xóa thành công, false nếu thất bại
     */
    boolean deleteById(String role_id);
}
