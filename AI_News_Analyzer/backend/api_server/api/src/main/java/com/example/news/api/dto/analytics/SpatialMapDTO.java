package com.example.news.api.dto.analytics;

public class SpatialMapDTO {
    private String location;
    private int count;


    public SpatialMapDTO() {
    }

    public SpatialMapDTO(String location, int count) {
        this.location = location;
        this.count = count;
    }

    public String getLocation() {
        return this.location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getCount() {
        return this.count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    @Override
    public String toString() {
        return "{" +
            " location='" + getLocation() + "'" +
            ", count='" + getCount() + "'" +
            "}";
    }

}
