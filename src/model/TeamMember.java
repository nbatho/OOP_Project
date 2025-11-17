package model;

public class TeamMember {
    private String project_id;
    private String user_id;
    private String role_id;

    public TeamMember(String project_id, String user_id, String role_id) {
        this.project_id = project_id;
        this.user_id = user_id;
        this.role_id = role_id;
    }
}
