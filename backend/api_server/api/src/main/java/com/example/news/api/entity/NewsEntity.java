package com.example.news.api.entity;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Set;

import jakarta.persistence.*;

@Entity
@Table(name = "news")
public class NewsEntity {

    @Id
    private Integer id;

    private String title;

    @Column(name = "publish_date")
    private Timestamp publishDate;

    private String link;
    private String lang;

    @Column(name = "full_text")
    private String fullText;

    private String summary;

    @Column(name = "sentiment_label")
    private String sentimentLabel;

    private BigDecimal sentiment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "topic_id")
    private TopicEntity topic;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "source_id")
    private SourceEntity source;

    @ManyToMany(mappedBy = "newsList")
    private Set<EntityEntity> entities;

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
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

    public String getLang() {
        return this.lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
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

    public TopicEntity getTopic() {
        return this.topic;
    }

    public void setTopic(TopicEntity topic) {
        this.topic = topic;
    }

    public SourceEntity getSource() {
        return this.source;
    }

    public void setSource(SourceEntity source) {
        this.source = source;
    }


    public Set<EntityEntity> getEntities() {
        return this.entities;
    }

    public void setEntities(Set<EntityEntity> entities) {
        this.entities = entities;
    }

}