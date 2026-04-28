package com.example.news.api.dto.analytics;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Entity {
    @JsonProperty("value")
    private String value;

    @JsonProperty("entity_type")
    private String entityType;

    // Default constructor needed for Jackson
    public Entity() {}

    public Entity(String value, String entityType) {
        this.value = value;
        this.entityType = entityType;
    }

    public String getValue() { return value; }
    public void setValue(String value) { this.value = value; }

    public String getEntityType() { return entityType; }
    public void setEntityType(String entityType) { this.entityType = entityType; }

    @Override
    public String toString() {
        return "{" +
                "value:'" + value + '\'' +
                ", entityType:'" + entityType + '\'' +
                '}';
    }
}