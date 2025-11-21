package main.java.model;

import java.sql.Timestamp;

public class Team {
    private String teamId;
    private String name;
    private Timestamp createdAt;

    public Team() {}

    public Team(String teamId, String name) {
        this.teamId = teamId;
        this.name = name;
        this.createdAt = new Timestamp(System.currentTimeMillis());
    }

    public Team(String teamId, String name, Timestamp createdAt) {
        this.teamId = teamId;
        this.name = name;
        this.createdAt = createdAt;
    }

    // Getters
    public String getTeamId() {
        return teamId;
    }

    public String getName() {
        return name;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    // Setters
    public void setTeamId(String teamId) {
        this.teamId = teamId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "Team{" +
                "teamId='" + teamId + '\'' +
                ", name='" + name + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}
