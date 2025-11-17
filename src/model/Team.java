package model;

public class Team {
    private String team_id;
    private String name;

    public Team(String team_id, String name) {
        this.team_id = team_id;
        this.name = name;
    }

    public String getTeam_id() {
        return team_id;
    }

    public String getName() {
        return name;
    }
}
