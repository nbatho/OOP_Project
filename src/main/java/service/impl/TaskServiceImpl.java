package main.java.service.impl;

import main.java.model.Task;
import main.java.service.TaskService;

import java.util.Date;
import java.util.List;

public class TaskServiceImpl implements TaskService {
    @Override
    public boolean create(Task task) {
        return false;
    }

    @Override
    public List<Task> getAll(String project_id) {
        return List.of();
    }

    @Override
    public Task getById(String task_id, String project_id) {
        return null;
    }

    @Override
    public boolean update(String task_id, String project_id, String title, String description, String status, String priority, Date due_date) {
        return false;
    }

    @Override
    public boolean updateStatus(String task_id, String project_id, String status) {
        return false;
    }

    @Override
    public boolean updatePriority(String task_id, String project_id, String priority) {
        return false;
    }

    @Override
    public boolean updateDueDate(String task_id, String project_id, Date due_date) {
        return false;
    }

    @Override
    public boolean deleteById(String task_id, String project_id) {
        return false;
    }
}
