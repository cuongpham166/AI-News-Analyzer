package com.example.news.api.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "news")
public class SourceEntity {
    @Id
    private Integer id;
    private String name;


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

}
