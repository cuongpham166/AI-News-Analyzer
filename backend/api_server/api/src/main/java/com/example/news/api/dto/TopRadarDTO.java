package com.example.news.api.dto;

import java.util.List;

public class TopRadarDTO {
    private long count;
    private List<TopicDistributionDTO> distribution;


    public TopRadarDTO() {
    }

    public TopRadarDTO(long count, List<TopicDistributionDTO> distribution) {
        this.count = count;
        this.distribution = distribution;
    }

    public long getCount() {
        return this.count;
    }

    public void setCount(long count) {
        this.count = count;
    }

    public List<TopicDistributionDTO> getDistribution() {
        return this.distribution;
    }

    public void setDistribution(List<TopicDistributionDTO> distribution) {
        this.distribution = distribution;
    }

    @Override
    public String toString() {
        return "{" +
            " count='" + getCount() + "'" +
            ", distribution='" + getDistribution() + "'" +
            "}";
    }

}
