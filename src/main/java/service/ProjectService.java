package main.java.service;

import main.java.model.Project;

import java.util.List;

public interface ProjectService {
    /**
     * Tạo một project mới
     * @param project đối tượng Project cần tạo
     * @return true nếu tạo thành công, false nếu thất bại
     */
    boolean create(Project project);

    /**
     * Lấy danh sách tất cả các project
     * @return List<Project> danh sách project, có thể rỗng
     */
    List<Project> getAll();

    /**
     * Lấy project theo project_id
     * @param project_id mã của project cần tìm
     * @return Project nếu tìm thấy, null nếu không tìm thấy
     */
    Project getById(String project_id);

    /**
     * Lấy danh sách các project theo team_id
     * @param team_id mã của team
     * @return List<Project> danh sách projects của team, có thể rỗng
     */
    List<Project> getByTeamId(String team_id);

    /**
     * Lấy project theo tên
     * @param name tên project cần tìm
     * @return Project nếu tìm thấy, null nếu không tìm thấy
     */
    Project getByName(String name);

    /**
     * Cập nhật toàn bộ thông tin project
     * @param project_id mã project cần cập nhật
     * @param team_id mã team mới
     * @param name tên mới
     * @param description mô tả mới
     * @return true nếu cập nhật thành công, false nếu thất bại
     */
    boolean update(String project_id, String team_id, String name, String description);

    /**
     * Cập nhật tên project
     * @param project_id mã project cần cập nhật
     * @param name tên mới
     * @return true nếu cập nhật thành công, false nếu thất bại
     */
    boolean updateName(String project_id, String name);

    /**
     * Cập nhật mô tả project
     * @param project_id mã project cần cập nhật
     * @param description mô tả mới
     * @return true nếu cập nhật thành công, false nếu thất bại
     */
    boolean updateDescription(String project_id, String description);

    /**
     * Xóa project theo project_id
     * @param project_id mã project cần xóa
     * @return true nếu xóa thành công, false nếu thất bại
     */
    boolean deleteById(String project_id);

    /**
     * Xóa tất cả projects của một team
     * @param team_id mã team
     * @return true nếu xóa thành công, false nếu thất bại
     */
    boolean deleteByTeamId(String team_id);
}
