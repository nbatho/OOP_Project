package main.java.model;


public class Project {
    private String project_id;
    private String team_id;
    private String name;
    private String description;

    public Project() {}

    public Project(String project_id, String team_id, String name, String description) {
        this.project_id = project_id;
        this.team_id = team_id;
        this.name = name;
        this.description = description;
    }

    //Getter


    public String getProject_id() {
        return project_id;
    }

    public String getTeam_id() {
        return team_id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    //Setter


    public void setProject_id(String project_id) {
        this.project_id = project_id;
    }

    public void setTeam_id(String team_id) {
        this.team_id = team_id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "Project{" +
                "project_id='" + project_id + '\'' +
                ", team_id='" + team_id + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
