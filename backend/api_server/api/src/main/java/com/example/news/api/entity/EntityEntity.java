package com.example.news.api.entity;

import java.util.Set;

import jakarta.persistence.*;

@Entity
@Table(name = "entity")
public class EntityEntity {
    @Id
    private Integer id;
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "entity_type_id")
    private EntityTypeEntity entityType;

    @ManyToMany
    @JoinTable(
        name = "news_entity",
        joinColumns = @JoinColumn(name = "entity_id"),
        inverseJoinColumns = @JoinColumn(name = "news_id")
    )
    private Set<NewsEntity> newsList;

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public EntityTypeEntity getEntityType() {
        return this.entityType;
    }

    public void setEntityType(EntityTypeEntity entityType) {
        this.entityType = entityType;
    }

    public Set<NewsEntity> getNewsList() {
        return this.newsList;
    }

    public void setNewsList(Set<NewsEntity> newsList) {
        this.newsList = newsList;
    }

}
