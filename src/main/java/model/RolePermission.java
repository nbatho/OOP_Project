package main.java.model;

public class RolePermission {
    private String roleId;
    private String permissionId;

    public RolePermission() {}

    public RolePermission(String roleId, String permissionId) {
        this.roleId = roleId;
        this.permissionId = permissionId;
    }

    // Getters
    public String getRoleId() {
        return roleId;
    }

    public String getPermissionId() {
        return permissionId;
    }

    // Setters
    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public void setPermissionId(String permissionId) {
        this.permissionId = permissionId;
    }

    @Override
    public String toString() {
        return "RolePermission{" +
                "roleId='" + roleId + '\'' +
                ", permissionId='" + permissionId + '\'' +
                '}';
    }
}
