package main.java.service.impl;

import main.java.model.Project;
import main.java.service.ProjectService;

import java.util.List;

public class ProjectServiceImpl implements ProjectService {
    @Override
    public boolean create(Project project) {
        return false;
    }

    @Override
    public List<Project> getAll() {
        return List.of();
    }

    @Override
    public Project getById(String project_id) {
        return null;
    }

    @Override
    public List<Project> getByTeamId(String team_id) {
        return List.of();
    }

    @Override
    public Project getByName(String name) {
        return null;
    }

    @Override
    public boolean update(String project_id, String team_id, String name, String description) {
        return false;
    }

    @Override
    public boolean updateName(String project_id, String name) {
        return false;
    }

    @Override
    public boolean updateDescription(String project_id, String description) {
        return false;
    }

    @Override
    public boolean deleteById(String project_id) {
        return false;
    }

    @Override
    public boolean deleteByTeamId(String team_id) {
        return false;
    }
}
