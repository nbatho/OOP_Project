package main.java.service.impl;

import main.java.model.CheckListItems;
import main.java.service.CheckListItemService;

import java.util.List;

public class CheckListItemServiceImpl implements CheckListItemService {
    @Override
    public boolean create(CheckListItems checklistItem) {
        return false;
    }

    @Override
    public List<CheckListItems> getAll() {
        return List.of();
    }

    @Override
    public CheckListItems getById(String item_id) {
        return null;
    }

    @Override
    public List<CheckListItems> getByChecklistId(String checklist_id) {
        return List.of();
    }

    @Override
    public boolean updateContent(String item_id, String content) {
        return false;
    }

    @Override
    public boolean updateStatus(String item_id, boolean is_done) {
        return false;
    }

    @Override
    public boolean update(String item_id, String content, boolean is_done) {
        return false;
    }

    @Override
    public boolean deleteById(String item_id) {
        return false;
    }

    @Override
    public boolean deleteByChecklistId(String checklist_id) {
        return false;
    }
}
