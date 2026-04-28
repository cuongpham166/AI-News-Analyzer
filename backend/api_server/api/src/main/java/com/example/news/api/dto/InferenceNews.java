package com.example.news.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Date;
import java.util.List;

public class InferenceNews {

    @JsonProperty("@timestamp")
    private Date timestamp;

    @JsonProperty("sentiment_label")
    private String sentimentLabel;

    private Float sentiment;
    private String topic;

    @JsonProperty("entities")
    private List<Entity> entities;

    private String summary;
    private String link;

    @JsonProperty("publish_date")
    private long publishDate;

    private String title;
    private String source;

    // --- Getters and setters ---
    public Date getTimestamp() { return timestamp; }
    public void setTimestamp(Date timestamp) { this.timestamp = timestamp; }

    public long getPublishDate() {return publishDate;}
    public void setPublishDate(long publishDate) { this.publishDate = publishDate; }

    public String getSentimentLabel() { return sentimentLabel; }
    public void setSentimentLabel(String sentimentLabel) { this.sentimentLabel = sentimentLabel; }

    public Float getSentiment() { return sentiment; }
    public void setSentiment(Float sentiment) { this.sentiment = sentiment; }

    public String getTopic() { return topic; }
    public void setTopic(String topic) { this.topic = topic; }

    public List<Entity> getEntities() { return entities; }
    public void setEntities(List<Entity> entities) { this.entities = entities; }

    public String getSummary() { return summary; }
    public void setSummary(String summary) { this.summary = summary; }

    public String getLink() { return link; }
    public void setLink(String link) { this.link = link; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getSource() { return source; }
    public void setSource(String source) { this.source = source; }

    @Override
    public String toString() {
        return "{" +
                "timestamp:'" + timestamp + '\'' +
                ", sentimentLabel:'" + sentimentLabel + '\'' +
                ", sentiment:" + sentiment +
                ", topic:'" + topic + '\'' +
                ", entities:" + getEntities() +
                ", summary:'" + summary + '\'' +
                ", link:'" + link + '\'' +
                ", publishDate:" + ""+publishDate +
                ", title:'" + title + '\'' +
                ", source:'" + source + '\'' +
                '}';
    }
}