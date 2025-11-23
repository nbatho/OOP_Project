package main.java.model;

public class Permission {
    private String permissionId;
    private String name;
    private String description;

    public Permission() {}

    public Permission(String permissionId, String name, String description) {
        this.permissionId = permissionId;
        this.name = name;
        this.description = description;
    }

    // Getters
    public String getPermissionId() {
        return permissionId;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    // Setters
    public void setPermissionId(String permissionId) {
        this.permissionId = permissionId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "Permission{" +
                "permissionId='" + permissionId + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
