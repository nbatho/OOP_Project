package model;

public class Team {
    private String team_id;
    private String name;

    public Team() {}
    public Team(String team_id, String name) {
        this.team_id = team_id;
        this.name = name;
    }

    //Getter

    public String getTeam_id() {
        return team_id;
    }

    public String getName() {
        return name;
    }

    //Setter

    public void setTeam_id(String team_id) {
        this.team_id = team_id;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Team{" +
                "team_id='" + team_id + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
