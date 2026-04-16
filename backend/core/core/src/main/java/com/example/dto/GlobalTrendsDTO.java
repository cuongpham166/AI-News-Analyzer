package com.example.dto;

import java.util.List;
import com.example.dto.TrendBucketDTO;

public class GlobalTrendsDTO {
    private List<TrendBucketDTO> timeline;

    public GlobalTrendsDTO() {}
    
    public GlobalTrendsDTO(List<TrendBucketDTO> timeline) {
        this.timeline = timeline;
    }

    public List<TrendBucketDTO> getTimeline() {
        return this.timeline;
    }

    public void setTimeline(List<TrendBucketDTO> timeline) {
        this.timeline = timeline;
    }

    @Override
    public String toString() {
        return "{" +
            " timeline='" + getTimeline() + "'" +
            "}";
    }


}
