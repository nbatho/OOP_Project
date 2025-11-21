package main.java.model;

public class ProjectMember {
    private String projectId;
    private String userId;
    private String roleId;

    public ProjectMember() {}

    public ProjectMember(String projectId, String userId, String roleId) {
        this.projectId = projectId;
        this.userId = userId;
        this.roleId = roleId;
    }

    // Getters
    public String getProjectId() {
        return projectId;
    }

    public String getUserId() {
        return userId;
    }

    public String getRoleId() {
        return roleId;
    }

    // Setters
    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    @Override
    public String toString() {
        return "ProjectMember{" +
                "projectId='" + projectId + '\'' +
                ", userId='" + userId + '\'' +
                ", roleId='" + roleId + '\'' +
                '}';
    }
}
