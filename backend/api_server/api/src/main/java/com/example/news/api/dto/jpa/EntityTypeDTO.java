package com.example.news.api.dto.jpa;

public class EntityTypeDTO {
    private int id;
    private String name;


    public EntityTypeDTO() {
    }


    public EntityTypeDTO(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }


}
