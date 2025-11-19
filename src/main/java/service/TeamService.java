package main.java.service;

import main.java.model.Team;

import java.util.List;

public interface TeamService {
    /**
     * Tạo một team mới
     * @param team đối tượng Team cần tạo
     * @return true nếu tạo thành công, false nếu thất bại
     */
    boolean create(Team team);

    /**
     * Lấy danh sách tất cả các team
     * @param uuid định danh người dùng (có thể dùng để lọc)
     * @return List<Team> danh sách team, có thể rỗng
     */
    List<Team> getAll(String uuid);

    /**
     * Lấy team theo team_id
     * @param team_id mã của team cần tìm
     * @return Team nếu tìm thấy, null nếu không tìm thấy
     */
    Team getById(String team_id);

    /**
     * Lấy team theo tên
     * @param name tên team cần tìm
     * @return Team nếu tìm thấy, null nếu không tìm thấy
     */
    Team getByName(String name);

    /**
     * Cập nhật tên team
     * @param team_id mã team cần cập nhật
     * @param name tên mới
     * @return true nếu cập nhật thành công, false nếu thất bại
     */
    boolean updateName(String team_id, String name);

    /**
     * Xóa team theo team_id
     * @param team_id mã team cần xóa
     * @return true nếu xóa thành công, false nếu thất bại
     */
    boolean deleteById(String team_id);
}
