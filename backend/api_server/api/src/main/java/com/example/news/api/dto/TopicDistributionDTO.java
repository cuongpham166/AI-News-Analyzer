package com.example.news.api.dto;

public class TopicDistributionDTO {
    private String name;
    private long count;


    public TopicDistributionDTO() {
    }

    public TopicDistributionDTO(String name, long count) {
        this.name = name;
        this.count = count;
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
        return "{" +
            " name='" + getName() + "'" +
            ", count='" + getCount() + "'" +
            "}";
    }

}
