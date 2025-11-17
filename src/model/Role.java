package model;

public class Role {
    private String role_id;
    private String name;

    public Role() {}

    public Role(String role_id, String name) {
        this.role_id = role_id;
        this.name = name;
    }

    //Getter
    public String getRole_id() {
        return role_id;
    }

    public String getName() {
        return name;
    }

    //Setter
    public void setRole_id(String role_id) {
        this.role_id = role_id;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Role{" +
                "role_id='" + role_id + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
