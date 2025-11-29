package main.java.service;

import java.util.List;
import main.java.model.Project;

public interface ProjectService {
    /**
     * Tạo một project mới
     * @param project đối tượng Project cần tạo
     * @return true nếu tạo thành công, false nếu thất bại
     */
    String createProject(Project project);
    /**
     * Lấy danh sách tất cả các project
     * @return List<Project> danh sách project, có thể rỗng
     */
    List<Project> getAllProjects();

    String[] getProjectNameByUserId(String userId);


    /**
     * Lấy project theo projectId
     * @param projectId mã của project cần tìm
     * @return Project nếu tìm thấy, null nếu không tìm thấy
     */
    Project getProjectById(String projectId);


    /**
     * Lấy project theo tên
     * @param name tên project cần tìm
     * @return Project nếu tìm thấy, null nếu không tìm thấy
     */
    Project getProjectByName(String name);

    /**
     * Cập nhật thông tin project
     * @param project đối tượng Project với thông tin mới
     * @return true nếu cập nhật thành công, false nếu thất bại
     */
    boolean updateProject(Project project);

    /**
     * Xóa project theo projectId
     * @param projectId mã project cần xóa
     * @return true nếu xóa thành công, false nếu thất bại
     */
    boolean deleteProject(String projectId);

    /**
     * Kiểm tra tên project đã tồn tại chưa
     * @param name tên project cần kiểm tra
     * @return true nếu tên đã tồn tại, false nếu chưa
     */
    boolean isProjectNameExists(String name);

    /**
     * Kiểm tra project có tồn tại không
     * @param projectId mã project cần kiểm tra
     * @return true nếu project tồn tại, false nếu không
     */
    boolean projectExists(String projectId);
}
