package com.example.news.api.dto;

public class EventTrackerDTO {
    private String event;
    private String location;
    private int strength;


    public EventTrackerDTO() {
    }

    public EventTrackerDTO(String event, String location, int strength) {
        this.event = event;
        this.location = location;
        this.strength = strength;
    }


    public String getEvent() {
        return this.event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public String getLocation() {
        return this.location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getStrength() {
        return this.strength;
    }

    public void setStrength(int strength) {
        this.strength = strength;
    }

    @Override
    public String toString() {
        return "{" +
            " event='" + getEvent() + "'" +
            ", location='" + getLocation() + "'" +
            ", strength='" + getStrength() + "'" +
            "}";
    }

}


