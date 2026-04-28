package com.example.news.api.dto.analytics;

import java.util.Map;
import java.util.List;
import java.util.stream.Collectors;

public class GlobalEntityTrendsDTO {
    private Map<String, List<EntityCountDTO>> timeline;


    public GlobalEntityTrendsDTO() {
    }

    public GlobalEntityTrendsDTO(Map<String,List<EntityCountDTO>> timeline) {
        this.timeline = timeline;
    }

    public Map<String,List<EntityCountDTO>> getTimeline() {
        return this.timeline;
    }

    public void setTimeline(Map<String,List<EntityCountDTO>> timeline) {
        this.timeline = timeline;
    }



    @Override
    public String toString() {
        if (timeline == null) return "{}";

        String timelineStr = timeline.entrySet().stream()
            .map(entry -> "\"" + entry.getKey() + "\": " + entry.getValue())
            .collect(Collectors.joining(", "));

        return "{ " + timelineStr + " }";
    }


}
