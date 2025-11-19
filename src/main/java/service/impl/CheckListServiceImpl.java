package main.java.service.impl;

import main.java.model.Checklist;
import main.java.service.CheckListService;

import java.util.List;

public class CheckListServiceImpl implements CheckListService {
    @Override
    public boolean create(Checklist checklist) {
        return false;
    }

    @Override
    public List<Checklist> getAll() {
        return List.of();
    }

    @Override
    public Checklist getById(String checklist_id) {
        return null;
    }

    @Override
    public List<Checklist> getByTaskId(String task_id) {
        return List.of();
    }

    @Override
    public Checklist getByTitle(String title) {
        return null;
    }

    @Override
    public boolean updateTitle(String checklist_id, String title) {
        return false;
    }

    @Override
    public boolean deleteById(String checklist_id) {
        return false;
    }

    @Override
    public boolean deleteByTaskId(String task_id) {
        return false;
    }
}
