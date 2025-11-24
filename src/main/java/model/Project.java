package main.java.model;

import java.sql.Timestamp;

public class Project {
    private String projectId;
    private String teamId;
    private String name;
    private String description;
    private Timestamp createdAt;

    public Project() {}

    public Project(String projectId, String teamId, String name, String description) {
        this.projectId = projectId;
        this.teamId = teamId;
        this.name = name;
        this.description = description;
        this.createdAt = new Timestamp(System.currentTimeMillis());
    }

    public Project(String projectId, String name, String description) {
        this.projectId = projectId;
        this.name = name;
        this.description = description;
    }

    // Getters
    public String getProjectId() {
        return projectId;
    }

    public String getTeamId() {
        return teamId;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    // Setters
    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public void setTeamId(String teamId) {
        this.teamId = teamId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "Project{" +
                "projectId='" + projectId + '\'' +
                ", teamId='" + teamId + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}
