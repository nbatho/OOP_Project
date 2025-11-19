package main.java.service;

import main.java.model.TeamMember;

import java.util.List;

public interface TeamMemberService {
    /**
     * Thêm một team member mới
     * @param teamMember đối tượng TeamMember cần thêm
     * @return true nếu thêm thành công, false nếu thất bại
     */
    boolean add(TeamMember teamMember);

    /**
     * Lấy danh sách tất cả member của một team
     * @param team_id mã team
     * @return List<TeamMember> danh sách member, có thể rỗng
     */
    List<TeamMember> getAll(String team_id);

    /**
     * Lấy một team member theo team_id và user_id
     * @param team_id mã team
     * @param user_id mã user
     * @return TeamMember nếu tìm thấy, null nếu không tìm thấy
     */
    TeamMember getByUserId(String team_id, String user_id);

    /**
     * Cập nhật thông tin user_id và role_id của một team member
     * @param team_id mã team
     * @param old_user_id user_id cũ cần update
     * @param new_user_id user_id mới
     * @param role_id role_id mới
     * @return true nếu update thành công, false nếu thất bại
     */
    boolean update(String team_id, String old_user_id, String new_user_id, String role_id);

    /**
     * Xóa một team member theo team_id và user_id
     * @param team_id mã team
     * @param user_id mã user
     * @return true nếu xóa thành công, false nếu thất bại
     */
    boolean delete(String team_id, String user_id);
}
