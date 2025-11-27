package main.java.service;

import main.java.model.ProjectMember;
import main.java.model.User;

import java.util.List;

public interface ProjectMemberService {
    /**
     * Thêm một project member mới
     * @param projectMember đối tượng ProjectMember cần tạo
     * @return true nếu tạo thành công, false nếu thất bại
     */
    boolean create(ProjectMember projectMember);

    /**
     * Lấy danh sách tất cả các project members
     * @return List<ProjectMember> danh sách project members, có thể rỗng
     */
    List<ProjectMember> getAll();

    /**
     * Lấy project member theo project_id và user_id
     * @param project_id mã của project
     * @param user_id mã của user
     * @return ProjectMember nếu tìm thấy, null nếu không tìm thấy
     */
    ProjectMember getByProjectIdAndUserId(String project_id, String user_id);

    List<User> getProjectMember(String project_id);

    List <User> getAvailableUsers(String project_id);
    /**
     * Lấy danh sách tất cả members của một project
     * @param project_id mã của project
     * @return List<ProjectMember> danh sách members của project, có thể rỗng
     */
    List<ProjectMember> getByProjectId(String project_id);

    /**
     * Lấy danh sách tất cả projects của một user
     * @param user_id mã của user
     * @return List<ProjectMember> danh sách projects của user, có thể rỗng
     */
    List<ProjectMember> getByUserId(String user_id);

    /**
     * Lấy danh sách tất cả members có một role cụ thể
     * @param role_id mã của role
     * @return List<ProjectMember> danh sách members có role đó, có thể rỗng
     */
    List<ProjectMember> getByRoleId(String role_id);

    /**
     * Cập nhật role của project member
     * @param project_id mã project
     * @param user_id mã user
     * @param role_id role_id mới
     * @return true nếu cập nhật thành công, false nếu thất bại
     */
    boolean updateRole(String project_id, String user_id, String role_id);

    /**
     * Xóa project member theo project_id và user_id
     * @param project_id mã của project
     * @param user_id mã của user
     * @return true nếu xóa thành công, false nếu thất bại
     */
    boolean deleteByProjectIdAndUserId(String project_id, String user_id);

    /**
     * Xóa tất cả members của một project
     * @param project_id mã của project
     * @return true nếu xóa thành công, false nếu thất bại
     */
    boolean deleteByProjectId(String project_id);

    /**
     * Xóa tất cả project memberships của một user
     * @param user_id mã của user
     * @return true nếu xóa thành công, false nếu thất bại
     */
    boolean deleteByUserId(String user_id);
}
