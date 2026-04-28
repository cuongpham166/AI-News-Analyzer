package com.example.news.api.dto;

public class EntityCountDTO {
    private String name;
    private long count;
    private Double averageSentiment;

    public EntityCountDTO(String name, long count, Double averageSentiment) {
        this.name = name;
        this.count = count;
        this.averageSentiment = averageSentiment;
    }
    
    public EntityCountDTO() {
    }
    
    public Double getAverageSentiment() {
        return this.averageSentiment;
    }

    public void setAverageSentiment(Double averageSentiment) {
        this.averageSentiment = averageSentiment;
    }
    


    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getCount() {
        return this.count;
    }

    public void setCount(long count) {
        this.count = count;
    }


    @Override
    public String toString() {
        return "{ " +
               "\"name\": \"" + name + "\", " +
               "\"count\": " + count + ", " +
               "\"averageSentiment\": " + averageSentiment +
               " }";
    }
}
