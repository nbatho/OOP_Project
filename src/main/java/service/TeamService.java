package main.java.service;

import java.util.List;
import main.java.model.Team;

public interface TeamService {
    /**
     * Tạo một team mới
     * @param team đối tượng Team cần tạo
     * @return true nếu tạo thành công, false nếu thất bại
     */
    boolean createTeam(Team team);

    /**
     * Lấy danh sách tất cả các team
     * @return List<Team> danh sách team, có thể rỗng
     */
    List<Team> getAllTeams();

    /**
     * Lấy team theo teamId
     * @param teamId mã của team cần tìm
     * @return Team nếu tìm thấy, null nếu không tìm thấy
     */
    Team getTeamById(String teamId);

    /**
     * Lấy team theo tên
     * @param name tên team cần tìm
     * @return Team nếu tìm thấy, null nếu không tìm thấy
     */
    Team getTeamByName(String name);

    /**
     * Cập nhật thông tin team
     * @param team đối tượng Team với thông tin mới
     * @return true nếu cập nhật thành công, false nếu thất bại
     */
    boolean updateTeam(Team team);

    /**
     * Cập nhật tên team
     * @param teamId mã team cần cập nhật
     * @param name tên mới
     * @return true nếu cập nhật thành công, false nếu thất bại
     */
    boolean updateTeamName(String teamId, String name);

    /**
     * Xóa team theo teamId
     * @param teamId mã team cần xóa
     * @return true nếu xóa thành công, false nếu thất bại
     */
    boolean deleteTeam(String teamId);

    /**
     * Kiểm tra tên team đã tồn tại chưa
     * @param name tên team cần kiểm tra
     * @return true nếu tên đã tồn tại, false nếu chưa
     */
    boolean isTeamNameExists(String name);

    /**
     * Kiểm tra team có tồn tại không
     * @param teamId mã team cần kiểm tra
     * @return true nếu team tồn tại, false nếu không
     */
    boolean teamExists(String teamId);
}
