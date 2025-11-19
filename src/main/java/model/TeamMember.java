package main.java.model;

public class TeamMember {
    private String team_id;
    private String user_id;
    private String role_id;

    public TeamMember() {}
    public TeamMember(String team_id, String user_id, String role_id) {
        this.team_id = team_id;
        this.user_id = user_id;
        this.role_id = role_id;
    }

    //Getter
    public String getTeam_id() {
        return team_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public String getRole_id() {
        return role_id;
    }

    //Setter
    public void setTeam_id(String project_id) {
        this.team_id = project_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public void setRole_id(String role_id) {
        this.role_id = role_id;
    }

    @Override
    public String toString() {
        return "TeamMember{" +
                "project_id='" + team_id + '\'' +
                ", user_id='" + user_id + '\'' +
                ", role_id='" + role_id + '\'' +
                '}';
    }
}
