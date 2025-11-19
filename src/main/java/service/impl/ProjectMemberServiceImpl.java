package main.java.service.impl;

import main.java.model.ProjectMember;
import main.java.service.ProjectMemberService;

import java.util.List;

public class ProjectMemberServiceImpl implements ProjectMemberService {
    @Override
    public boolean create(ProjectMember projectMember) {
        return false;
    }

    @Override
    public List<ProjectMember> getAll() {
        return List.of();
    }

    @Override
    public ProjectMember getByProjectIdAndUserId(String project_id, String user_id) {
        return null;
    }

    @Override
    public List<ProjectMember> getByProjectId(String project_id) {
        return List.of();
    }

    @Override
    public List<ProjectMember> getByUserId(String user_id) {
        return List.of();
    }

    @Override
    public List<ProjectMember> getByRoleId(String role_id) {
        return List.of();
    }

    @Override
    public boolean updateRole(String project_id, String user_id, String role_id) {
        return false;
    }

    @Override
    public boolean deleteByProjectIdAndUserId(String project_id, String user_id) {
        return false;
    }

    @Override
    public boolean deleteByProjectId(String project_id) {
        return false;
    }

    @Override
    public boolean deleteByUserId(String user_id) {
        return false;
    }
}
