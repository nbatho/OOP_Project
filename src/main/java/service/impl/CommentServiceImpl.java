package main.java.service.impl;

import main.java.model.Comments;
import main.java.service.CommentService;

import java.util.List;

public class CommentServiceImpl implements CommentService {
    @Override
    public boolean create(Comments comment) {
        return false;
    }

    @Override
    public List<Comments> getAll() {
        return List.of();
    }

    @Override
    public Comments getById(String comment_id) {
        return null;
    }

    @Override
    public List<Comments> getByTaskId(String task_id) {
        return List.of();
    }

    @Override
    public List<Comments> getByUserId(String user_id) {
        return List.of();
    }

    @Override
    public boolean updateBody(String comment_id, String body) {
        return false;
    }

    @Override
    public boolean deleteById(String comment_id) {
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
