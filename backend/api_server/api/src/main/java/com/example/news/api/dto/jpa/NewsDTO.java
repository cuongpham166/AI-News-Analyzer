package com.example.news.api.dto.jpa;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Set;

public class NewsDTO {
    private int id;
    private String title;
    private Timestamp publishDate;
    private String link;
    private String language;
    private String fullText;
    private String summary;
    private String sentimentLabel;
    private BigDecimal sentiment;
    private Integer topicId;
    private Integer sourceId;
    private String topic_name;
    private String source_name;

    public NewsDTO() {
    }

    public NewsDTO(int id, String title, Timestamp publishDate, String link, String language, String fullText, String summary, String sentimentLabel, BigDecimal sentiment, Integer topicId, Integer sourceId, String topic_name, String source_name) {
        this.id = id;
        this.title = title;
        this.publishDate = publishDate;
        this.link = link;
        this.language = language;
        this.fullText = fullText;
        this.summary = summary;
        this.sentimentLabel = sentimentLabel;
        this.sentiment = sentiment;
        this.topicId = topicId;
        this.sourceId = sourceId;
        this.topic_name = topic_name;
        this.source_name = source_name;
    }
   

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Timestamp getPublishDate() {
        return this.publishDate;
    }

    public void setPublishDate(Timestamp publishDate) {
        this.publishDate = publishDate;
    }

    public String getLink() {
        return this.link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getLanguage() {
        return this.language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getFullText() {
        return this.fullText;
    }

    public void setFullText(String fullText) {
        this.fullText = fullText;
    }

    public String getSummary() {
        return this.summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getSentimentLabel() {
        return this.sentimentLabel;
    }

    public void setSentimentLabel(String sentimentLabel) {
        this.sentimentLabel = sentimentLabel;
    }

    public BigDecimal getSentiment() {
        return this.sentiment;
    }

    public void setSentiment(BigDecimal sentiment) {
        this.sentiment = sentiment;
    }

    public Integer getTopicId() {
        return this.topicId;
    }

    public void setTopicId(Integer topicId) {
        this.topicId = topicId;
    }

    public Integer getSourceId() {
        return this.sourceId;
    }

    public void setSourceId(Integer sourceId) {
        this.sourceId = sourceId;
    }

    public String getTopic_name() {
        return this.topic_name;
    }

    public void setTopic_name(String topic_name) {
        this.topic_name = topic_name;
    }

    public String getSource_name() {
        return this.source_name;
    }

    public void setSource_name(String source_name) {
        this.source_name = source_name;
    }

    @Override
    public String toString() {
        return "{" +
            " id='" + getId() + "'" +
            ", title='" + getTitle() + "'" +
            ", publishDate='" + getPublishDate() + "'" +
            ", link='" + getLink() + "'" +
            ", language='" + getLanguage() + "'" +
            ", fullText='" + getFullText() + "'" +
            ", summary='" + getSummary() + "'" +
            ", sentimentLabel='" + getSentimentLabel() + "'" +
            ", sentiment='" + getSentiment() + "'" +
            ", topicId='" + getTopicId() + "'" +
            ", sourceId='" + getSourceId() + "'" +
            ", topic_name='" + getTopic_name() + "'" +
            ", source_name='" + getSource_name() + "'" +
            "}";
    }


}
