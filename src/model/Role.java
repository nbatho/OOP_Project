package model;

public class Role {
    private String role_id;
    private String name;

    public Role(String role_id, String name) {
        this.role_id = role_id;
        this.name = name;
    }

    public String getRole_id() {
        return role_id;
    }

    public String getRole_name() {
        return name;
    }
}
