package com.example.news.api.entity;

import java.util.Set;

import jakarta.persistence.*;

@Entity
@Table(name = "entity_entity")
public class EntityTypeEntity {
    @Id
    private Integer id;
    private String name;

    @OneToMany(mappedBy = "entityType")
    private Set<EntityEntity> entities;

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


    public Set<EntityEntity> getEntities() {
        return this.entities;
    }

    public void setEntities(Set<EntityEntity> entities) {
        this.entities = entities;
    }


}
