package com.example.news.api.dto.analytics;

public class DetailedEntityDTO {
    private Integer entityId;
    private String entityName;
    private String entityTypeName;
    private Integer entityTypeId;

    public DetailedEntityDTO(){}

    public DetailedEntityDTO(Integer entityId, String entityName, Integer entityTypeId, String entityTypeName) {
        this.entityId = entityId;
        this.entityName = entityName;
        this.entityTypeId = entityTypeId;
        this.entityTypeName = entityTypeName;
    }

    public Integer getEntityId() {
        return entityId;
    }

    public void setEntityId(Integer entityId) {
        this.entityId = entityId;
    }

    public String getEntityName() {
        return entityName;
    }

    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }

    public Integer getEntityTypeId() {
        return entityTypeId;
    }

    public void setEntityTypeId(Integer entityTypeId) {
        this.entityTypeId = entityTypeId;
    }

    public String getEntityTypeName() {
        return entityTypeName;
    }

    public void setEntityTypeName(String entityTypeName) {
        this.entityTypeName = entityTypeName;
    }
}
