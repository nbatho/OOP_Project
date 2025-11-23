package main.java.service.impl;

import java.util.List;
import main.java.model.Manager;
import main.java.model.Project;
import main.java.model.Team;
import main.java.service.ManagerService;
import main.java.service.ProjectService;
import main.java.service.TeamService;
import main.java.service.UserService;

public class ManagerServiceImpl implements ManagerService {
    private final UserService userService;
    private final ProjectService projectService;
    private final TeamService teamService;

    public ManagerServiceImpl() {
        this.userService = new UserServiceImpl();
        this.projectService = new ProjectServiceImpl();
        this.teamService = new TeamServiceImpl();
    }

    @Override
    public boolean createManager(Manager manager) {
        try {
            if (manager == null) {
                System.out.println("Manager không được null");
                return false;
            }

            // Validate thông tin manager
            if (!isValidManagerData(manager)) {
                return false;
            }

            // Kiểm tra user có tồn tại không
            if (!userService.userExists(manager.getUserId())) {
                System.out.println("User không tồn tại: " + manager.getUserId());
                return false;
            }

            // Kiểm tra user đã là manager chưa
            if (isUserManager(manager.getUserId())) {
                System.out.println("User đã là manager: " + manager.getUserId());
                return false;
            }

            // Lưu manager vào database (giả lập có repository)
            // return managerRepository.createManager(manager);
            System.out.println("Manager được tạo thành công: " + manager.getUserId());
            return true;
        } catch (Exception e) {
            System.out.println("Lỗi khi tạo manager: " + e.getMessage());
            return false;
        }
    }

    @Override
    public List<Manager> getAllManagers() {
        try {
            // return managerRepository.getAllManagers();
            System.out.println("Lấy danh sách managers");
            return List.of(); // Placeholder
        } catch (Exception e) {
            System.out.println("Lỗi khi lấy danh sách managers: " + e.getMessage());
            return List.of();
        }
    }

    @Override
    public Manager getManagerById(String managerId) {
        try {
            if (managerId == null || managerId.trim().isEmpty()) {
                System.out.println("Manager ID không được null hoặc rỗng");
                return null;
            }

            // return managerRepository.getManagerById(managerId);
            System.out.println("Lấy manager theo ID: " + managerId);
            return null; // Placeholder
        } catch (Exception e) {
            System.out.println("Lỗi khi lấy manager theo ID: " + e.getMessage());
            return null;
        }
    }

    @Override
    public Manager getManagerByUserId(String userId) {
        try {
            if (userId == null || userId.trim().isEmpty()) {
                System.out.println("User ID không được null hoặc rỗng");
                return null;
            }

            // return managerRepository.getManagerByUserId(userId);
            System.out.println("Lấy manager theo user ID: " + userId);
            return null; // Placeholder
        } catch (Exception e) {
            System.out.println("Lỗi khi lấy manager theo user ID: " + e.getMessage());
            return null;
        }
    }

    @Override
    public boolean updateManager(Manager manager) {
        try {
            if (manager == null) {
                System.out.println("Manager không được null");
                return false;
            }

            if (!isValidManagerData(manager)) {
                return false;
            }

            if (!managerExists(manager.getUserId())) {
                System.out.println("Manager không tồn tại");
                return false;
            }

            // return managerRepository.updateManager(manager);
            System.out.println("Cập nhật manager thành công: " + manager.getUserId());
            return true;
        } catch (Exception e) {
            System.out.println("Lỗi khi cập nhật manager: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean deleteManager(String managerId) {
        try {
            if (managerId == null || managerId.trim().isEmpty()) {
                System.out.println("Manager ID không được null hoặc rỗng");
                return false;
            }

            if (!managerExists(managerId)) {
                System.out.println("Manager không tồn tại");
                return false;
            }

            // return managerRepository.deleteManager(managerId);
            System.out.println("Xóa manager thành công: " + managerId);
            return true;
        } catch (Exception e) {
            System.out.println("Lỗi khi xóa manager: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean managerExists(String managerId) {
        try {
            Manager manager = getManagerById(managerId);
            return manager != null;
        } catch (Exception e) {
            System.out.println("Lỗi khi kiểm tra manager tồn tại: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean isUserManager(String userId) {
        try {
            Manager manager = getManagerByUserId(userId);
            return manager != null;
        } catch (Exception e) {
            System.out.println("Lỗi khi kiểm tra user là manager: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean promoteUserToManager(String userId) {
        try {
            if (userId == null || userId.trim().isEmpty()) {
                System.out.println("User ID không được null hoặc rỗng");
                return false;
            }

            if (!userService.userExists(userId)) {
                System.out.println("User không tồn tại: " + userId);
                return false;
            }

            if (isUserManager(userId)) {
                System.out.println("User đã là manager: " + userId);
                return false;
            }

            Manager manager = new Manager();
            manager.setUserId(userId);
            
            return createManager(manager);
        } catch (Exception e) {
            System.out.println("Lỗi khi thăng cấp user lên manager: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean demoteManagerToUser(String managerId) {
        try {
            return deleteManager(managerId);
        } catch (Exception e) {
            System.out.println("Lỗi khi hạ cấp manager: " + e.getMessage());
            return false;
        }
    }

    @Override
    public List<Project> getManagedProjects(String managerId) {
        try {
            if (managerId == null || managerId.trim().isEmpty()) {
                return List.of();
            }

            // Placeholder - cần implement logic để lấy projects của manager
            System.out.println("Lấy danh sách projects của manager: " + managerId);
            return List.of();
        } catch (Exception e) {
            System.out.println("Lỗi khi lấy projects của manager: " + e.getMessage());
            return List.of();
        }
    }

    @Override
    public List<Team> getManagedTeams(String managerId) {
        try {
            if (managerId == null || managerId.trim().isEmpty()) {
                return List.of();
            }

            // Placeholder - cần implement logic để lấy teams của manager  
            System.out.println("Lấy danh sách teams của manager: " + managerId);
            return List.of();
        } catch (Exception e) {
            System.out.println("Lỗi khi lấy teams của manager: " + e.getMessage());
            return List.of();
        }
    }

    @Override
    public boolean assignToProject(String managerId, String projectId) {
        try {
            if (!managerExists(managerId)) {
                System.out.println("Manager không tồn tại");
                return false;
            }

            if (!projectService.projectExists(projectId)) {
                System.out.println("Project không tồn tại");
                return false;
            }

            // Gán manager cho project
            System.out.println("Gán manager " + managerId + " cho project " + projectId);
            return true;
        } catch (Exception e) {
            System.out.println("Lỗi khi gán manager cho project: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean assignToTeam(String managerId, String teamId) {
        try {
            if (!managerExists(managerId)) {
                System.out.println("Manager không tồn tại");
                return false;
            }

            if (!teamService.teamExists(teamId)) {
                System.out.println("Team không tồn tại");
                return false;
            }

            // Gán manager cho team
            System.out.println("Gán manager " + managerId + " cho team " + teamId);
            return true;
        } catch (Exception e) {
            System.out.println("Lỗi khi gán manager cho team: " + e.getMessage());
            return false;
        }
    }

    /**
     * Validate dữ liệu manager
     */
    private boolean isValidManagerData(Manager manager) {
        if (manager.getUserId() == null || manager.getUserId().trim().isEmpty()) {
            System.out.println("User ID không được null hoặc rỗng");
            return false;
        }

        return true;
    }
}
