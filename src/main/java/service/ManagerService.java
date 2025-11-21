package main.java.service;

import java.util.List;
import main.java.model.Manager;
import main.java.model.Project;
import main.java.model.Team;

public interface ManagerService {
    /**
     * Tạo một manager mới
     * @param manager đối tượng Manager cần tạo
     * @return true nếu tạo thành công, false nếu thất bại
     */
    boolean createManager(Manager manager);

    /**
     * Lấy danh sách tất cả các managers
     * @return List<Manager> danh sách managers, có thể rỗng
     */
    List<Manager> getAllManagers();

    /**
     * Lấy manager theo managerId
     * @param managerId mã của manager cần tìm
     * @return Manager nếu tìm thấy, null nếu không tìm thấy
     */
    Manager getManagerById(String managerId);

    /**
     * Lấy manager theo userId
     * @param userId mã user cần tìm
     * @return Manager nếu tìm thấy, null nếu không tìm thấy
     */
    Manager getManagerByUserId(String userId);

    /**
     * Cập nhật thông tin manager
     * @param manager đối tượng Manager với thông tin mới
     * @return true nếu cập nhật thành công, false nếu thất bại
     */
    boolean updateManager(Manager manager);

    /**
     * Xóa manager theo managerId
     * @param managerId mã manager cần xóa
     * @return true nếu xóa thành công, false nếu thất bại
     */
    boolean deleteManager(String managerId);

    /**
     * Kiểm tra manager có tồn tại không
     * @param managerId mã manager
     * @return true nếu manager tồn tại
     */
    boolean managerExists(String managerId);

    /**
     * Kiểm tra user đã là manager chưa
     * @param userId mã user
     * @return true nếu đã là manager
     */
    boolean isUserManager(String userId);

    /**
     * Thăng cấp user lên manager
     * @param userId mã user cần thăng cấp
     * @return true nếu thành công
     */
    boolean promoteUserToManager(String userId);

    /**
     * Hạ cấp manager xuống user thường
     * @param managerId mã manager cần hạ cấp
     * @return true nếu thành công
     */
    boolean demoteManagerToUser(String managerId);

    /**
     * Lấy danh sách projects mà manager quản lý
     * @param managerId mã manager
     * @return List<Project> danh sách projects
     */
    List<Project> getManagedProjects(String managerId);

    /**
     * Lấy danh sách teams mà manager quản lý
     * @param managerId mã manager
     * @return List<Team> danh sách teams
     */
    List<Team> getManagedTeams(String managerId);

    /**
     * Gán manager cho project
     * @param managerId mã manager
     * @param projectId mã project
     * @return true nếu thành công
     */
    boolean assignToProject(String managerId, String projectId);

    /**
     * Gán manager cho team
     * @param managerId mã manager
     * @param teamId mã team
     * @return true nếu thành công
     */
    boolean assignToTeam(String managerId, String teamId);
}
