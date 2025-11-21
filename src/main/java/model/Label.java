package main.java.model;

public class Label {
    private String labelId;
    private String teamId;
    private String name;
    private String color;

    public Label() {}

    public Label(String labelId, String teamId, String name, String color) {
        this.labelId = labelId;
        this.teamId = teamId;
        this.name = name;
        this.color = color;
    }

    // Getters
    public String getLabelId() {
        return labelId;
    }

    public String getTeamId() {
        return teamId;
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

    // Setters
    public void setLabelId(String labelId) {
        this.labelId = labelId;
    }

    public void setTeamId(String teamId) {
        this.teamId = teamId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setColor(String color) {
        this.color = color;
    }

    @Override
    public String toString() {
        return "Label{" +
                "labelId='" + labelId + '\'' +
                ", teamId='" + teamId + '\'' +
                ", name='" + name + '\'' +
                ", color='" + color + '\'' +
                '}';
    }
}