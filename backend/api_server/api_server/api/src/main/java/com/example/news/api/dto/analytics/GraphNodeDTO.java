package com.example.news.api.dto.analytics;

public class GraphNodeDTO {
    private String id;      // Unique ID (e.g., entity name)
    private String label;   // Display name
    private String group;   // Entity type (Person, Org, etc.) for coloring
    private double size;    // Importance (e.g., mention count)
    private double sentiment; // Average sentiment of this entity

    public GraphNodeDTO() {
    }

    public GraphNodeDTO(String id, String label, String group, double size, double sentiment) {
        this.id = id;
        this.label = label;
        this.group = group;
        this.size = size;
        this.sentiment = sentiment;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public double getSentiment() {
        return sentiment;
    }

    public void setSentiment(double sentiment) {
        this.sentiment = sentiment;
    }

    public double getSize() {
        return size;
    }

    public void setSize(double size) {
        this.size = size;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
}
