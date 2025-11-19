package main.java.service.impl;

import main.java.model.TeamMember;
import main.java.service.TeamMemberService;

import java.util.List;

public class TeamMemberServiceImpl implements TeamMemberService {
    @Override
    public boolean add(TeamMember teamMember) {
        return false;
    }

    @Override
    public List<TeamMember> getAll(String team_id) {
        return List.of();
    }

    @Override
    public TeamMember getByUserId(String team_id, String user_id) {
        return null;
    }

    @Override
    public boolean update(String team_id, String old_user_id, String new_user_id, String role_id) {
        return false;
    }

    @Override
    public boolean delete(String team_id, String user_id) {
        return false;
    }
}
