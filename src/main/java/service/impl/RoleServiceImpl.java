package main.java.service.impl;

import main.java.model.Role;
import main.java.service.RoleService;

import java.util.List;

public class RoleServiceImpl implements RoleService {
    @Override
    public boolean create(Role role) {
        return false;
    }

    @Override
    public List<Role> getAll() {
        return List.of();
    }

    @Override
    public Role getById(String role_id) {
        return null;
    }

    @Override
    public Role getByName(String name) {
        return null;
    }

    @Override
    public boolean updateName(String role_id, String name) {
        return false;
    }

    @Override
    public boolean deleteById(String role_id) {
        return false;
    }
}
