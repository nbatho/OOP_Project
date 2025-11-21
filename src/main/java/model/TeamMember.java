package main.java.model;

public class TeamMember {
    private String teamId;
    private String userId;
    private String roleId;

    public TeamMember() {}

    public TeamMember(String teamId, String userId, String roleId) {
        this.teamId = teamId;
        this.userId = userId;
        this.roleId = roleId;
    }

    // Getters
    public String getTeamId() {
        return teamId;
    }

    public String getUserId() {
        return userId;
    }

    public String getRoleId() {
        return roleId;
    }

    // Setters
    public void setTeamId(String teamId) {
        this.teamId = teamId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    @Override
    public String toString() {
        return "TeamMember{" +
                "teamId='" + teamId + '\'' +
                ", userId='" + userId + '\'' +
                ", roleId='" + roleId + '\'' +
                '}';
    }
}
