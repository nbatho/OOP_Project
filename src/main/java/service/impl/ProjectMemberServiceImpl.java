package main.java.service.impl;

import java.util.List;
import main.java.model.ProjectMember;
import main.java.service.ProjectMemberService;
import main.java.service.ProjectService;
import main.java.service.RoleService;
import main.java.service.UserService;

public class ProjectMemberServiceImpl implements ProjectMemberService {
    private final ProjectService projectService;
    private final UserService userService;
    private final RoleService roleService;

    public ProjectMemberServiceImpl() {
        this.projectService = new ProjectServiceImpl();
        this.userService = new UserServiceImpl();
        this.roleService = new RoleServiceImpl();
    }

    @Override
    public boolean create(ProjectMember projectMember) {
        try {
            if (projectMember == null) {
                System.out.println("ProjectMember không được null");
                return false;
            }

            if (!isValidProjectMemberData(projectMember)) {
                return false;
            }

            // Kiểm tra project có tồn tại không
            if (!projectService.projectExists(projectMember.getProjectId())) {
                System.out.println("Project không tồn tại: " + projectMember.getProjectId());
                return false;
            }

            // Kiểm tra user có tồn tại không
            if (!userService.userExists(projectMember.getUserId())) {
                System.out.println("User không tồn tại: " + projectMember.getUserId());
                return false;
            }

            // Kiểm tra user đã là member của project chưa
            ProjectMember existing = getByProjectIdAndUserId(projectMember.getProjectId(), projectMember.getUserId());
            if (existing != null) {
                System.out.println("User đã là member của project");
                return false;
            }

            // return projectMemberRepository.createProjectMember(projectMember);
            System.out.println("Thêm project member thành công: " + projectMember.getUserId());
            return true;
        } catch (Exception e) {
            System.out.println("Lỗi khi tạo project member: " + e.getMessage());
            return false;
        }
    }

    @Override
    public List<ProjectMember> getAll() {
        try {
            // return projectMemberRepository.getAllProjectMembers();
            System.out.println("Lấy danh sách tất cả project members");
            return List.of(); // Placeholder
        } catch (Exception e) {
            System.out.println("Lỗi khi lấy danh sách project members: " + e.getMessage());
            return List.of();
        }
    }

    @Override
    public ProjectMember getByProjectIdAndUserId(String project_id, String user_id) {
        try {
            if (project_id == null || project_id.trim().isEmpty()) {
                System.out.println("Project ID không được null hoặc rỗng");
                return null;
            }

            if (user_id == null || user_id.trim().isEmpty()) {
                System.out.println("User ID không được null hoặc rỗng");
                return null;
            }

            // return projectMemberRepository.getByProjectAndUser(project_id, user_id);
            System.out.println("Lấy project member theo project và user: " + project_id + ", " + user_id);
            return null; // Placeholder
        } catch (Exception e) {
            System.out.println("Lỗi khi lấy project member: " + e.getMessage());
            return null;
        }
    }

    @Override
    public List<ProjectMember> getByProjectId(String project_id) {
        try {
            if (project_id == null || project_id.trim().isEmpty()) {
                System.out.println("Project ID không được null hoặc rỗng");
                return List.of();
            }

            // return projectMemberRepository.getMembersByProject(project_id);
            System.out.println("Lấy members theo project: " + project_id);
            return List.of(); // Placeholder
        } catch (Exception e) {
            System.out.println("Lỗi khi lấy members của project: " + e.getMessage());
            return List.of();
        }
    }

    @Override
    public List<ProjectMember> getByUserId(String user_id) {
        try {
            if (user_id == null || user_id.trim().isEmpty()) {
                System.out.println("User ID không được null hoặc rỗng");
                return List.of();
            }

            // return projectMemberRepository.getProjectsByUser(user_id);
            System.out.println("Lấy projects theo user: " + user_id);
            return List.of(); // Placeholder
        } catch (Exception e) {
            System.out.println("Lỗi khi lấy projects của user: " + e.getMessage());
            return List.of();
        }
    }

    @Override
    public List<ProjectMember> getByRoleId(String role_id) {
        try {
            if (role_id == null || role_id.trim().isEmpty()) {
                System.out.println("Role ID không được null hoặc rỗng");
                return List.of();
            }

            // return projectMemberRepository.getMembersByRole(role_id);
            System.out.println("Lấy members theo role: " + role_id);
            return List.of(); // Placeholder
        } catch (Exception e) {
            System.out.println("Lỗi khi lấy members của role: " + e.getMessage());
            return List.of();
        }
    }

    @Override
    public boolean updateRole(String project_id, String user_id, String role_id) {
        try {
            ProjectMember member = getByProjectIdAndUserId(project_id, user_id);
            if (member == null) {
                System.out.println("Project member không tồn tại");
                return false;
            }

            member.setRoleId(role_id);
            // return projectMemberRepository.updateProjectMember(member);
            System.out.println("Cập nhật role thành công: " + role_id);
            return true;
        } catch (Exception e) {
            System.out.println("Lỗi khi cập nhật role: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean deleteByProjectIdAndUserId(String project_id, String user_id) {
        try {
            if (getByProjectIdAndUserId(project_id, user_id) == null) {
                System.out.println("Project member không tồn tại");
                return false;
            }

            // return projectMemberRepository.deleteByProjectAndUser(project_id, user_id);
            System.out.println("Xóa project member thành công");
            return true;
        } catch (Exception e) {
            System.out.println("Lỗi khi xóa project member: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean deleteByProjectId(String project_id) {
        try {
            if (project_id == null || project_id.trim().isEmpty()) {
                return false;
            }

            // return projectMemberRepository.deleteByProject(project_id);
            System.out.println("Xóa tất cả members của project: " + project_id);
            return true;
        } catch (Exception e) {
            System.out.println("Lỗi khi xóa members của project: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean deleteByUserId(String user_id) {
        try {
            if (user_id == null || user_id.trim().isEmpty()) {
                return false;
            }

            // return projectMemberRepository.deleteByUser(user_id);
            System.out.println("Xóa tất cả projects của user: " + user_id);
            return true;
        } catch (Exception e) {
            System.out.println("Lỗi khi xóa projects của user: " + e.getMessage());
            return false;
        }
    }

    /**
     * Validate dữ liệu project member
     */
    private boolean isValidProjectMemberData(ProjectMember member) {
        if (member.getProjectId() == null || member.getProjectId().trim().isEmpty()) {
            System.out.println("Project ID không được null hoặc rỗng");
            return false;
        }

        if (member.getUserId() == null || member.getUserId().trim().isEmpty()) {
            System.out.println("User ID không được null hoặc rỗng");
            return false;
        }

        return true;
    }
}
