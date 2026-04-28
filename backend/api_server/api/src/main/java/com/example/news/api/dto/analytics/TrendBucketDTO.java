package com.example.news.api.dto.analytics;

import java.util.Map;

public class TrendBucketDTO {

    private String date; // e.g., "2026-04-13"
    private long articleCount;
    private double averageSentiment;
    private Map<String, Long> topTopics;
    

    public String getDate() {
        return this.date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public long getArticleCount() {
        return this.articleCount;
    }

    public void setArticleCount(long articleCount) {
        this.articleCount = articleCount;
    }

    public double getAverageSentiment() {
        return this.averageSentiment;
    }

    public void setAverageSentiment(double averageSentiment) {
        this.averageSentiment = averageSentiment;
    }

    public Map<String,Long> getTopTopics() {
        return this.topTopics;
    }

    public void setTopTopics(Map<String,Long> topTopics) {
        this.topTopics = topTopics;
    }

    @Override
    public String toString() {
        return "{" +
            " date='" + getDate() + "'" +
            ", articleCount='" + getArticleCount() + "'" +
            ", averageSentiment='" + getAverageSentiment() + "'" +
            ", topTopics='" + getTopTopics() + "'" +
            "}";
    }

}
