package com.example.news.api.dto.analytics;

public class VolatilityIndexDTO {
    private String entity_name;
    private float avg_sentiment;
    private float volatility;
    private int mentions;


    public VolatilityIndexDTO() {
    }

    public VolatilityIndexDTO(String entity_name, float avg_sentiment, float volatility, int mentions) {
        this.entity_name = entity_name;
        this.avg_sentiment = avg_sentiment;
        this.volatility = volatility;
        this.mentions = mentions;
    }

    public String getEntity_name() {
        return this.entity_name;
    }

    public void setEntity_name(String entity_name) {
        this.entity_name = entity_name;
    }

    public float getAvg_sentiment() {
        return this.avg_sentiment;
    }

    public void setAvg_sentiment(float avg_sentiment) {
        this.avg_sentiment = avg_sentiment;
    }

    public float getVolatility() {
        return this.volatility;
    }

    public void setVolatility(float volatility) {
        this.volatility = volatility;
    }

    public int getMentions() {
        return this.mentions;
    }

    public void setMentions(int mentions) {
        this.mentions = mentions;
    }

    @Override
    public String toString() {
        return "{" +
            " entity_name='" + getEntity_name() + "'" +
            ", avg_sentiment='" + getAvg_sentiment() + "'" +
            ", volatility='" + getVolatility() + "'" +
            ", mentions='" + getMentions() + "'" +
            "}";
    }
}
