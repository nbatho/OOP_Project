package main.java.service.impl;

import java.util.List;
import main.java.model.TeamMember;
import main.java.service.TeamMemberService;
import main.java.service.TeamService;
import main.java.service.UserService;

public class TeamMemberServiceImpl implements TeamMemberService {
    private final TeamService teamService;
    private final UserService userService;

    public TeamMemberServiceImpl() {
        this.teamService = new TeamServiceImpl();
        this.userService = new UserServiceImpl();
    }

    @Override
    public boolean add(TeamMember teamMember) {
        try {
            if (teamMember == null) {
                System.out.println("TeamMember không được null");
                return false;
            }

            if (!isValidTeamMemberData(teamMember)) {
                return false;
            }

            // Kiểm tra team có tồn tại không
            if (!teamService.teamExists(teamMember.getTeamId())) {
                System.out.println("Team không tồn tại: " + teamMember.getTeamId());
                return false;
            }

            // Kiểm tra user có tồn tại không
            if (!userService.userExists(teamMember.getUserId())) {
                System.out.println("User không tồn tại: " + teamMember.getUserId());
                return false;
            }

            // Kiểm tra user đã là member của team chưa
            TeamMember existing = getByUserId(teamMember.getTeamId(), teamMember.getUserId());
            if (existing != null) {
                System.out.println("User đã là member của team");
                return false;
            }

            // return teamMemberRepository.createTeamMember(teamMember);
            System.out.println("Thêm team member thành công: " + teamMember.getUserId());
            return true;
        } catch (Exception e) {
            System.out.println("Lỗi khi thêm team member: " + e.getMessage());
            return false;
        }
    }

    @Override
    public List<TeamMember> getAll(String team_id) {
        try {
            if (team_id == null || team_id.trim().isEmpty()) {
                System.out.println("Team ID không được null hoặc rỗng");
                return List.of();
            }

            // return teamMemberRepository.getMembersByTeam(team_id);
            System.out.println("Lấy danh sách members của team: " + team_id);
            return List.of(); // Placeholder
        } catch (Exception e) {
            System.out.println("Lỗi khi lấy members của team: " + e.getMessage());
            return List.of();
        }
    }

    @Override
    public TeamMember getByUserId(String team_id, String user_id) {
        try {
            if (team_id == null || team_id.trim().isEmpty()) {
                System.out.println("Team ID không được null hoặc rỗng");
                return null;
            }

            if (user_id == null || user_id.trim().isEmpty()) {
                System.out.println("User ID không được null hoặc rỗng");
                return null;
            }

            // return teamMemberRepository.getByTeamAndUser(team_id, user_id);
            System.out.println("Lấy team member theo team và user: " + team_id + ", " + user_id);
            return null; // Placeholder
        } catch (Exception e) {
            System.out.println("Lỗi khi lấy team member: " + e.getMessage());
            return null;
        }
    }

    @Override
    public boolean update(String team_id, String old_user_id, String new_user_id, String role_id) {
        try {
            TeamMember member = getByUserId(team_id, old_user_id);
            if (member == null) {
                System.out.println("Team member không tồn tại");
                return false;
            }

            // Kiểm tra new_user_id có tồn tại không
            if (!userService.userExists(new_user_id)) {
                System.out.println("User mới không tồn tại: " + new_user_id);
                return false;
            }

            member.setUserId(new_user_id);
            if (role_id != null && !role_id.trim().isEmpty()) {
                member.setRoleId(role_id);
            }

            // return teamMemberRepository.updateTeamMember(member);
            System.out.println("Cập nhật team member thành công");
            return true;
        } catch (Exception e) {
            System.out.println("Lỗi khi cập nhật team member: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean delete(String team_id, String user_id) {
        try {
            if (getByUserId(team_id, user_id) == null) {
                System.out.println("Team member không tồn tại");
                return false;
            }

            // return teamMemberRepository.deleteByTeamAndUser(team_id, user_id);
            System.out.println("Xóa team member thành công");
            return true;
        } catch (Exception e) {
            System.out.println("Lỗi khi xóa team member: " + e.getMessage());
            return false;
        }
    }

    /**
     * Validate dữ liệu team member
     */
    private boolean isValidTeamMemberData(TeamMember member) {
        if (member.getTeamId() == null || member.getTeamId().trim().isEmpty()) {
            System.out.println("Team ID không được null hoặc rỗng");
            return false;
        }

        if (member.getUserId() == null || member.getUserId().trim().isEmpty()) {
            System.out.println("User ID không được null hoặc rỗng");
            return false;
        }

        return true;
    }
}
