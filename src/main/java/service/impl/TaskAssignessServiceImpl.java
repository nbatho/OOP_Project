package main.java.service.impl;

import main.java.model.TaskAsigness;
import main.java.service.TaskAssignessService;

import java.util.List;

public class TaskAssignessServiceImpl implements TaskAssignessService {
    @Override
    public boolean create(TaskAsigness taskAsigness) {
        return false;
    }

    @Override
    public List<TaskAsigness> getAll() {
        return List.of();
    }

    @Override
    public TaskAsigness getByTaskIdAndUserId(String task_id, String user_id) {
        return null;
    }

    @Override
    public List<TaskAsigness> getByTaskId(String task_id) {
        return List.of();
    }

    @Override
    public List<TaskAsigness> getByUserId(String user_id) {
        return List.of();
    }

    @Override
    public boolean deleteByTaskIdAndUserId(String task_id, String user_id) {
        return false;
    }

    @Override
    public boolean deleteByTaskId(String task_id) {
        return false;
    }

    @Override
    public boolean deleteByUserId(String user_id) {
        return false;
    }
}
