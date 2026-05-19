package com.example.news.api.dto.analytics;

public class GraphLinkDTO {
    private String source;  // ID of the start node
    private String target;  // ID of the end node
    private double value;   // Connection strength (weight)
    private double sentiment; // Mood of the relationship

    public GraphLinkDTO() {
    }

    public GraphLinkDTO(String source, String target, double value, double sentiment) {
        this.source = source;
        this.target = target;
        this.value = value;      // Connection strength
        this.sentiment = sentiment; // Mood
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public double getSentiment() {
        return sentiment;
    }

    public void setSentiment(double sentiment) {
        this.sentiment = sentiment;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }
}
